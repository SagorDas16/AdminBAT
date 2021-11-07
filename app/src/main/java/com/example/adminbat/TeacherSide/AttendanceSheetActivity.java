package com.example.adminbat.TeacherSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.adminbat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AttendanceSheetActivity extends AppCompatActivity {
    ListView listView;
    DatabaseReference databaseReference;
    ArrayList<String> sheetlist = new ArrayList<>();
    ArrayList<String> rolllist = new ArrayList<>();
    ArrayList<String> statuslist = new ArrayList<>();
    String Key,date,pathtocourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_sheet);

        listView = findViewById(R.id.listviewattendencesheetID);
        date = getIntent().getStringExtra("Date");
        pathtocourse = getIntent().getStringExtra("CoursePath");
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(AttendanceSheetActivity.this, android.R.layout.simple_list_item_1,sheetlist);
        AttendanceSheetAdapter adapter = new AttendanceSheetAdapter(this,rolllist,statuslist);
        databaseReference = FirebaseDatabase.getInstance().getReference();

       // String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child(pathtocourse).child("Attendence").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sheetlist.clear();
                sheetlist.add("Roll"+"       "+"Attendence");
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    AttendanceModel attendanceModel = dataSnapshot1.getValue(AttendanceModel.class);
                    String string = attendanceModel.getRoll();
                    String string1 = attendanceModel.getStatus();
                    // String string1 =dataSnapshot1.getKey();
                    String list = string+"          "+string1;
                    sheetlist.add(list);
                    rolllist.add(string);
                    statuslist.add(string1);

                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}