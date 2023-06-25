package com.example.e_attendece_application;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_attendece_application.Adapter.ViewPagerRegistrationAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Admin_home_page extends AppCompatActivity {

    Button addFaculty,addStudent;
    TextView tv_reset_Password,tv_Logout;
    FirebaseAuth fAuth;

    AlertDialog.Builder alertdialog_builder;
    TabLayout tabLayout;
    ViewPager viewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);
        fAuth=FirebaseAuth.getInstance();
        tv_reset_Password=findViewById(R.id.tv_Admin_ResetPassword);
        tv_Logout=findViewById(R.id.tv_Admin_Logout);
        tabLayout=findViewById(R.id.tab_admin_layout);
        viewPager=findViewById(R.id.viewPagerAdmin);

        ViewPagerRegistrationAdapter adapter=new ViewPagerRegistrationAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tv_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login_for_all.class));
                finish();
            }
        });

        tv_reset_Password.setOnClickListener(new View.OnClickListener() {
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
                                Toast.makeText(Admin_home_page.this, "password updated", Toast.LENGTH_SHORT).show();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_resetpassword,menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id==R.id.Logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Admin_home_page.this,Login_for_all.class));
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Admin_home_page.this,Login_for_all.class));
        finish();
    }

}