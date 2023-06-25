package com.example.e_attendece_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class F_View_Today_Attendance extends AppCompatActivity {

    TextView date2;
   private DatePickerDialog datePickerDialog;
   FirebaseFirestore fStore;
   FirebaseAuth fAuth;
   FirebaseDatabase fDatabase;
   DatabaseReference dReference;
   String date;


   int flag=0;
   ListView list_view,list_student;
ArrayList<String> arr=new ArrayList<>();
    ArrayList<String>student_name=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fview_today_attendance);
        date2=findViewById(R.id.tv_date);
        list_view=findViewById(R.id.F_attendance_list);
        list_student=findViewById(R.id.F_student_name_list);

        fStore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();
        fDatabase=FirebaseDatabase.getInstance();
        dReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://e-attendance-4bd5e-default-rtdb.firebaseio.com/");

        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
    }

    private void getDate() {
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
               date=makeDateString(year,month,dayOfMonth);
                date2.setText(date);
                String uid=fAuth.getCurrentUser().getUid();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arr);

                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, student_name);

                fStore.collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String s=documentSnapshot.getString("faculty_subject");
                        fStore.collection("Users").whereEqualTo("isStudent","3").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                student_name.clear();
                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    String name = snapshot.getString("student_name");
                                    student_name.add(name);
                                    dReference=FirebaseDatabase.getInstance().getReference(s).child(date);
                                    dReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            arr.clear();
                                         if (snapshot.exists()){
                                             for (DataSnapshot snapshot1:snapshot.getChildren()){
                                                 if (snapshot1.exists()) {
                                                     arr.add(snapshot1.getValue(String.class));
                                                 }
                                                 else{
                                                     arr.add("Not Marked");
                                                 }

                                             }
                                             adapter.notifyDataSetChanged();

                                         }
                                         else {

                                                 flag=1;

                                         }


                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                adapter2.notifyDataSetChanged();

                                list_view.setAdapter(adapter);
                                list_student.setAdapter(adapter2);

                            }
                        });



                    }
                });
            }
        };
        if (flag==1){
            Toast.makeText(F_View_Today_Attendance.this, "some", Toast.LENGTH_SHORT).show();
        }
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);
        int style= AlertDialog.THEME_HOLO_LIGHT;
        DatePickerDialog datePickerDialog=new DatePickerDialog(this,style,dateSetListener,year,month,day);
        datePickerDialog.show();


    }

    private String makeDateString(int year, int month, int dayOfMonth) {
        return year+"-"+month+"-"+dayOfMonth;
    }

    public void date_Picker_show(View view) {
       if( datePickerDialog!=null) {

       }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Faculty_Home_Page.class));
        finish();
    }
}