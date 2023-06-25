package com.example.e_attendece_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.e_attendece_application.Admin_home_page;
import com.example.e_attendece_application.Faculty_Home_Page;
import com.example.e_attendece_application.R;
import com.example.e_attendece_application.Student_Home_Page;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login_for_all extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private CheckBox rememberMeCheckBox;
    private TextView forgotPasswordTextView;
    private ToggleButton toggleButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private boolean rememberMeChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_for_all);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        rememberMeCheckBox = findViewById(R.id.checkBoxRememberMe);
        forgotPasswordTextView = findViewById(R.id.textViewForgotPassword);
        toggleButton = findViewById(R.id.buttonTogglePassword);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        rememberMeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rememberMeChecked = isChecked;
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show password
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // Hide password
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                // Move cursor to the end of the password field
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if(!rememberMeChecked){
            rememberMeChecked=false;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            checkUserRole(user.getUid());
                        }
                    } else {
                        Toast.makeText(Login_for_all.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserRole(String uid) {
        DocumentReference userRef = mFirestore.collection("Users").document(uid);
        userRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            if (document.getString("isAdmin") != null) {
                                // User is admin
                                startActivity(new Intent(Login_for_all.this, Admin_home_page.class));
                                finish();
                            } else if (document.getString("isFaculty") != null) {
                                // User is faculty/teacher
                                startActivity(new Intent(Login_for_all.this, Faculty_Home_Page.class));
                                finish();
                            } else if (document.getString("isStudent") != null) {
                                // User is student
                                startActivity(new Intent(Login_for_all.this, Student_Home_Page.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(Login_for_all.this, "User data not found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login_for_all.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void forgotPassword() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login_for_all.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login_for_all.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (rememberMeChecked){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            checkUserRole(currentUser.getUid());
        }}
    }
}
