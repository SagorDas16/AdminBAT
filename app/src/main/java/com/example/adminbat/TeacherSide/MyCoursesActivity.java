package com.example.adminbat.TeacherSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.adminbat.AfterloginActivity;
import com.example.adminbat.Courses.AddCoursesActivity;
import com.example.adminbat.Courses.CourseDetailsActivity;
import com.example.adminbat.Courses.CourseModel;
import com.example.adminbat.Courses.TeacherAssignModel;
import com.example.adminbat.MainActivity;
import com.example.adminbat.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCoursesActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    String Key,current,currentuser,year,semester;
    DatabaseReference databaseReference;
    ListView listView;
    ArrayList<String> arrayList,keylist,coursenamelist,coursecodelist,pathtocourselist,pathtostudentlist,yearlist;
    EditText editText1,editText2,editText3;
    Toolbar toolbar;
    ImageButton searchbutton;
    BluetoothAdapter BTadapter = BluetoothAdapter.getDefaultAdapter();
    StringBuilder st = new StringBuilder();
    final static int REQUEST_COARSE_LOCATION = 1, REQUEST_ENABLE_BT = 2;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        listView = findViewById(R.id.listviewteachercourseID);
        arrayList = new ArrayList<>();
        keylist = new ArrayList<>();
        coursenamelist = new ArrayList<>();
        coursecodelist = new ArrayList<>();
        pathtocourselist = new ArrayList<>();
        pathtostudentlist = new ArrayList<>();
        yearlist = new ArrayList<>();


        currentuser = getemail(FirebaseAuth.getInstance().getCurrentUser().getEmail());


     //   ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyCoursesActivity.this, android.R.layout.simple_list_item_1,arrayList);
        SubjectAdapter adapter = new SubjectAdapter(this,coursenamelist,coursecodelist,yearlist);

        databaseReference.child(currentuser).child("TCourse").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                keylist.clear();
                coursecodelist.clear();
                coursenamelist.clear();
                pathtocourselist.clear();
                pathtostudentlist.clear();
                yearlist.clear();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    TeacherAssignModel teacherAssignModel = dataSnapshot1.getValue(TeacherAssignModel.class);
                    String title1 = teacherAssignModel.getTitle();
                    String code1 = teacherAssignModel.getCode();
                    String pathtocourse1 = teacherAssignModel.getPathtocourse();
                    String pathtostudent1 = teacherAssignModel.getPathtostudent();
                    String year1 = teacherAssignModel.getYear();
                    String tmp = title1 + "  " + code1;

                    String key1 = dataSnapshot1.getKey();
                    coursenamelist.add(title1);
                    coursecodelist.add(code1);
                    pathtocourselist.add(pathtocourse1);
                    pathtostudentlist.add(pathtostudent1);
                    yearlist.add(year1);

                    arrayList.add(tmp);
                    keylist.add(key1);
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
                Intent intent = new Intent(MyCoursesActivity.this, MyCousreDetailsActivity.class);
                intent.putExtra("CoursePath",pathtocourselist.get(position));
                intent.putExtra("StudentPath",pathtostudentlist.get(position));
                startActivity(intent);
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

    //setting doted menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/developer?id=Mahfuzur+Rahman");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share play store link via"));

            return true;
        }
        if (id == R.id.rate) {
            String appPackageName = getPackageName();
            try {
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=")));
            } catch (android.content.ActivityNotFoundException anfe) {
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/apps/details?id=" + appPackageName)));
            }
            return true;
        }

        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.logout) {
            //progressDialog.setMessage("logging Out ...");
            //progressDialog.show();
            FirebaseAuth.getInstance().signOut();
            //progressDialog.dismiss();
            Toast.makeText(MyCoursesActivity.this, "Logged out successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MyCoursesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}