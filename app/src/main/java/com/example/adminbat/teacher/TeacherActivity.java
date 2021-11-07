package com.example.adminbat.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

import com.example.adminbat.AfterloginActivity;
import com.example.adminbat.R;
import com.example.adminbat.SignupActivity;
import com.example.adminbat.Userrollmodel;
import com.example.adminbat.student.AddNewStudentActivity;
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

public class TeacherActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    String Key,current,currentuser;
    DatabaseReference databaseReference;
    ListView listView;
    ArrayList<String> arrayList,Keylist,macarrayList,macaddList,namelist,designationlist,yearlist;
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
        setContentView(R.layout.activity_teacher);


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        floatingActionButton = findViewById(R.id.addteacherfloatingbuttonID);
        listView = findViewById(R.id.listviewteacherID);
        arrayList = new ArrayList<>();
        Keylist = new ArrayList<>();

        macarrayList = new ArrayList<>();
        macaddList = new ArrayList<>();
        namelist = new ArrayList<>();
        designationlist = new ArrayList<>();
        yearlist = new ArrayList<>();
        currentuser = getemail(FirebaseAuth.getInstance().getCurrentUser().getEmail());



        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeacherActivity.this, android.R.layout.simple_list_item_1,arrayList);
        TeacherAdapter adapter = new TeacherAdapter(this,namelist,designationlist);

        databaseReference.child(currentuser).child("Teacher").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                Keylist.clear();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    TeacherModel teacherModel = dataSnapshot1.getValue(TeacherModel.class);
                    String name1 = teacherModel.getName();
                    String designation1 = teacherModel.getDesignation();
                    String email1 = teacherModel.getEmail();
                    String key1 = dataSnapshot1.getKey();


                    arrayList.add(name1);
                    Keylist.add(key1);
                    namelist.add(name1);
                    designationlist.add(designation1);
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
                Intent intent = new Intent(TeacherActivity.this, TeacherProfileActivity.class);
                intent.putExtra("key", Keylist.get(position));
                startActivity(intent);
            }
        });





        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
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
        Toast.makeText(TeacherActivity.this,email+password,Toast.LENGTH_SHORT).show();



        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              //  progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Register is successful", Toast.LENGTH_LONG).show();
                    String current = getemail(email);
                    Userrollmodel userrollmodel = new Userrollmodel("Teacher");
                    databaseReference.child("user-roll").child(current).setValue(userrollmodel);

                    TeacherModel teacherModel = new TeacherModel(Name,Designation,email);
                    databaseReference.child(currentuser).child("Teacher").push().setValue(teacherModel);



                }
                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(getApplicationContext(), "User is already registered",Toast.LENGTH_SHORT).show();
                        editText3.setError("User already exits");
                        editText3.requestFocus();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}