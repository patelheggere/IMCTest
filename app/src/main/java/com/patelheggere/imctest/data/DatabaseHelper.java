package com.patelheggere.imctest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Table Name
    public static final String TABLE_NAME = "users_tbl";

    // Table columns
    public static final String _ID = "_id";
    public static final String FNAME = "fname";
    public static final String LName = "lname";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String EMAIL = "email";
    public static final String PWD = "pwd";

    public static final String TABLE_NAME_TIME = "time_tbl";
    public static final String TIME = "time";

    // Database Information
    static final String DB_NAME = "IMC.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    // + _ID
    //            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "("  + FNAME + " TEXT , " + LName + " TEXT , "+ EMAIL +" TEXT PRIMARY KEY,"
            +PWD +" TEXT, "+ LAT +" TEXT , "+ LON +" TEXT );";

    private static final String CREATE_TIME_TABLE = "create table " + TABLE_NAME_TIME + "("  + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + EMAIL + " TEXT ," +TIME +" TEXT );";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TIME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TIME);
        onCreate(db);
    }
}
