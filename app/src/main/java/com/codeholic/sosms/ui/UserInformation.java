package com.codeholic.sosms.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codeholic.sosms.R;
import com.codeholic.sosms.services.DBHelper;
import com.codeholic.sosms.services.UserFunctions;


public class UserInformation extends ActionBarActivity {

    EditText phone, first_name, last_name, age, blood_group;
    Button save;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String etFontPath = "fonts/bpg_rioni_vera.otf";
        Typeface font_et = Typeface.createFromAsset(getAssets(), etFontPath);

        String path = "fonts/bpg_nino_mtavruli_bold.ttf";
        String btn_font_path = "fonts/bog_solo_mtavruli.otf";
        Typeface font_btn = Typeface.createFromAsset(getAssets(), btn_font_path);

        phone = (EditText) findViewById(R.id.mobile_number);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        age = (EditText) findViewById(R.id.age);
        blood_group = (EditText) findViewById(R.id.blood);

        save = (Button) findViewById(R.id.save_user);
        save.setTypeface(font_btn);


        //phone.setTypeface(font_et);
        //first_name.setTypeface(font_et);
        //last_name.setTypeface(font_et);
        //age.setTypeface(font_et);
        //blood_group.setTypeface(font_et);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new saveTask().execute();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about_user_info) {
            Intent i = new Intent(UserInformation.this, AboutPage_UserInfo.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public class saveTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected  void onPreExecute() {
            progress = new ProgressDialog(UserInformation.this);
            progress.setMessage("ინახება...");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... param) {

            DBHelper db = new DBHelper(getApplicationContext());

            String p, f, l, a, b;

            p = phone.getText().toString();
            f = first_name.getText().toString();
            l = last_name.getText().toString();
            a = age.getText().toString();
            b = blood_group.getText().toString();


            if(p.trim().length() > 0 && f.trim().length() > 0 && l.trim().length() > 0 &&
                    a.trim().length() > 0 && b.trim().length() > 0) {
                // save data and go to next activity
                UserFunctions userFunction = new UserFunctions();
                userFunction.logoutUser(getApplicationContext());
                db.saveUser(p,f,l,a,b);

                Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
                dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(dashboard);
                finish();
            } else {
                showToast("შეავსეთ ყველა საჭირო ველი!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progress.dismiss();
        }
    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UserInformation.this, text,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
