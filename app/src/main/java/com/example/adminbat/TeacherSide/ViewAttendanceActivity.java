package com.example.adminbat.TeacherSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adminbat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAttendanceActivity extends AppCompatActivity {
    String pathtocourse,pathtostudent;
    ListView listView;
    DatabaseReference databaseReference;
    ArrayList<String> datelist = new ArrayList<>();
    String Key;
    Button overallattendence;
    TextView textView;
    TextView noresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        pathtocourse = getIntent().getStringExtra("CoursePath");
        pathtostudent = getIntent().getStringExtra("StudentPath");
        noresult = findViewById(R.id.noresult);

        listView = findViewById(R.id.listviewattendenceID);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewAttendanceActivity.this, android.R.layout.simple_list_item_1,datelist);
        ViewAttendanceAdapter adapter = new ViewAttendanceAdapter(this,datelist);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        overallattendence = findViewById(R.id.overallattendenceID);
        overallattendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAttendanceActivity.this,OverallAttendanceActivity.class);
                intent.putExtra("CoursePath",pathtocourse);
                startActivity(intent);


            }
        });



        databaseReference.child(pathtocourse).child("Attendencedate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datelist.clear();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    Date dates = dataSnapshot1.getValue(Date.class);
                    String string = dates.getDate();
                    //String string1 =dataSnapshot1.getKey();
                    datelist.add(string);

                }
                if(datelist.isEmpty()){
                    noresult.setText("No Data Here");
                    noresult.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    overallattendence.setVisibility(View.GONE);
                }
                    listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ViewAttendanceActivity.this, AttendanceSheetActivity.class);
                intent.putExtra("Date", datelist.get(position));
                intent.putExtra("CoursePath", pathtocourse);
                startActivity(intent);
            }
        });




    }
}