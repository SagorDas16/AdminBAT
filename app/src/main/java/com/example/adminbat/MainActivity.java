package com.example.adminbat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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

import com.example.adminbat.TeacherSide.MyCoursesActivity;
import com.example.adminbat.teacher.TeacherModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email, password;
    private TextView textView;
    //private Button button;
    private TextView button;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    StringBuilder st = new StringBuilder();


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            loggedActivitystart();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("sign in activity");

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        email = findViewById(R.id.signinemailid);
        password = findViewById(R.id.signinpasswordid);
        button = findViewById(R.id.signinbuttonid);
        textView = findViewById(R.id.signuptextviewid);
        progressBar = findViewById(R.id.progressbarid);

        button.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.signinbuttonid:
                userlogin();
                break;

            case R.id.signuptextviewid:
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
                break;

        }

    }

    private void userlogin() {
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

        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                   /* finish();
                    Intent intent = new Intent(getApplicationContext(),AfterloginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                    Activtystart();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Login unsuccessful", Toast.LENGTH_SHORT).show();
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
    private void Activtystart() {

        databaseReference.child("user-roll").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    Userrollmodel userrollmodel = dataSnapshot1.getValue(Userrollmodel.class);
                    String userroll = userrollmodel.getUserroll();

                    String key = dataSnapshot1.getKey();
                    String Email1 = email.getText().toString().trim();

                    if(key.equals(getemail(Email1))){
                        if(userroll.equals("Teacher")){
                            Toast.makeText(MainActivity.this,"Teacher Activity started",Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(getApplicationContext(), MyCoursesActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else if(userroll.equals("Admin")){
                            Toast.makeText(MainActivity.this,"Admin activity started",Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(getApplicationContext(),AfterloginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void loggedActivitystart(){
        databaseReference.child("user-roll").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    Userrollmodel userrollmodel = dataSnapshot1.getValue(Userrollmodel.class);
                    String userroll = userrollmodel.getUserroll();

                    String key = dataSnapshot1.getKey();
                    String Email1 = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                    if(key.equals(getemail(Email1))){
                        if(userroll.equals("Teacher")){
                            Toast.makeText(MainActivity.this,"Teacher Activity started",Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(getApplicationContext(), MyCoursesActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else if(userroll.equals("Admin")){
                            Toast.makeText(MainActivity.this,"Admin activity started",Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(getApplicationContext(),AfterloginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}