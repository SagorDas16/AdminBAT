package com.example.adminbat.TeacherSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
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

public class OverallAttendanceActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    ArrayList<String> datelist = new ArrayList<>();
    ArrayList<String> rolllist = new ArrayList<>();
    ArrayList<String> uprolllist = new ArrayList<>();
    ArrayList<String> attendancelist = new ArrayList<>();
    ArrayList<String> percentagelist = new ArrayList<>();
    String Key,pathtocourse;
    int arr[] = new int[1000];
    int i=0, j;
    ListView listView;
    long count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_attendance);

        pathtocourse = getIntent().getStringExtra("CoursePath");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        listView = findViewById(R.id.listviewoverallattendenceID);


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(OverallAttendanceActivity.this, android.R.layout.simple_list_item_1, datelist);
        OverallAttendanceAdapter adapter = new OverallAttendanceAdapter(this,uprolllist,attendancelist,percentagelist);


      //  String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(pathtocourse).child("Attendence").addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    if(i==0){
                        count = snapshot.getChildrenCount();
                    }
                    j=0;
                    for (DataSnapshot rollSnap:dataSnapshot1.getChildren()) {
                        AttendanceModel model=rollSnap.getValue(AttendanceModel.class);
                        // textView.setText(model.getRoll()+"\n"+model.getStatus());
                        //textView.setText(String.valueOf(i));
                        if(i==0) {
                            rolllist.add(model.getRoll());
                        }
                        String str = model.getStatus();
                        if(str.equals("P")){
                            arr[j] = arr[j]+1;
                        }
                        j++;

                    }

                    i++;

                    // Toast.makeText(OverallAttendenceActivity.this, "ssssssssss",Toast.LENGTH_SHORT).show();
                }
                String str4 = "   roll"+"   "+"  Attendence"+"       "+"Percentage";
                datelist.add(str4);
                int x=0;
                for(int k=0;k<rolllist.size();k++){
                    x = (int)count;
                    int percent = (int)((arr[k]/(double)x) * 100 );
                    String st = rolllist.get(k);
                    String st1 = String.valueOf(arr[k]);
                    String st2 = String.valueOf(count);
                    String st3 = String.valueOf(percent);
                    //  String st3 = new DecimalFormat("##.##").format(percent);
                    str4 = st+"      "+st1+"/"+st2+"                     "+st3+"%";
                    datelist.add(str4);
                    uprolllist.add(st);
                    attendancelist.add(st1+"/"+st2);
                    percentagelist.add(st3+"%");
                }
                //  int val = rolllist.size();
                //  textView.setText(String.valueOf(x));
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }
}
