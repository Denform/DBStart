package com.example.dbstart;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ShopTable";
    public static final String TABLE_Shop = "dataShop";

    public static final String KEY_Id = "_id";
    public static final String KEY_NameWorker = "name";
    public static final String KEY_Department = "depart";
    public static final String KEY_Salary = "salary";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + TABLE_Shop +
                "(" + KEY_Id + " integer primary key," + KEY_NameWorker +
                " text," + KEY_Department + " text," + KEY_Salary + " integer" +
                ")");
    }
//    asd
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists " + TABLE_Shop);

        onCreate(db);
    }
}