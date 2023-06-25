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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Faculty_Home_Page extends AppCompatActivity {
    Button fAttendance,Faculty_ResetPassword,view_attendance;
    TextView f_Name,f_Subject,auth,logout,id;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    AlertDialog.Builder alertdialog_builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home_page);
        auth=findViewById(R.id.Authentication);

        id=findViewById(R.id.id_faculty_profile);
        f_Name=findViewById(R.id.name_faculty_profile);
        f_Subject=findViewById(R.id.subject_faculty_profile);
        fAttendance=findViewById(R.id.Attendance);
        logout=findViewById(R.id.tv_logout);
        Faculty_ResetPassword=findViewById(R.id.btn_Faculty_ResetPassword);
        view_attendance=findViewById(R.id.F_view_Attendance);


        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

      String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();


        DocumentReference df= fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String s_Name=documentSnapshot.getString("faculty_name");
                String s_Subject=documentSnapshot.getString("faculty_subject");
                String fid= documentSnapshot.getString("Id");
                checkIfAdmin(uid);

                id.setText(fid);
                f_Name.setText(s_Name);
                f_Subject.setText(s_Subject);



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        fAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),T_Attendance.class));
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                 startActivity(new Intent(getApplicationContext(),Login_for_all.class));
                 finish();
            }
        });
        Faculty_ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertdialog_builder=new AlertDialog.Builder(v.getContext());
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
                               Toast.makeText(Faculty_Home_Page.this, "Password Updated", Toast.LENGTH_SHORT).show();
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
        view_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Faculty_Home_Page.this,F_View_Today_Attendance.class));
                finish();
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