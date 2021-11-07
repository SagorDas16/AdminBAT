package com.example.adminbat.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.adminbat.R;
import com.example.adminbat.StudentModel;
import com.example.adminbat.Userrollmodel;
import com.example.adminbat.student.StudentProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherProfileActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    String Key,currentuser;
    DatabaseReference databaseReference;
    ListView listView;
    ArrayList<String> arrayList,Keylist,macarrayList,macaddList,namelist,designationlist;
    EditText editText1,editText2,editText3;
    Toolbar toolbar;
    ImageButton searchbutton;
    BluetoothAdapter BTadapter = BluetoothAdapter.getDefaultAdapter();
    StringBuilder st = new StringBuilder();
    final static int REQUEST_COARSE_LOCATION = 1, REQUEST_ENABLE_BT = 2;
    FirebaseAuth mAuth;
    TextView name,desig,email;
    Button edit,delete;
    String name1, desig1, email1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        Key = getIntent().getStringExtra("key");
        name = findViewById(R.id.teachernametext);
        desig = findViewById(R.id.teacherdesignationtext);
        email = findViewById(R.id.teacheremailtext);
        edit = findViewById(R.id.teacherprofileedit);
        delete = findViewById(R.id.teacherprofiledelete);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        String current = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        currentuser =  getemail(current);


        databaseReference.child(currentuser).child("Teacher").child(Key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                TeacherModel teacherModel = dataSnapshot1.getValue(TeacherModel.class);
                name1 = teacherModel.getName();
                desig1 = teacherModel.getDesignation();
                email1 = teacherModel.getEmail();
                // String string1 = dataSnapshot1.getKey();
                Toast.makeText(TeacherProfileActivity.this,Key,Toast.LENGTH_SHORT).show();
                //Keylist.add(string1);
                name.setText(name1);
                desig.setText(desig1);
                email.setText(email1);

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
                //delete();
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
        View view = LayoutInflater.from(this).inflate(R.layout.add_teacher_dialog,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        editText1 = view.findViewById(R.id.teacheredittextnameid);
        editText2 = view.findViewById(R.id.teacheredittextdesignationid);
        editText3 = view.findViewById(R.id.teacheredittextemailid);
        editText3.setEnabled(false);


        editText1.setText(name1);
        editText2.setText(desig1);
        editText3.setText(email1);

        Button add = view.findViewById(R.id.add_btn);
        Button cancel = view.findViewById(R.id.cancel_btn);

        /*editText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TeacherProfileActivity.this, "Email Cannot be changed",Toast.LENGTH_SHORT).show();
            }
        });*/

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
        String Name = editText1.getText().toString().trim();
        String Designation = editText2.getText().toString().trim();
        String email = editText3.getText().toString().trim();
        if(email.isEmpty()){
            editText3.setError("Enter an emailaddress");
            editText3.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editText3.setError("Enter a valid email");
            editText3.requestFocus();
            return;
        }
        if(Designation.isEmpty()){
            editText2.setError("Enter designation");
            editText2.requestFocus();
            return;
        }
        if(Name.isEmpty()){
            editText1.setError("Enter name");
            editText1.requestFocus();
            return;
        }
        String password = "123456";
        Toast.makeText(TeacherProfileActivity.this,email+password,Toast.LENGTH_SHORT).show();

        databaseReference.child(currentuser).child("Teacher").child(Key).removeValue();
        TeacherModel teacherModel = new TeacherModel(Name,Designation,email);
        databaseReference.child(currentuser).child("Teacher").child(Key).setValue(teacherModel);

    }
}