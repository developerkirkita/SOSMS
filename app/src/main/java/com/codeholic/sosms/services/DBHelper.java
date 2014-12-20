package com.codeholic.sosms.services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bw_user";
    private static final String USER_ID = "id";
    private static final String USER_MNUMBER = "mnumber";
    private static final String USER_FNAME = "fname";
    private static final String USER_LNAME = "lname";
    private static final String USER_AGE = "age";
    private static final String USER_BLOOD = "blood";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE users IF NOT EXISTS (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "mnumber TEXT, " +
                "fname TEXT, " +
                "lname TEXT, " +
                "age TEXT, " +
                "blood TEXT )";

        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addRecords(String mnumber, String fname, String lname, String age, String blood) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String INSERT_RECORD = "INSERT INTO users (mnumber, fname, lname, age, blood) VALUES (" + mnumber + ", " + fname + ", " + lname + ", " + age + ", " + blood + ")";

            db.execSQL(INSERT_RECORD);

            return true;
        } catch(Exception exc) {
            return false;
        }
    }

    public List<String> getUserInfo() {
        List arr = new ArrayList();

        String query = "SELECT  * FROM users";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
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
        }

        return arr;
    }

    public boolean dropTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String DROP_TABLE = "DROP TABLE users";

            db.execSQL(DROP_TABLE);

            return true;
        } catch(Exception exc) {
            return false;
        }
    }
}
