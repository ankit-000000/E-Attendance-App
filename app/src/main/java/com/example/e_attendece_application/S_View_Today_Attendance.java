package com.example.e_attendece_application;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class S_View_Today_Attendance extends AppCompatActivity {


    ListView listView, listview2;
    FirebaseAuth fAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayList<String> attendance_list = new ArrayList<>();

    ArrayList<String> SubjectList = new ArrayList<>();

    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sview_today_attendance);
        fAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://e-attendance-4bd5e-default-rtdb.firebaseio.com/");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, SubjectList);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, attendance_list);
        fStore = FirebaseFirestore.getInstance();
        listView = findViewById(R.id.list_view);
        listview2 = findViewById(R.id.list_view2);

        fStore.collection("Users").whereEqualTo("isFaculty", "2").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                Toast.makeText(S_View_Today_Attendance.this, "you", Toast.LENGTH_SHORT).show();
                SubjectList.clear();
                attendance_list.clear();
                for (QueryDocumentSnapshot snapshot : value) {
                    String subject = snapshot.getString("faculty_subject");
                    SubjectList.add(subject);
                    adapter.notifyDataSetChanged();
                    String Date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    fStore.collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String name = documentSnapshot.getString("student_name");

                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String s=snapshot.child(subject).child(Date).child(name).getValue(String.class);
                                    if(s!=null){
                                        attendance_list.add(s);
                                        adapter2.notifyDataSetChanged();
                                    }
                                    else{
                                        attendance_list.add("not marked yet");
                                        adapter2.notifyDataSetChanged();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                    Log.d(TAG, "onCancelled: "+error.getMessage());
                                }
                            });
                        }

                        ;
                    });
                }

            }
        });
        Collections.sort(SubjectList);
        listView.setAdapter(adapter);
        listview2.setAdapter(adapter2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Student_Home_Page.class));
        finish();
    }
}

















//    private void setRecyclerView() {
//        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new AttendanceAdapter(this,getList());
//        adapter.notifyDataSetChanged();
//        recyclerView.setAdapter(adapter);
//    }
//    private List<Attendace_toady> getList(){
//
//        Toast.makeText(this, "me", Toast.LENGTH_SHORT).show();
//        fStore=FirebaseFirestore.getInstance();
//        List<Attendace_toady> Attendance_today=new ArrayList<>();
//        //retrive data from firestore and add in list attendace today then we will work for more
//        fStore.collection("Users").whereEqualTo("isFaculty","2").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                Toast.makeText(S_View_Today_Attendance.this, "you", Toast.LENGTH_SHORT).show();
//                SubjectList.clear();
//                for (QueryDocumentSnapshot snapshot : value){
//                    SubjectList.add(snapshot.getString("faculty_subject"));
//                }
//            }
//        });
//        String Date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//
//        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
//        fStore.collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                String name=documentSnapshot.getString("student_name");
//
//                for (int i=0;i<SubjectList.size();i++){
//                    int finalI = i;
//                    databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                        @Override
//                        public void onSuccess(DataSnapshot dataSnapshot) {
//                            String a=dataSnapshot.child(SubjectList.get(finalI)).child(name).getValue(String.class);
//                            Attendance_today.add(new Attendace_toady(SubjectList.get(finalI),a));
//                        }
//                    });
//                }
//
//                Toast.makeText(S_View_Today_Attendance.this, "he", Toast.LENGTH_SHORT).show();
////                int i=0;
////                for (i=0;i<SubjectList.size();i++){
////
////
////
////                    int f=i;
////
////                    fStore.collection(SubjectList.get(i).toString()).document(Date).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
////                        @Override
////                        public void onSuccess(DocumentSnapshot documentSnapshot) {
////                            Toast.makeText(S_View_Today_Attendance.this, "they", Toast.LENGTH_SHORT).show();
////                            String Today_attendance=documentSnapshot.getString(name);
////                            Attendance_today.add(new Attendace_toady(SubjectList.get(f),Today_attendance));
////                        }
////                    });
////                }
//
//            }
//        });
//
//
//
//        return Attendance_today;
//    }
