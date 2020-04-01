package com.example.todo_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static DatabaseHelper databaseHelper;
    private static ListView listView; // affichage user
    private Button searchListView;
    public static ArrayList<String> listDataContent = new ArrayList<>(); // liste qui est ensuite affiché au ListView


    HashMap<String, String> listData = new HashMap<>();


     Boolean newTask = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this);
        //populateListView();
        final Button newTaskButton = findViewById(R.id.newTaskButton);
        final Intent intentEditActivity = new Intent(this, EditActivity.class);

        newTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                newTask = true;
                startActivity(intentEditActivity);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listView.getItemAtPosition(position);
                intentEditActivity.putExtra(databaseHelper.COL_1, listItem.toString());
                startActivity(intentEditActivity);
            }
        });

        intentEditActivity.putExtra("sessionNewTask", newTask);


        populateListView();

    }

    @Override
    public void onRestart(){
        super.onRestart();
        listDataContent.clear();
        populateListView();
    }

    public static void deleteListView(){
            listView.setAdapter(null);
    }

    public void populateListView(){

        Cursor data = databaseHelper.getData();

        while(data.moveToNext()){
            listData.put(data.getString(0), data.getString(1));
            listDataContent.add(data.getString(1));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listDataContent);
        listView.setAdapter(adapter);
        data.close();
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



}
