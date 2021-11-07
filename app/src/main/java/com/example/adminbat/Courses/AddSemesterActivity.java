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
import android.widget.Toolbar;

import com.example.adminbat.BatchActivity;
import com.example.adminbat.BatchOptionActivity;
import com.example.adminbat.R;
import com.example.adminbat.YearModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddSemesterActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    String Key,current,currentuser,year;
    DatabaseReference databaseReference;
    ListView listView;
    ArrayList<String> arrayList,Keylist,macarrayList,macaddList;
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
        setContentView(R.layout.activity_add_semester);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        year = getIntent().getStringExtra("Year");

        floatingActionButton = findViewById(R.id.addsemesterfloatingbuttonID);
        listView = findViewById(R.id.listviewsemesterID);
        arrayList = new ArrayList<>();
        Keylist = new ArrayList<>();


        currentuser = getemail(FirebaseAuth.getInstance().getCurrentUser().getEmail());


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddSemesterActivity.this, android.R.layout.simple_list_item_1,arrayList);

        databaseReference.child(currentuser).child("Years").child(year).child("Semester").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    SemesterModel semesterModel = dataSnapshot1.getValue(SemesterModel.class);
                    String semester1 = semesterModel.getSemester();

                    // String string1 = dataSnapshot1.getKey();

                    arrayList.add(semester1);
                    //Keylist.add(string1);
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AddSemesterActivity.this, AddCoursesActivity.class);
                intent.putExtra("Year", year);
                intent.putExtra("Semester", arrayList.get(position));
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
    private void showdialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_semester_dialog,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        editText1 = view.findViewById(R.id.yeareditid);
        editText2 = view.findViewById(R.id.semestereditid);

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
        String styear = editText1.getText().toString().trim();
        String stsemester = editText2.getText().toString().trim();
        String tmp = styear+"-"+stsemester;

        SemesterModel semesterModel = new SemesterModel(tmp);
        databaseReference.child(currentuser).child("Years").child(year).child("Semester").child(tmp).setValue(semesterModel);
    }
}