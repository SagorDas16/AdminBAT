package com.example.adminbat.TeacherSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.adminbat.R;
import com.example.adminbat.StudentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {
    String pathtostudent;
    ArrayList<String> arrayList,Keylist,macarrayList,macaddList,namelist,rolllist;
    DatabaseReference databaseReference;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        pathtostudent = getIntent().getStringExtra("StudentPath");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        listView = findViewById(R.id.listviewstudentshowID);

        arrayList = new ArrayList<>();
        Keylist = new ArrayList<>();
        macarrayList = new ArrayList<>();
        macaddList = new ArrayList<>();
        namelist = new ArrayList<>();
        rolllist = new ArrayList<>();
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(StudentActivity.this, android.R.layout.simple_list_item_1,arrayList);
        StudentAdapter adapter = new StudentAdapter(this,namelist,rolllist);

        databaseReference.child(pathtostudent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    StudentModel studentModel = dataSnapshot1.getValue(StudentModel.class);
                    String name1 = studentModel.getName();
                    String roll1 = studentModel.getRoll();
                    String mac1 = studentModel.getMac();
                    String string1 = dataSnapshot1.getKey();
                    String nameroll = name1 + " " + roll1;
                    arrayList.add(nameroll);
                    Keylist.add(string1);
                    namelist.add(name1);
                    rolllist.add(roll1);
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}