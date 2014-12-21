package com.codeholic.sosms.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codeholic.sosms.R;
import com.codeholic.sosms.services.GPSTracker;

public class CrashedActivity extends ActionBarActivity {

    TextView timer;
    Button do_not_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_crashed);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.alarm);

        timer = (TextView) findViewById(R.id.timer);
        do_not_send = (Button) findViewById(R.id.do_not_send);

        final CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mp.start();
                timer.setText("გაგაზვნამდე დარჩა: " + millisUntilFinished / 1000 + "_წმ");

            }

            @Override
            public void onFinish() {
                sendSMS();
                timer.setText("შეტყობინება გაიგზავნა!");
            }
        };

        countDownTimer.start();

        getLoc();

        do_not_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                countDownTimer.cancel();
                finish();
            }
        });
    }

    private void sendSMS() {
        String phoneNumber = "599426341";
        String first_name = "first name";
        String message = "Pirovneba: Nika Kirkitadze, Asaki: 20, Sisxlis jgufi: 2 uaryofiti - Lokacia: ";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS გაიგზავნა", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS არ გაიგზავნა", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void getLoc() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                GPSTracker gps;

                gps = new GPSTracker(CrashedActivity.this);

                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    //gps.showSettingsAlert();
                    Log.e("Can not get: ", "can not get location");
                    showToast("Can't get Location");
                }
            }
        };

        thread.start();

    }



    protected void sendEmail() {
        Log.i("Send email", "");

        String[] TO = {"developerkirkita@gmail.com"};
        String[] CC = {"saba@bit-wire.org"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CrashedActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
