package com.rejoicewindow.hive.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, "xumade.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE manager(_id INTEGER PRIMARY KEY AUTOINCREMENT, f_number VARCHAR(20), f_count VARCHAR(5), a_name VARCHAR(20), a_count VARCHAR(5))");
        db.execSQL("CREATE TABLE rdump(_id INTEGER PRIMARY KEY AUTOINCREMENT, number VARCHAR(20),time VARCHAR(128),is_1 VARCHAR(2))");
        db.execSQL("CREATE TABLE rsteal(_id INTEGER PRIMARY KEY AUTOINCREMENT, number VARCHAR(20),time VARCHAR(128),is_1 VARCHAR(2))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
