package com.example.e_attendece_application;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Student_Home_Page extends AppCompatActivity {
    Button reset_pass,attendance;
    TextView auth,logout,name,id;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_home_page);

        attendance=findViewById(R.id.Student_Show_Attendance);
        logout=findViewById(R.id.tv_logout);
        auth=findViewById(R.id.Authentication);
        name=findViewById(R.id.name_student_profile);
        id=findViewById(R.id.id_student_profile);

        reset_pass=findViewById(R.id.Student_reset_password);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();


        checkIfAdmin(uid);
        userinfo(uid);
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),S_View_Today_Attendance.class));
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Login_for_all.class));
            finish();
            }
        });
        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertdialog_builder = new AlertDialog.Builder(v.getContext());
                EditText ResetPassword=new EditText(v.getContext());
                alertdialog_builder.setTitle("Reset Password ?");
                alertdialog_builder.setMessage("Enter new password");
                alertdialog_builder.setView(ResetPassword);
                alertdialog_builder.setPositiveButton("reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPassword=ResetPassword.getText().toString();
                        fAuth.getCurrentUser().updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Student_Home_Page.this, "Password Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: "+e.getMessage());
                            }
                        });
                    }
                });
                alertdialog_builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        alertdialog_builder.create().dismiss();
                    }
                });
                alertdialog_builder.create().show();

            }
        });
    }

    private void userinfo(String uid) {
        fStore.collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

              String s1=  documentSnapshot.getString("student_name");
              String s2=  documentSnapshot.getString("student_id");

              name.setText(s1);
              id.setText(s2);

            }
        });
    }

    private void checkIfAdmin(String uid) {
        DocumentReference df=fStore.collection("Users").document(uid);
        //extracting data
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, "onSuccess: "+documentSnapshot.getData());

                if(documentSnapshot.getString("isAdmin") != null){
                    auth.setText("Admin");

                }
                if (documentSnapshot.getString("isFaculty") != null){
                    auth.setText("Teacher");


                }
                if (documentSnapshot.getString("isStudent") != null){

                    auth.setText("Student");

                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Login_for_all.class));
        finish();
    }
}