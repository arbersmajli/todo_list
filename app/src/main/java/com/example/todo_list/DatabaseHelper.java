package com.example.todo_list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";
    public static final String TABLE_NAME_MAIN_ACTIVITY = "task";
    public static final String TABLE_TASK_EDIT_ACTIVITY = "edit_task";
    public static final String DB_NAME = "task.db";

    public static final String COL_0_MA = "id"; // MA pour Main Activity
    public static final String COL_1_MA = "title";
    public static final String COL_2_MA = "color";

    public static final String COL_0_EA = "id"; // EA pour Edit Activity
    public static final String COL_1_EA = "id_task";
    public static final String COL_2_EA = "description";
    public static final String COL_3_EA = "date_fin";
    public static final String COL_4_EA = "termine";
    public static final String COL_5_EA = "link_picture";



    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableMainActivity = "CREATE TABLE " + TABLE_NAME_MAIN_ACTIVITY + " ("+ COL_0_MA +" INTEGER PRIMARY KEY, " + COL_1_MA + " TEXT, " + COL_2_MA + " TEXT)";
        String createTableEditActivity = "CREATE TABLE " + TABLE_TASK_EDIT_ACTIVITY + " ("+ COL_0_EA +" INTEGER PRIMARY KEY, " + COL_1_EA + " INTEGER, " + COL_2_EA + " TEXT, " + COL_3_EA + " TEXT, " + COL_4_EA + " BOOLEAN, " + COL_5_EA + " BLOB )";


        db.execSQL(createTableMainActivity);
        db.execSQL(createTableEditActivity);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MAIN_ACTIVITY);
        onCreate(db);
    }

    public long addDataMainActivity(String title, String color){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_MA, title);
        contentValues.put(COL_2_MA, color);

        long result = database.insert(TABLE_NAME_MAIN_ACTIVITY, null, contentValues);
        //database.insert(TABLE_TASK_EDIT_ACTIVITY, null, contentValues);


        database.close();

        Log.d(TAG, "addData : Adding " + title + " to "+ TABLE_NAME_MAIN_ACTIVITY);

        return result;
    }


    public boolean addDataEditActivity(int idTask, String description, String dateFin, boolean finished, byte[] link_picture){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_EA, idTask);
        contentValues.put(COL_2_EA, description);
        contentValues.put(COL_3_EA, dateFin);
        contentValues.put(COL_4_EA, finished);
        contentValues.put(COL_5_EA, link_picture);

        long result = database.insert(TABLE_TASK_EDIT_ACTIVITY, null, contentValues);

        database.close();
        if(result == 1){
            return false;
        }else{
            return true;
        }

    }

/*
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
*/
    public boolean deleteDataMainActivity(int idMainActivity){
        SQLiteDatabase database = this.getWritableDatabase();

        long result = database.delete(TABLE_NAME_MAIN_ACTIVITY, COL_0_MA +  " = " + idMainActivity, null);
        // database.delete(TABLE_TASK_EDIT_ACTIVITY, COL_1_EA + " = '"+ item + "'", null);
        deleteCascadeActivity(idMainActivity);

        if(result == 1){
            return false;
        }else{
            return true;
        }
    }

    public boolean deleteCascadeActivity(int idMainActivity){
        SQLiteDatabase database = this.getWritableDatabase();
        long result = database.delete(TABLE_TASK_EDIT_ACTIVITY, COL_1_EA + " = " + idMainActivity, null);
        if(result == 1){
            return false;
        }else{
            return true;
        }


    }

    public boolean updateData(int id, String newTitle, String newColor){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_MA, newTitle);
        contentValues.put(COL_2_MA, newColor);

        long result = database.update(TABLE_NAME_MAIN_ACTIVITY, contentValues, COL_0_MA + " = " + id, null);

        if(result == 1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateData(int idTask, int idSubTask, String description, String date, Boolean finished, byte[] link_picture ){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_EA, description);
        contentValues.put(COL_3_EA, date);
        contentValues.put(COL_4_EA, finished);
        contentValues.put(COL_5_EA, link_picture);

        long result = database.update(TABLE_TASK_EDIT_ACTIVITY, contentValues, COL_0_EA + " = " + idSubTask + " AND " + COL_1_EA + " = " + idTask, null);

        if(result == 1){
            return false;
        }else{
            return true;
        }
    }




    public boolean deleteDataEditActivity(int id){
        SQLiteDatabase database = this.getWritableDatabase();

        long result = database.delete(TABLE_TASK_EDIT_ACTIVITY, COL_0_EA + " = '" + id + "'", null);

        if(result == 1){
            return false;
        }else{
            return true;
        }

    }

    public boolean deleteDataEditActivity(int idTask, int idSubTask){
        SQLiteDatabase database = this.getWritableDatabase();

        long result = database.delete(TABLE_TASK_EDIT_ACTIVITY, COL_0_EA + " = " + idSubTask + " AND " + COL_1_EA + " = " + idTask, null);

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

    public Cursor getData(String table, String columnToPoint, int id){
        String clauseWhere = "";

        if(id > -1){
            clauseWhere = " WHERE " + columnToPoint +" = " + id + " ";
        }

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + table + clauseWhere;
        Cursor data = database.rawQuery(query, null);
        //database.close();
        return data;
    }

    public Cursor getData(String table, String columnToPoint, String columnToJoin, int idTask, int idSubTask){
        String clauseWhere = "";

        if(idTask > -1){
            clauseWhere = " WHERE " + columnToPoint +" = " + idSubTask + " AND " + columnToJoin + " = " + idTask;
        }

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + table + clauseWhere;
        Cursor data = database.rawQuery(query, null);
        //database.close();
        return data;
    }

    public Bitmap getImage(Integer idSubTask){
        SQLiteDatabase database = this.getWritableDatabase();
        Bitmap bitmap  = null;
        String query = "SELECT * FROM " + TABLE_TASK_EDIT_ACTIVITY + " WHERE " + COL_0_EA + " = " + idSubTask;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToNext()){
            byte[] image = cursor.getBlob(5);
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        }

        return bitmap;
    }




}

















