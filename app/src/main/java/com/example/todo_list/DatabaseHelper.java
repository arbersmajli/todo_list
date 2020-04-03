package com.example.todo_list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";
    public static final String TABLE_NAME_MAIN_ACTIVITY = "task";
    public static final String TABLE_TASK_EDIT_ACTIVITY = "edit_task";
    public static final String DB_NAME = "task.db";

    public static final String COL_0_MA = "ID"; // MA pour Main Activity
    public static final String COL_1_MA = "title";

    public static final String COL_0_EA = "ID"; // EA pour Edit Activity
    public static final String COL_1_EA = COL_1_MA;
    public static final String COL_2_EA = "description";
    public static final String COL_3_EA = "date_fin";
    public static final String COL_4_EA = "termine";



    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableMainActivity = "CREATE TABLE " + TABLE_NAME_MAIN_ACTIVITY + " ("+ COL_0_MA +" INTEGER PRIMARY KEY, " + COL_1_MA + " TEXT )";
        String createTableEditActivity = "CREATE TABLE " + TABLE_TASK_EDIT_ACTIVITY + " ("+ COL_0_EA +" INTEGER PRIMARY KEY, " + COL_1_EA + " TEXT, " + COL_2_EA + " TEXT, " + COL_3_EA + " TEXT, " + COL_4_EA + " BOOLEAN )";


        db.execSQL(createTableMainActivity);
        db.execSQL(createTableEditActivity);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MAIN_ACTIVITY);
        onCreate(db);
    }

    public boolean addDataMainActivity(String item){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_MA, item);

        long result = database.insert(TABLE_NAME_MAIN_ACTIVITY, null, contentValues);
        //database.insert(TABLE_TASK_EDIT_ACTIVITY, null, contentValues);

        database.close();

        Log.d(TAG, "addData : Adding " + item + " to "+ TABLE_NAME_MAIN_ACTIVITY);

        if(result == 1){
            return false;
        }else{
            return true;
        }
    }

    public boolean addDataEditActivity(String title, String description, String dateFin, boolean finished){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_EA, title);
        contentValues.put(COL_2_EA, description);
        contentValues.put(COL_3_EA, dateFin);
        contentValues.put(COL_4_EA, finished);

        long result = database.insert(TABLE_TASK_EDIT_ACTIVITY, null, contentValues);

        database.close();
        if(result == 1){
            return false;
        }else{
            return true;
        }

    }



    public boolean deleteDataMainActivity(String item){
        SQLiteDatabase database = this.getWritableDatabase();

        long result = database.delete(TABLE_NAME_MAIN_ACTIVITY, COL_1_MA +  " = '" + item + "'", null);
        // database.delete(TABLE_TASK_EDIT_ACTIVITY, COL_1_EA + " = '"+ item + "'", null);

        if(result == 1){
            return false;
        }else{
            return true;
        }
    }

    public boolean deleteDataEditActivity(String title, String description){
        SQLiteDatabase database = this.getWritableDatabase();

        long result = database.delete(TABLE_TASK_EDIT_ACTIVITY, COL_1_EA + " = '" + title + "' AND " + COL_2_EA + " = '" + description + "'", null);


        if(result == 1){
            return false;
        }else{
            return true;
        }

    }


    public Cursor getData(String table, String clause){
        String clauseWhere = "";

        if(clause.length() > 0){
            clauseWhere = " WHERE title = '" + clause + "' ";
        }


        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + table + clauseWhere;
        Cursor data = database.rawQuery(query, null);
        //database.close();
        return data;
    }



}

















