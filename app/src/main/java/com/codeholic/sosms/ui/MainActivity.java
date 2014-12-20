package com.codeholic.sosms.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.gsm.SmsManager;

import com.codeholic.sosms.R;
import com.codeholic.sosms.model.User;
import com.codeholic.sosms.services.BackgroundService;
import com.codeholic.sosms.services.DBHelper;
import com.codeholic.sosms.services.UserFunctions;

import java.util.List;
import java.util.logging.Handler;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 4000;

    Button clear;
    Button reset;

    ImageButton do_not_send;

    TextView timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserFunctions userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())) {
            setContentView(R.layout.activity_main);
            startService(new Intent(this, BackgroundService.class));
            senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        for (int i = 0; i < num.size(); i++) {
//             Log.i("LOG", " @@@@@@@@" + num.get(i));
//        }

            clear = (Button) findViewById(R.id.clear);
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Clear();
                }
            });

            reset = (Button) findViewById(R.id.reset);
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserFunctions userFunctions = new UserFunctions();
                    userFunctions.logoutUser(getApplicationContext());
                    Intent i = new Intent(getApplicationContext(), UserInformation.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });
        } else {
            Intent i = new Intent(this, UserInformation.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

        timer = (TextView) findViewById(R.id.timer);
        do_not_send = (ImageButton) findViewById(R.id.do_not_send);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);

                    Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
                    do_not_send.setVisibility(View.VISIBLE);

                    TextView warning_message = (TextView) findViewById(R.id.warning_message);
                    warning_message.setText("შეჯახება!");

                    new CountDownTimer(30000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            timer.setText("გაგაზვნამდე დარჩა: " + millisUntilFinished / 1000 + "_წმ");
                        }

                        public void onFinish() {
                            SendSMS();
                            timer.setText("შეტყობინება გაიგზავნა!");
                        }
                    }.start();


                    do_not_send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            restart();
                        }
                    });
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    private void restart() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // can be ignored //
    }


    private void SendSMS() {

        DBHelper db = new DBHelper(MainActivity.this);


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
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void Clear() {
        TextView warning_message = (TextView) findViewById(R.id.warning_message);
        warning_message.setText("");
    }
}