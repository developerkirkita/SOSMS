package com.codeholic.sosms.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codeholic.sosms.R;

public class CrashedActivity extends ActionBarActivity {

    TextView timer;
    Button do_not_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_crashed);

        timer = (TextView) findViewById(R.id.timer);
        do_not_send = (Button) findViewById(R.id.do_not_send);

        final CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText("გაგაზვნამდე დარჩა: " + millisUntilFinished / 1000 + "_წმ");
            }

            @Override
            public void onFinish() {
                sendSMS();
                timer.setText("შეტყობინება გაიგზავნა!");
            }
        };

        countDownTimer.start();

        do_not_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                finish();
            }
        });
    }

    private void sendSMS() {
        String phoneNumber = "551506070";
        String first_name = "first name";
        String message = "Giorgi ebanoidze, asaki: 89 wlis. fb-password: moskvichi123";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS გაიგზავნა", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS არ გაიგზავნა", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
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
}
