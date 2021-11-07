package com.example.adminbat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email, password;
    private TextView textView;
    private TextView button;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    DatabaseReference databaseReference;
    StringBuilder st = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.setTitle("Sign up as Admin");
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.signupedittextemailid);
        password = findViewById(R.id.signupedittextpasswordid);
        button = findViewById(R.id.signupbuttonid);
        textView = findViewById(R.id.signintextviewid);
        progressBar = findViewById(R.id.progressbarid);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        button.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupbuttonid:
                userregister();
                break;

            case R.id.signintextviewid:
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void userregister() {
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();

        if(Email.isEmpty()){
            email.setError("Enter an emailaddress");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }

        if(Password.isEmpty()){
            password.setError("Enter a password");
            password.requestFocus();
            return;
        }
        if(Password.length()<6){
            password.setError("Password should at least 6");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Register is successful", Toast.LENGTH_LONG).show();
                    Userrollmodel userrollmodel = new Userrollmodel("Admin");
                    String user = getemail(email.getText().toString().trim());
                    databaseReference.child("user-roll").child(user).setValue(userrollmodel);
                    Intent intent = new Intent(SignupActivity.this, AfterloginActivity.class);
                    startActivity(intent);



                }
                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(getApplicationContext(), "User is already registered",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
    private String getemail(String current1) {
        st = new StringBuilder();
        st.append(current1);
        for(int i = 0;i<st.length();i++){
            char ch = st.charAt(i);
            if(ch == '.'){
                st.setCharAt(i, ',');
            }
        }
        return (st.toString());
    }
}