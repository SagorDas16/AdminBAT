package com.example.adminbat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.adminbat.Courses.AddSemesterActivity;
import com.example.adminbat.student.AddNewStudentActivity;

public class BatchOptionActivity extends AppCompatActivity {
    Button addstudentbtn,addsemesterbtn;
    String year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_option);

        year = getIntent().getStringExtra("Year");
        addsemesterbtn = findViewById(R.id.addSemesterID);
        addstudentbtn = findViewById(R.id.addstudentID);

        addstudentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BatchOptionActivity.this, AddNewStudentActivity.class);
                intent.putExtra("Year",year);
                startActivity(intent);
            }
        });
        addsemesterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BatchOptionActivity.this, AddSemesterActivity.class);
                intent.putExtra("Year",year);
                startActivity(intent);
            }
        });
    }
}