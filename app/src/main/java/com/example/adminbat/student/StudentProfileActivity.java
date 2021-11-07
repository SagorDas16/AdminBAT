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
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

public class StudentProfileActivity extends AppCompatActivity {
    String key,year;
    DatabaseReference databaseReference;
    String currentuser;
    StringBuilder st = new StringBuilder();
    TextView name,roll,mac;
    String name1,roll1,mac1;
    Button edit,delete;
    ArrayList<String> arrayList,Keylist,macarrayList,macaddList;
    EditText editText1,editText2,editText3;

    FloatingActionButton floatingActionButton;
    ListView listView;
    Toolbar toolbar;
    ImageButton searchbutton;
    BluetoothAdapter BTadapter = BluetoothAdapter.getDefaultAdapter();
    final static int REQUEST_COARSE_LOCATION = 1, REQUEST_ENABLE_BT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        key = getIntent().getStringExtra("key");
        year = getIntent().getStringExtra("Year");
        name = findViewById(R.id.nametext);
        roll = findViewById(R.id.rolltext);
        mac = findViewById(R.id.mactext);
        edit = findViewById(R.id.profileedit);
        delete = findViewById(R.id.profiledelete);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        String current = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        currentuser =  getemail(current);
        //Toast.makeText(StudentProfileActivity.this,key+year+currentuser,Toast.LENGTH_SHORT).show();



        databaseReference.child(currentuser).child("Years").child(year).child("Students").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                    StudentModel studentModel = dataSnapshot1.getValue(StudentModel.class);
                    name1 = studentModel.getName();
                    roll1 = studentModel.getRoll();
                    mac1 = studentModel.getMac();
                    // String string1 = dataSnapshot1.getKey();
                    Toast.makeText(StudentProfileActivity.this,key,Toast.LENGTH_SHORT).show();
                    //Keylist.add(string1);
                    name.setText(name1);
                    roll.setText(roll1);
                    mac.setText(mac1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });


        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
    }

    private void delete(){
        new AlertDialog.Builder(StudentProfileActivity.this)
                .setIcon(R.drawable.ic_baseline_delete_24)
                .setTitle("DELETE")
                .setMessage("Are you Sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(StudentProfileActivity.this,"deleted",Toast.LENGTH_SHORT).show();
                        databaseReference.child(currentuser).child("Years").child(year).child("Students").child(key).removeValue();
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(StudentProfileActivity.this,"not deleted",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).show();
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
        editText1.setText(name1);
        editText2.setText(roll1);
        editText3.setText(mac1);

        Button add = view.findViewById(R.id.add_btn);
        Button cancel = view.findViewById(R.id.cancel_btn);
        add.setText("Update");
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

            ArrayAdapter<String> lsadapter = new ArrayAdapter<String>(StudentProfileActivity.this, android.R.layout.simple_list_item_1, macarrayList1);
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
        if (ContextCompat.checkSelfPermission(StudentProfileActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StudentProfileActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
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
            databaseReference.child(currentuser).child("Years").child(year).child("Students").child(key).removeValue();
            key = Roll;
            databaseReference.child(currentuser).child("Years").child(year).child("Students").child(Roll).setValue(studentModel);
            Toast.makeText(StudentProfileActivity.this, currentuser, Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Toast.makeText(StudentProfileActivity.this, "Enter name and roll", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);

        super.onDestroy();
    }
}