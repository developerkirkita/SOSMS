package com.codeholic.sosms.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.codeholic.sosms.model.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bw_user";
    private static final String TABLE_LOGIN = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_MNUMBER = "mnumber";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_AGE = "age";
    private static final String KEY_BLOOD = "blood";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_MNUMBER + " TEXT,"
                + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_AGE + " TEXT,"
                + KEY_BLOOD + " TEXT " + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);
    }

    public void saveUser(String mNumber, String first_name, String last_name, String age, String blood) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MNUMBER, mNumber);
        values.put(KEY_FIRST_NAME, first_name);
        values.put(KEY_LAST_NAME, last_name);
        values.put(KEY_AGE, age);
        values.put(KEY_BLOOD, blood);

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }


    public List<String> getUserInfo () {
        List arr = new ArrayList();

        String query = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int id = Integer.parseInt(cursor.getString(0));
            String mnumber = cursor.getString(1);
            String fname = cursor.getString(2);
            String lname = cursor.getString(3);
            String age = cursor.getString(4);
            String blood = cursor.getString(5);

            arr.add(id);
            arr.add(mnumber);
            arr.add(fname);
            arr.add(lname);
            arr.add(age);
            arr.add(blood);
        } else {
            Log.e("ERORRRR: ", "ERRORRR");
        }

        return arr;
    }

    public List<User> getUser() {
        List<User> userInfo = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setMnumber(cursor.getString(1));
                user.setFirstName(cursor.getString(2));
                user.setLastName(cursor.getString(3));
                user.setAge(cursor.getString(5));
                user.setBlood(cursor.getString(6));
                // Adding subject to list
                userInfo.add(user);
            } while (cursor.moveToNext());
        }

        // return subject list
        return userInfo;
    }


    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
}
