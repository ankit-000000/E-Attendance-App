package com.example.e_attendece_application;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class T_Attendance extends AppCompatActivity {


    TextView dateFormat;
    int year,month,day;


    ListView student_list;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayList<String> arrNamesList =new ArrayList<>();
    ArrayList<String> Present=new ArrayList<>();
    ArrayList<String> Absent=new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tattendance);

        dateFormat=findViewById(R.id.Attendance_Date);
        Calendar calendar=Calendar.getInstance();

        student_list=findViewById(R.id.List);



        fStore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://e-attendance-4bd5e-default-rtdb.firebaseio.com/");


        String Date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        dateFormat.setText(Date);

        fStore.collection("Users").whereEqualTo("isStudent","3").addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

               if (value!=null) {
                   arrNamesList.clear();
                   for (QueryDocumentSnapshot snapshot : value) {

                       arrNamesList.add(snapshot.getString("student_name"));
                   }
               }
               ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_multiple_choice,arrNamesList);
               adapter.notifyDataSetChanged();
               Collections.sort(arrNamesList);
               student_list.setAdapter(adapter);
           }
       });


        student_list.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Faculty_Home_Page.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id== R.id.itemDone){


            String itemSelected="selected item \n";
            for ( int i=0;i<student_list.getCount();i++){
                if(student_list.isItemChecked(i)){
                    Present.add(student_list.getItemAtPosition(i).toString());
                    Toast.makeText(this, "HELLO", Toast.LENGTH_SHORT).show();
                }
                else{
                   Absent.add(student_list.getItemAtPosition(i).toString());
                }
            }



            String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
            DocumentReference df= fStore.collection("Users").document(uid);
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                   String s_Subject=documentSnapshot.getString("faculty_subject");
                    String Date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    DocumentReference af=fStore.collection(s_Subject).document(Date);
                    Map<String,Object> StudentAttendance=new HashMap<>();
                    for (int i=0;i<Present.size();i++){
                        databaseReference.child(s_Subject).child(Date).child(Present.get(i)).setValue("p");
                        StudentAttendance.put(Present.get(i).toString(),"P");
                    }
                    for (int i=0;i<Absent.size();i++){
                        StudentAttendance.put(Absent.get(i).toString(),"A");
                        databaseReference.child(s_Subject).child(Date).child(Absent.get(i)).setValue("A");
                    }
                    af.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                Toast.makeText(T_Attendance.this, "You have already taken attendance", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                af.set(StudentAttendance);
                                Toast.makeText(T_Attendance.this, "data Saved", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: "+e.getMessage());
                }
            });

        }
        return super.onOptionsItemSelected(item);

    }



}

