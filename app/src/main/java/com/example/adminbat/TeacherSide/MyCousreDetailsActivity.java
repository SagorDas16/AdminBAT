package com.example.adminbat.TeacherSide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminbat.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyCousreDetailsActivity extends AppCompatActivity {
    String pathtocourse,pathtostudent;
    private Button viewstudent, takeattendence, viewattendence;
    CardView cardView1,cardView2,cardView3;
    String Key,current,cremail;
    EditText creditText;
    TextView crtextView;
    StringBuilder st = new StringBuilder();
    Button crbutton;
    DatabaseReference databaseReference;
    String currentuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cousre_details);

        pathtocourse = getIntent().getStringExtra("CoursePath");
        pathtostudent = getIntent().getStringExtra("StudentPath");
        Toast.makeText(MyCousreDetailsActivity.this,pathtocourse+pathtostudent,Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        cardView1 = findViewById(R.id.cardview1);
        cardView2 = findViewById(R.id.cardview2);
        cardView3 = findViewById(R.id.cardview3);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCousreDetailsActivity.this, StudentActivity.class);
                intent.putExtra("StudentPath", pathtostudent);
                startActivity(intent);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCousreDetailsActivity.this, MyTakeAttendanceActivity.class);
                intent.putExtra("StudentPath", pathtostudent);
                intent.putExtra("CoursePath", pathtocourse);
                startActivity(intent);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MyCousreDetailsActivity.this,ViewAttendanceActivity.class);
                intent.putExtra("StudentPath", pathtostudent);
                intent.putExtra("CoursePath", pathtocourse);
                startActivity(intent);
            }
        });
    }
}