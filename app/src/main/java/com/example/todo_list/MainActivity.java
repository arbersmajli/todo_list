package com.example.todo_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static DatabaseHelper databaseHelper;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this);

        final Button newTaskButton = findViewById(R.id.newTaskButton);

        newTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openEdit();

            }
        });
    }

    @Override
    public void onRestart(){
        super.onRestart();
        populateListView();
    }

    private void populateListView(){
        Log.d(TAG, "pupulateListView : Displaying data in the listView");

        Cursor data = getData();

        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            listData.add(data.getString(1));
        }

        toastMessage("La taille : "+ listData.size());

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
        data.close();
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void openEdit(){
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }


    public Cursor getData(){
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME;

        Cursor data = database.rawQuery(query, null);
        //database.close();
        return data;
    }

    public static boolean addTask(String item){
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_1, item);

        long result = database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
        // database.close();

        Log.d(TAG, "addData : Adding " + item + " to "+ DatabaseHelper.TABLE_NAME);


        if(result == 1){
            return false;
        }else{
            return true;
        }

    }

}
