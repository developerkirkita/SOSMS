package com.codeholic.sosms.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.codeholic.sosms.R;
import com.codeholic.sosms.services.Accelerometer;
import com.codeholic.sosms.services.UserFunctions;


public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserFunctions userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())) {
            setContentView(R.layout.activity_main);
            startService(new Intent(this, Accelerometer.class));


        } else {
            Intent i = new Intent(this, UserInformation.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }

    private void log_out() {
        UserFunctions userFunctions = new UserFunctions();
        userFunctions.logoutUser(getApplicationContext());
        Intent i = new Intent(getApplicationContext(), UserInformation.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }



    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, text,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}