package com.patelheggere.imctest.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.patelheggere.imctest.models.UserDetails;

import static com.patelheggere.imctest.data.DatabaseHelper.PWD;


public class DBManager {
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(UserDetails object) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.FNAME, object.getFname());
        contentValue.put(DatabaseHelper.LName, object.getLname());
        contentValue.put(DatabaseHelper.LAT, object.getLat());
        contentValue.put(DatabaseHelper.LON, object.getLon());
        contentValue.put(PWD, object.getPwd());
        contentValue.put(DatabaseHelper.EMAIL, object.getEmail());
       // System.out.println("Inserted:"+database.insert(DatabaseHelper.TABLE_NAME, null, contentValue));
        return database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public long insertTime(String email, String time) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.EMAIL, email);
        contentValue.put(DatabaseHelper.TIME, time);
        return database.insert(DatabaseHelper.TABLE_NAME_TIME, null, contentValue);
    }

    public Cursor fetchAllSchedules(String email) {
        Cursor cursor;// = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        cursor = database.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_TIME+ " WHERE email='"+email+"'" , null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch(String email, String pwd) {
        Cursor cursor;// = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        cursor = database.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME+ " WHERE email='"+email+"' AND pwd='"+pwd+"'" , null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
/*
    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECT, name);
        contentValues.put(DatabaseHelper.DESC, desc);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }*/

    public boolean isEmailExists(String email) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME,// Selecting Table
                new String[]{ DatabaseHelper.EMAIL, PWD},//Selecting columns want to query
                DatabaseHelper.EMAIL + "=?",
                new String[]{email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }

        //if email does not exist return false
        return false;
    }
    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }
}