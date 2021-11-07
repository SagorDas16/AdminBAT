package com.example.adminbat.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.adminbat.BatchActivity;
import com.example.adminbat.BatchOptionActivity;
import com.example.adminbat.R;
import com.example.adminbat.StudentModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddNewStudentActivity extends AppCompatActivity {
    String year;
    FloatingActionButton floatingActionButton;
    String Key,current,currentuser;
    DatabaseReference databaseReference;
    ListView listView;
    ArrayList<String> arrayList,Keylist,macarrayList,macaddList;
    EditText editText1,editText2,editText3;
    Toolbar toolbar;
    ImageButton searchbutton;
    BluetoothAdapter BTadapter = BluetoothAdapter.getDefaultAdapter();
    StringBuilder st = new StringBuilder();
    final static int REQUEST_COARSE_LOCATION = 1, REQUEST_ENABLE_BT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student);

        year = getIntent().getStringExtra("Year");
        Toast.makeText(AddNewStudentActivity.this,year,Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        Key = getIntent().getStringExtra("Key");
        floatingActionButton = findViewById(R.id.addstudentfloatingbuttonID);
        listView = findViewById(R.id.listviewstudentID);
        arrayList = new ArrayList<>();
        Keylist = new ArrayList<>();
        macarrayList = new ArrayList<>();
        macaddList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddNewStudentActivity.this, android.R.layout.simple_list_item_1,arrayList);

        current = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        currentuser =  getemail(current);






        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });




        databaseReference.child(currentuser).child("Years").child(year).child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                Keylist.clear();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    StudentModel studentModel = dataSnapshot1.getValue(StudentModel.class);
                    String name1 = studentModel.getName();
                    String roll1 = studentModel.getRoll();
                   // String string1 = dataSnapshot1.getKey();
                    String keyroll = dataSnapshot1.getKey();
                    String nameroll = name1 + " " + roll1;
                    arrayList.add(nameroll);
                    Keylist.add(keyroll);
                    //Keylist.add(string1);
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
                Intent intent = new Intent(AddNewStudentActivity.this, StudentProfileActivity.class);
                intent.putExtra("key", Keylist.get(position));
                intent.putExtra("Year", year);
                startActivity(intent);
            }
        });

       /* Handler handler = new Handler();
        int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if(!arrayList.isEmpty())//checking if the data is loaded or not
                {
                    Toast.makeText(AddStudentActivity.this, arrayList.size(),Toast.LENGTH_SHORT).show();
                }
                else
                    handler.postDelayed(this, delay);
            }
        }, delay);*/
        //setToolbar();
        //Bluetooth

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
        //Bluetooth
    }
    private String getemail(String current1) {
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
        View view = LayoutInflater.from(this).inflate(R.layout.add_student_dialog,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        editText1 = view.findViewById(R.id.name_add_ID);
        editText2 = view.findViewById(R.id.roll_add_ID);
        editText3 = view.findViewById(R.id.mac_address_ID);

        Button add = view.findViewById(R.id.add_btn);
        Button cancel = view.findViewById(R.id.cancel_btn);
        searchbutton = view.findViewById(R.id.mac_address_searchID);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!BTadapter.isEnabled()){
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(intent);
                }
                checkLocationPermission();
                macarrayList.clear();
                BTadapter.cancelDiscovery();
                BTadapter.startDiscovery();
            }
        });
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
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog

                // progressBar.setVisibility(View.GONE);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // progressBar1.setVisibility(View.GONE);
                //  progressBar1.setVisibility(View.GONE);
                macarrayList.add(device.getName() + "      " + device.getAddress());
                macaddList.add(device.getAddress());
            }
            showlistdialog(macarrayList, macaddList);
        }

    };
    private void showlistdialog(ArrayList<String> macarrayList1, ArrayList<String> macaddList1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.mac_list_dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        ListView listView1 = view.findViewById(R.id.mac_add_list_ID);
        //TextView textView = view.findViewById(R.id.list_dialog_tvID);


        if(macarrayList1.size()>0 && macaddList1.size()>0) {
            dialog.dismiss();

            ArrayAdapter<String> lsadapter = new ArrayAdapter<String>(AddNewStudentActivity.this, android.R.layout.simple_list_item_1, macarrayList1);
            listView1.setAdapter(lsadapter);
            dialog.show();

            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //  Toast.makeText(AddStudentActivity.this,macarrayList1.get(position),Toast.LENGTH_SHORT).show();
                    editText3.setText(macaddList1.get(position));

                    dialog.dismiss();

                }
            });
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(AddNewStudentActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddNewStudentActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_COARSE_LOCATION);
        }
    }



    private void savedata() {
        String Name = editText1.getText().toString().trim();
        String Roll = editText2.getText().toString().trim();
        String Mac = editText3.getText().toString().trim();
        if(!Name.isEmpty() && !Roll.isEmpty()) {

            StudentModel studentModel = new StudentModel(Name, Roll, Mac);
            // String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseReference.child(currentuser).child("Years").child(year).child("Students").child(Roll).setValue(studentModel);
            Toast.makeText(AddNewStudentActivity.this, currentuser, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(AddNewStudentActivity.this, "Enter name and roll", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);

        super.onDestroy();
    }
}