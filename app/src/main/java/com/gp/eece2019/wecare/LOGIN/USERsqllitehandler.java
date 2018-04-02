package com.gp.eece2019.wecare.LOGIN;

/**
 * Created by budopest on 19/03/18.
 */
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

public class USERsqllitehandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "User_details.db";
    public static final String TABLE_NAME = "user_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "FIRSTNAME";
    public static final String COL_3 = "LASTNAME";
    public static final String COL_4 = "USERNAME";
    public static final String COL_5 = "PHONE";
    public static final String COL_6 = "BIRTHDATE";
    public static final String COL_7 = "USERID";

    public USERsqllitehandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,FIRSTNAME TEXT,LASTNAME TEXT,USERNAME TEXT,PHONE TEXT,BIRTHDATE TEXT,USERID TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String fname,String lname,String uname,String phone,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,fname);
        contentValues.put(COL_3,lname);
        contentValues.put(COL_4,uname);
        contentValues.put(COL_5,phone);
        contentValues.put(COL_6,date);
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
