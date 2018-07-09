package com.gp.eece2019.wecare.measurements;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MeasureSQLiteHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "measurements.db";
    public static final String TABLE_NAME = "measurements_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TEMPERATURE";
    public static final String COL_3 = "TEMPERATUREFLAG";
    public static final String COL_4 = "HEARTRATE";
    public static final String COL_5 = "HEARTRATEFLAG";
    public static final String COL_6 = "DATE";
    public static final String COL_7 = "LATESTID";


    public MeasureSQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,TEMPERATURE INTEGER,TEMPERATUREFLAG INTEGER,HEARTRATE INTEGER,HEARTRATEFLAG INTEGER, DATE TEXT,LATESTID TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(int t,int tf, int p , int pf,String date,String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,t);
        contentValues.put(COL_3,tf);
        contentValues.put(COL_4,p);
        contentValues.put(COL_5,pf);
        contentValues.put(COL_6,date);
        contentValues.put(COL_7,id);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getid(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ID from "+TABLE_NAME,null);
        return res;
    }

    /*public boolean updateData(String id,String name,String surname,String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,surname);
        contentValues.put(COL_4,marks);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }
    */

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}

