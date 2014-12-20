package com.codeholic.sosms.services;

import android.content.Context;

/**
 * Created by DeveloperKirkita on 20.12.2014.
 */
public class UserFunctions {

    public UserFunctions(){

    }

    public boolean isUserLoggedIn(Context context){
        DBHelper db = new DBHelper(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }

    public boolean logoutUser(Context context){
        DBHelper db = new DBHelper(context);
        db.resetTables();
        return true;
    }
}
