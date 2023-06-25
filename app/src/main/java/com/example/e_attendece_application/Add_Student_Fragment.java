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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Add_Student_Fragment extends Fragment {
    EditText student_name,student_email,student_password,student_id;
    Button student_save,student_cancel;
    FirebaseAuth fAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseFirestore fStore;

    public Add_Student_Fragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add__student_, container, false);
        student_name=view.findViewById(R.id.Student_Name);
        student_email= view.findViewById(R.id.Student_Email);
        student_password=view.findViewById(R.id.Student_Password);
        student_id=view.findViewById(R.id.Student_Id);
        student_save=view.findViewById(R.id.Student_Save);
        student_cancel=view.findViewById(R.id.Student_Cancel);

        fAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://e-attendance-4bd5e-default-rtdb.firebaseio.com/");
        fStore=FirebaseFirestore.getInstance();

        student_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1=student_name.getText().toString();
                String s2=student_email.getText().toString();
                String s3= student_password.getText().toString();
                String s4=student_id.getText().toString();

                if (s1.isEmpty())
                {
                    student_name.setError("enter the name");
                }
                if (s2.isEmpty())
                {
                    student_email.setError("enter the email");
                }
                if (s3.isEmpty())
                {
                    student_password.setError("enter the password");
                }
                if (s4.isEmpty())
                {
                    student_id.setError("enter the id");
                }
//    java.lang.NullPointerException: Attempt to invoke virtual method 'com.google.android.gms.tasks.Task com.google.firebase.auth.
//               FirebaseAuth.createUserWithEmailAndPassword(java.lang.String, java.lang.String)' on a null object reference
//        at com.example.e_attendece_application.Add_Student_Fragment$1.onClick(Add_Student_Fragment.java:70)
                fAuth.createUserWithEmailAndPassword(s2,s3).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        databaseReference.child("User").child("Student").child(s1).child("student_email").setValue(s2);
                        databaseReference.child("User").child("Student").child(s1).child("student_id").setValue(s4);
                        databaseReference.child("User").child("Student").child(s1).child("student_name").setValue(s4);

                        FirebaseUser user=fAuth.getCurrentUser();

                        Toast.makeText(getContext(), "Student added", Toast.LENGTH_SHORT).show();
                        student_name.setText("");
                        student_email.setText("");
                        student_id.setText("");
                        student_password.setText("");

                    }
                });
            }
        });
        student_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "canceled", Toast.LENGTH_SHORT).show();
                student_name.setText("");
                student_email.setText("");
                student_id.setText("");
                student_password.setText("");
            }
        });

        return view;

    }
}