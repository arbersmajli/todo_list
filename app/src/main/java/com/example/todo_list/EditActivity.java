package com.example.todo_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "Edit";
    private Button buttonSubmit, buttonDelete, buttonCancel, buttonNewSubTask;
    private EditText editTextTitle, editTextDate;
    private String sessionTitle;
    private Boolean sessionNewTask;
    public static ListView listViewSubTask;
    public static DatabaseHelper databaseHelper;
    public static ArrayList<String> listDataContent = new ArrayList<>(); // liste qui est ensuite affiché au ListView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonCancel = findViewById(R.id.buttonCancel);
        editTextTitle = findViewById(R.id.editTextTitle);
        buttonNewSubTask = findViewById(R.id.buttonNewSubTask);
        databaseHelper = new DatabaseHelper(this);
        sessionTitle = getIntent().getStringExtra(DatabaseHelper.COL_1_MA);
        sessionNewTask = getIntent().getBooleanExtra("sessionNewTask", false);
        listViewSubTask = findViewById(R.id.listViewSubTask);
        final Intent intentEditSubActivity = new Intent(this, SubActivity.class);


        if(sessionNewTask){
            editTextTitle.setText("");
            buttonDelete.setVisibility(View.GONE);
        }else {
           editTextTitle.setText(sessionTitle);
        }




        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(editTextTitle.length() != 0){
                     String newEntry = editTextTitle.getText().toString();
                     if(sessionNewTask){
                         AddData(newEntry);
                     }else{
                         DeleteData(sessionTitle);
                         AddData(newEntry);
                     }
                     editTextTitle.setText("");
                     finish();
                 }else{
                     toastMessage("T'as rien mis ");
                 }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteData(sessionTitle);
                MainActivity.deleteListView();
                //deleteListView();
                finish();
            }
        });


        listViewSubTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listViewSubTask.getItemAtPosition(position);
                intentEditSubActivity.putExtra(databaseHelper.COL_2_EA, listItem.toString());
                intentEditSubActivity.putExtra(databaseHelper.COL_1_MA,sessionTitle);
                startActivity(intentEditSubActivity);
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonNewSubTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentEditSubActivity.putExtra(databaseHelper.COL_1_EA, editTextTitle.getText().toString());
                startActivity(intentEditSubActivity);
            }
        });


        listDataContent.clear();
        listViewSubTask.setAdapter(null);
        populateListView(DatabaseHelper.TABLE_TASK_EDIT_ACTIVITY, editTextTitle.getText().toString());

    }

    public void DeleteData(String entry){
        MainActivity.databaseHelper.deleteDataMainActivity(entry);
    }




    @Override
    public void onRestart(){
        super.onRestart();
        listDataContent.clear();
        listViewSubTask.setAdapter(null);
        populateListView(DatabaseHelper.TABLE_TASK_EDIT_ACTIVITY, editTextTitle.getText().toString());
    }


    public void  AddData(String entry){
        MainActivity.databaseHelper.addDataMainActivity(entry);
    }

    public void populateListView(String table, String clause){
        Cursor data = databaseHelper.getData(table, clause);

        while(data.moveToNext()){
            listDataContent.add(data.getString(2));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listDataContent);
        listViewSubTask.setAdapter(adapter);
        data.close();

    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
