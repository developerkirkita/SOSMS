package com.codeholic.sosms.model;

/**
 * Created by DeveloperKirkita on 20.12.2014.
 */
public class User {
    int _id;
    String _mnumner;
    String _first_name;
    String _last_name;
    String _age;
    String _blood;

    // empty constructor
    public User() {

    }

    // constructor
    public User(int id, String mnumner, String first_name, String last_name, String age, String blood) {
        this._id = id;
        this._mnumner = mnumner;
        this._first_name = first_name;
        this._last_name = last_name;
        this._age = age;
        this._blood = blood;
    }

    // constructor
    public User(String mnumber, String first_name, String last_name, String age, String blood) {
        this._mnumner = mnumber;
        this._first_name = first_name;
        this._last_name = last_name;
        this._age = age;
        this._blood = blood;
    }

    // get ID
    public int getID() {
        return this._id;
    }

    // set ID
    public void setID(int id) {
        this._id = id;
    }

    // get mnumber
    public String getMnumber() {
        return this._mnumner;
    }

    // set mnumber
    public void setMnumber(String mnumber) {
        this._mnumner = mnumber;
    }

    // get first_name
    public String getFirstName() {
        return this._first_name;
    }

    // set first_name
    public void setFirstName(String first_naem) {
        this._first_name = first_naem;
    }

    // get last_name
    public String getLastName() {
        return this._last_name;
    }

    // set last_name
    public void setLastName(String last_name) {
        this._last_name = last_name;
    }

    // set age
    public void setAge(String age) {
        this._age = age;
    }

    // get age
    public String getAge () {
        return this._age;
    }

    // get blood
    public String getBlood() {
        return this._blood;
    }

    // set blood
    public void setBlood(String blood) {
        this._blood = blood;
    }

}
