package com.example.adminbat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adminbat.teacher.TeacherActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AfterloginActivity extends AppCompatActivity {
    Button button1,button2,button3;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterlogin);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AfterloginActivity.this, TeacherActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AfterloginActivity.this, BatchActivity.class);
                startActivity(intent);
            }
        });
        
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
            Toast.makeText(AfterloginActivity.this, "Logged out successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AfterloginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}