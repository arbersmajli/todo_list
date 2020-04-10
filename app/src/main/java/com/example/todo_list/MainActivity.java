package com.example.todo_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static DatabaseHelper databaseHelper;
    private static ListView listView; // affichage user
    private Button searchListView;
    private static EditText searchEditText;
    public static ArrayList<String> listDataContent = new ArrayList<>(); // liste qui est ensuite affiché au ListView

    HashMap<String, String> listData = new HashMap<>();
    private static Button buttonSearch;
    private static FloatingActionButton floatingActionButton;

     Boolean newTask = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = findViewById(R.id.listView);
        searchEditText = findViewById(R.id.searchTask);
        databaseHelper = new DatabaseHelper(this);
        buttonSearch = findViewById(R.id.searchTaskButton);
        floatingActionButton = findViewById(R.id.floating_button);
        final String contentEditText = searchEditText.getText().toString();
        //populateListView();
        //final Button newTaskButton = findViewById(R.id.newTaskButton);
        final Intent intentEditActivity = new Intent(this, EditActivity.class);

        searchEditText.setInputType(InputType.TYPE_NULL);
/*
        newTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                newTask = true;
                intentEditActivity.putExtra("sessionNewTask", true);
                intentEditActivity.putExtra("sessionId", -1);
                startActivity(intentEditActivity);
            }
        });
*/
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTask = true;
                intentEditActivity.putExtra("sessionNewTask", true);
                intentEditActivity.putExtra("sessionId", -1);
                startActivity(intentEditActivity);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listView.getItemAtPosition(position);
                newTask = false;
                int idPosition = Integer.parseInt(listItem.toString().substring(0, listItem.toString().indexOf(")")));
                //intentEditActivity.putExtra("sessionNewTask", false);
                intentEditActivity.putExtra("sessionNewTask", newTask);
                intentEditActivity.putExtra("sessionId", idPosition);
                //intentEditActivity.putExtra(databaseHelper.COL_1_MA, listItem.toString());
                startActivity(intentEditActivity);
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListView();
                populateListView(DatabaseHelper.TABLE_NAME_MAIN_ACTIVITY, searchEditText.getText().toString());
                if(listDataContent.isEmpty()){
                    toastMessage("Aucune correspondance");
                }else if(searchEditText.length() <= 0){
                    toastMessage("Rien recherché");
                }
            }
        });

        populateListView(DatabaseHelper.TABLE_NAME_MAIN_ACTIVITY,"");

    }

    @Override
    public void onRestart(){
        super.onRestart();
        listDataContent.clear();
        populateListView(DatabaseHelper.TABLE_NAME_MAIN_ACTIVITY,"");
    }

    public static void deleteListView(){
        listDataContent.clear();
        listView.setAdapter(null);
    }

    public void populateListView(String table, String clause){

        Cursor data = databaseHelper.getData(table, clause);

        while(data.moveToNext()){
            //listData.put(data.getString(0), data.getString(1));
            listDataContent.add(data.getString(0) + ") " + data.getString(1));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listDataContent);
        listView.setAdapter(adapter);
        data.close();
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



}
