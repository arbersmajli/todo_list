package com.example.todo_list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";
    public static final String TABLE_NAME = "task";
    public static final String DB_NAME = "task.db";
    public static final String COL_0 = "ID";
    public static final String COL_1 = "title";



    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("+ COL_0 +" INTEGER PRIMARY KEY, " + COL_1  + " TEXT )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
/*
    public boolean addData(String item){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, item);

        long result = database.insert(TABLE_NAME, null, contentValues);
        database.close();

        Log.d(TAG, "addData : Adding " + item + " to "+ TABLE_NAME);


        if(result == 1){
            return false;
        }else{
            return true;
        }

    }
*/

/*
    public Cursor getData(){
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = database.rawQuery(query, null);
        //database.close();
        return data;
    }
*/
}

















