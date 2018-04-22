package com.gp.eece2019.wecare.measurements;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by budopest on 21/04/18.
 */


public class LastRECdata extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "lastdate.db";
    public static final String TABLE_NAME = "lastdate_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DATE";



    public LastRECdata(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,date);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
    /*
    public Cursor getdate(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select DATE from "+TABLE_NAME,null);
        return res;
    }
    */

    public boolean updateData(String id,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,date);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }


    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}


