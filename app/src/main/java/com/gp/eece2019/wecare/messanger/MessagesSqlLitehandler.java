package com.gp.eece2019.wecare.messanger;


/**
 * Created by budopest on 03/04/18.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ProgrammingKnowledge on 4/3/2015.
 */
public class MessagesSqlLitehandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "messages.db";
    public static final String TABLE_NAME = "messages_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Message";
    public static final String COL_3 = "TIME";
    public static final String COL_4 = "SQLID";
    public static final String COL_5 = "TYPE";


    public MessagesSqlLitehandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,Message TEXT,TIME TEXT,SQLID INTEGER,TYPE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String message,String time,String sqlid,String type) {
        if(message.equals("")|| time.equals("") ||sqlid.equals("") ||type.equals(""))
            return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,message);
        contentValues.put(COL_3,time);
        contentValues.put(COL_4,sqlid);
        contentValues.put(COL_5,type);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
/*
        public boolean updateData(String id,String name,String Tel) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1,id);
            contentValues.put(COL_2,name);
            contentValues.put(COL_3,Tel);
            db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
            return true;
        }*/

    public Integer deleteData (String ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {ID});

    }
}


