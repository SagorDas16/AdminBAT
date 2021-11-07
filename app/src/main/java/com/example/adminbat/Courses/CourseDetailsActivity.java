package com.example.adminbat.Courses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.adminbat.R;
import com.example.adminbat.TeacherSide.ViewAttendanceActivity;
import com.example.adminbat.teacher.TeacherModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseDetailsActivity extends AppCompatActivity {
    Spinner spinner;
    Button teachersetbtn,adminviewatn;
    FloatingActionButton floatingActionButton;
    String Key,current,currentuser,year,semester;
    DatabaseReference databaseReference;
    ListView listView;
    ArrayList<String> arrayList,Keylist,namelist,emaillist,designationlist;
    EditText editText1,editText2,editText3;
    Toolbar toolbar;
    ImageButton searchbutton;
    BluetoothAdapter BTadapter = BluetoothAdapter.getDefaultAdapter();
    StringBuilder st = new StringBuilder();
    final static int REQUEST_COARSE_LOCATION = 1, REQUEST_ENABLE_BT = 2;
    FirebaseAuth mAuth;
    String coursekey,pathtocourse,pathtostudent,coursename,coursecode;
    TextView headline,teachername,nametext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        semester = getIntent().getStringExtra("Semester");
        year = getIntent().getStringExtra("Year");
        coursekey = getIntent().getStringExtra("CourseKey");
        coursename = getIntent().getStringExtra("CourseName");
        coursecode = getIntent().getStringExtra("CourseCode");
        adminviewatn = findViewById(R.id.adminViewattendaneID);

      //  floatingActionButton = findViewById(R.id.addcoursefloatingbuttonID);
        listView = findViewById(R.id.listviewcourseID);
        spinner = findViewById(R.id.teacherspinnerID);
        teachersetbtn = findViewById(R.id.setspinnerID);
        headline = findViewById(R.id.headlineID);
        teachername = findViewById(R.id.teachernameID);
        nametext = findViewById(R.id.nametextID);
        arrayList = new ArrayList<>();
        namelist = new ArrayList<>();
        emaillist = new ArrayList<>();
        designationlist = new ArrayList<>();

        Keylist = new ArrayList<>();


        currentuser = getemail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        pathtocourse = currentuser+"/"+"Years"+"/"+year+"/"+"Semester"+"/"+semester+"/"+"Courses"+"/"+coursekey;
        pathtostudent = currentuser+"/"+"Years"+"/"+year+"/"+"Students";


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CourseDetailsActivity.this, android.R.layout.simple_list_item_1,namelist);

        databaseReference.child(currentuser).child("Teacher").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                namelist.clear();
                emaillist.clear();
                designationlist.clear();
                namelist.add("Select a Teacher");

                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    TeacherModel teacherModel = dataSnapshot1.getValue(TeacherModel.class);
                    String name1 = teacherModel.getName();
                    String email1 = teacherModel.getEmail();
                    String designation1 = teacherModel.getDesignation();
                  //  String tmp = title1 + "  " + code1;

                    // String string1 = dataSnapshot1.getKey();

                 //   arrayList.add(tmp);
                    //Keylist.add(string1);
                    namelist.add(name1);
                    emaillist.add(email1);
                    designationlist.add(designation1);
                }
                spinner.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child(currentuser).child("Years").child(year).child("Semester").child(semester).child("Courses").child(coursekey).child("Teacher").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    TeacherModel teacherModel = dataSnapshot1.getValue(TeacherModel.class);
                    String name1 = teacherModel.getName();

                    teachername.setText(name1);
                }
                String tmp1 = teachername.getText().toString();
                if(tmp1.equals("")){
                    headline.setVisibility(View.VISIBLE);
                    teachername.setVisibility(View.GONE);
                    spinner.setVisibility(View.VISIBLE);
                    teachersetbtn.setVisibility(View.VISIBLE);
                    nametext.setVisibility(View.GONE);
                    adminviewatn.setVisibility(View.GONE);
                }
                else {
                    headline.setVisibility(View.GONE);
                    teachername.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                    teachersetbtn.setVisibility(View.GONE);
                    nametext.setVisibility(View.VISIBLE);
                    adminviewatn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        teachersetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(CourseDetailsActivity.this,String.valueOf(emaillist.size()),Toast.LENGTH_SHORT).show();
                setTeacher();
            }
        });
        adminviewatn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailsActivity.this, ViewAttendanceActivity.class);
                intent.putExtra("StudentPath", pathtostudent);
                intent.putExtra("CoursePath", pathtocourse);
                startActivity(intent);
            }
        });





      /*  floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CourseDetailsActivity.this, CourseDetailsActivity.class);
                intent.putExtra("Year", year);
                intent.putExtra("Semester", semester);
                startActivity(intent);
            }
        });*/
    }
    private void setTeacher() {
        String value = spinner.getSelectedItem().toString();
        if(value.equals("Select a Teacher")){
            Toast.makeText(CourseDetailsActivity.this,"No teacher selected",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            int i;
            for(i=0;i<namelist.size();i++){
                String tmp = namelist.get(i);
                if(tmp.equals(value)){
                    break;
                }
            }
          //  Toast.makeText(CourseDetailsActivity.this,coursekey,Toast.LENGTH_SHORT).show();

           /* Toast.makeText(CourseDetailsActivity.this,namelist.get(i),Toast.LENGTH_SHORT).show();
            Toast.makeText(CourseDetailsActivity.this,emaillist.get(i-1),Toast.LENGTH_SHORT).show();
            Toast.makeText(CourseDetailsActivity.this,designationlist.get(i-1),Toast.LENGTH_SHORT).show();*/
            TeacherModel teacherModel = new TeacherModel(namelist.get(i),designationlist.get(i-1),emaillist.get(i-1));
            databaseReference.child(currentuser).child("Years").child(year).child("Semester").child(semester).child("Courses").child(coursekey).child("Teacher").push().setValue(teacherModel);
            //String yyy = Integer.toString(year);

            TeacherAssignModel teacherAssignModel = new TeacherAssignModel(year,coursename,coursecode,pathtocourse,pathtostudent);
            String teacheremail = getemail(emaillist.get(i-1));
            databaseReference.child(teacheremail).child("TCourse").push().setValue(teacherAssignModel);
        }
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
  /*  private void showdialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_course_dialog,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        editText1 = view.findViewById(R.id.coursetitleeditid);
        editText2 = view.findViewById(R.id.coursecodeeditid);

        Button add = view.findViewById(R.id.add_btn);
        Button cancel = view.findViewById(R.id.cancel_btn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata();
                dialog.dismiss();
            }
        });

    }
    private void savedata() {
        String title = editText1.getText().toString().trim();
        String code = editText2.getText().toString().trim();
        // String tmp = styear+"-"+stsemester;

        CourseModel courseModel = new CourseModel(title,code);
        databaseReference.child(currentuser).child("Years").child(year).child("Semester").child(semester).child("Courses").push().setValue(courseModel);
    }*/
}