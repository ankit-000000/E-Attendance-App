package com.example.e_attendece_application;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Add_Teacher_Fragment extends Fragment {


    public Add_Teacher_Fragment() {
        // Required empty public constructor
    }
    EditText faculty_name,faculty_email,faculty_password,faculty_id,faculty_subject;
    Button faculty_save,faculty_cancel;
    FirebaseAuth fAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseFirestore fStore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add__teacher_, container, false);
        faculty_name=view.findViewById(R.id.Faculty_Name);
        faculty_email=view.findViewById(R.id.Faculty_Email);
        faculty_password=view.findViewById(R.id.Faculty_Password);
        faculty_id=view.findViewById(R.id.Faculty_Id);
        faculty_subject=view.findViewById(R.id.Faculty_Subject);

        faculty_save=view.findViewById(R.id.Faculty_Save);
        faculty_cancel=view.findViewById(R.id.Faculty_Cancel);

        fAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://e-attendance-4bd5e-default-rtdb.firebaseio.com/");
        fStore=FirebaseFirestore.getInstance();
        faculty_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1=faculty_name.getText().toString();
                String s2=faculty_email.getText().toString();
                String s3= faculty_password.getText().toString();
                String s4=faculty_id.getText().toString();
                String s5=faculty_subject.getText().toString();
                if (s1.isEmpty())
                {
                    faculty_name.setError("enter the name");
                }
                if (s2.isEmpty())
                {
                    faculty_email.setError("enter the email");
                }
                if (s3.isEmpty())
                {
                    faculty_password.setError("enter the password");
                }
                if (s4.isEmpty())
                {
                    faculty_id.setError("enter the id");
                }
                if (s5.isEmpty())
                {
                    faculty_subject.setError("enter the subject");
                }







                fAuth.createUserWithEmailAndPassword(s2,s3).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
//
                        databaseReference.child("User").child("Faculty").child(s4).child("faculty_email").setValue(s2);
                        databaseReference.child("User").child("Faculty").child(s4).child("faculty_name").setValue(s1);
                        databaseReference.child("User").child("Faculty").child(s4).child("faculty_subject").setValue(s5);
                        databaseReference.child("User").child("Faculty").child(s4).child("isFaculty").setValue("1");
                        Toast.makeText(getContext(), "saved", Toast.LENGTH_SHORT).show();

//                 storing data in fireStore
//                        FirebaseUser user=fAuth.getCurrentUser();
//
//                        DocumentReference df=fStore.collection("Users").document(user.getUid());
//                        Map<String,Object> userinfo =new HashMap<>();
//                        userinfo.put("faculty_name",s1);
//                        userinfo.put("faculty_email",s2);
//                        userinfo.put("faculty_subject",s5);
//                        userinfo.put("Id",s4);
//
//                        // specifying access level
//                        userinfo.put("isFaculty","2");
//
//                        df.set(userinfo);
//
//
//                        startActivity(new Intent(Faculty_Registration.this,Admin_home_page.class));
//                        finish();
//
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        Toast.makeText(Faculty_Registration.this, "Teacher is not Added", Toast.LENGTH_SHORT).show();

                    }
                });
//

            }
        });
        faculty_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "canceled", Toast.LENGTH_SHORT).show();
                faculty_name.setText("");
                faculty_email.setText("");
                faculty_id.setText("");
                faculty_password.setText("");
            }
        });
        return view;
    }

    }