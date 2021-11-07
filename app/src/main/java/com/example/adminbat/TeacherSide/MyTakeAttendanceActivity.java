package com.example.adminbat.TeacherSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminbat.R;
import com.example.adminbat.StudentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.adminbat.TeacherSide.MyCoursesActivity.REQUEST_COARSE_LOCATION;

public class MyTakeAttendanceActivity extends AppCompatActivity {
    String pathtocourse,pathtostudent;
    String Key, date,cremail;
    DatabaseReference databaseReference;
    ArrayList<String> namelist, Keylist, rolllist, statuslist,macaddlist,macarraylist;
    Button savebutton, changedate,scanbutton;
    EditText dateeditext;
    RecyclerView recyclerView;
    AtAdapter atAdapter;
    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    int day, month, year;
    Calendar calendar;
    TextView textView;
    String currentdate;
    BluetoothAdapter BTadapter = BluetoothAdapter.getDefaultAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_take_attendance);

        pathtocourse = getIntent().getStringExtra("CoursePath");
        pathtostudent = getIntent().getStringExtra("StudentPath");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        namelist = new ArrayList<>();
        rolllist = new ArrayList<>();
        Keylist = new ArrayList<>();
        statuslist = new ArrayList<>();
        macaddlist = new ArrayList<>();
        macarraylist = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerviewID);
        savebutton = findViewById(R.id.saveattendenceID);
        textView = findViewById(R.id.dateshowID);
        changedate = findViewById(R.id.changedatebuttonID);
        scanbutton = findViewById(R.id.scanmacID);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        month = month+1;
        currentdate = day + "-" + month + "-" + year;
        textView.setText(currentdate);


        changedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdatedialog();
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }


            private void save() {

                for (int i = 0; i < namelist.size(); i++) {
                    String name = namelist.get(i);
                    String roll = rolllist.get(i);
                    String status = statuslist.get(i);
                    date = textView.getText().toString();


                    AttendanceModel attendanceModel = new AttendanceModel(name, roll, status);
                    databaseReference.child(pathtocourse).child("Attendence").child(date).child(roll).setValue(attendanceModel);


                    Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();

                }
                Date dates = new Date(date);
                databaseReference.child(pathtocourse).child("Attendencedate").child(date).setValue(dates);
            }


        });

        scanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!BTadapter.isEnabled()){
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(intent);
                }
                macarraylist.clear();
                //     progressBar1.setVisibility(View.VISIBLE);
                checkLocationPermission();
                BTadapter.cancelDiscovery();
                BTadapter.startDiscovery();
            }
        });


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(TakeAttendenceActivity.this, android.R.layout.simple_list_item_1,namelist);
       // Toast.makeText(MyTakeAttendanceActivity.this,cremail,Toast.LENGTH_SHORT).show();


        databaseReference.child(pathtostudent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                namelist.clear();
                rolllist.clear();
                statuslist.clear();
                macaddlist.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    StudentModel studentModel = dataSnapshot1.getValue(StudentModel.class);
                    String name1 = studentModel.getName();
                    String roll1 = studentModel.getRoll();
                    String mac1 = studentModel.getMac();
                    String string1 = dataSnapshot1.getKey();
                    String nameroll = name1 + " " + roll1;
                    namelist.add(name1);
                    rolllist.add(roll1);
                    Keylist.add(string1);
                    statuslist.add("A");
                    macaddlist.add(mac1);
                }
                // listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        atAdapter = new AtAdapter(this, namelist, rolllist, statuslist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(atAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        atAdapter.setOnItemClickListener(new AtAdapter.ClickListener() {
            @Override
            public void OnItemClick(int Position, View view) {
                //  String status = atAdapter.statuslist.get(Position);
                if (statuslist.get(Position) == "A") {
                    statuslist.set(Position, "P");
                    atAdapter.notifyItemChanged(Position);
                } else if (statuslist.get(Position) == "P") {
                    statuslist.set(Position, "A");
                    atAdapter.notifyItemChanged(Position);
                }


                Toast.makeText(MyTakeAttendanceActivity.this, "Click " + Position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnItemLongClick(int Position, View view) {

                Toast.makeText(MyTakeAttendanceActivity.this, "Long Click " + Position, Toast.LENGTH_SHORT).show();
            }
        });

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);

    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog

                //progressBar.setVisibility(View.GONE);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //progressBar1.setVisibility(View.GONE);
                //progressBar1.setVisibility(View.GONE);
                macarraylist.add(device.getAddress());
            }
            checklist(macarraylist);
            //  Toast.makeText(TakeAttendenceActivity.this,String.valueOf(macarraylist.size()),Toast.LENGTH_SHORT).show();
        }


    };
    private void checklist(ArrayList<String> macarraylist1) {
        // Toast.makeText(TakeAttendenceActivity.this,String.valueOf(macarraylist1.size()),Toast.LENGTH_SHORT).show();
        if(macarraylist1.size()>0){
            for(int i=0;i<macarraylist1.size();i++){
                String mst = macarraylist1.get(i);
                for(int j=0;j<macaddlist.size();j++){
                    String mmst = macaddlist.get(j);
                    if(mst.equals(mmst)){
                        statuslist.set(j, "P");
                        atAdapter.notifyItemChanged(j);
                    }
                }
            }
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MyTakeAttendanceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MyTakeAttendanceActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_COARSE_LOCATION);
        }
    }

    private void showdatedialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MyTakeAttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                currentdate = day + "-" + month + "-" + year;
                textView.setText(currentdate);
            }
        }, year, month, day);
        datePickerDialog.show();

    }
    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);

        super.onDestroy();
    }

}