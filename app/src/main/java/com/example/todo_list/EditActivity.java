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
    private Boolean sessionNewTask,newSubTask;
    public static ListView listViewSubTask;
    public static DatabaseHelper databaseHelper;
    public static ArrayList<String> listDataContent = new ArrayList<>(); // liste qui est ensuite affiché au ListView
    public static ArrayList<Integer> listIdDataContent = new ArrayList<>();
    int sessionId, newIdCreated;




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
        sessionId = getIntent().getIntExtra("sessionId", -1);
        listViewSubTask = findViewById(R.id.listViewSubTask);
        final Intent intentEditSubActivity = new Intent(this, SubActivity.class);


        buttonNewSubTask.setVisibility(View.GONE);
        listViewSubTask.setVisibility(View.GONE);

        if(sessionNewTask){
            editTextTitle.setText("");
            //toastMessage("fumier"+ sessionId);
            buttonDelete.setVisibility(View.GONE);

        }else {
            //populateDataEditActivity(sessionId);
            //populateDataEditActivity(sessionId);
            buttonNewSubTask.setVisibility(View.VISIBLE);
            listViewSubTask.setVisibility(View.VISIBLE);
            populateTitleById(sessionId);
            buttonSubmit.setText("Update");


            //editTextTitle.setText("_____"+sessionId);
        }





        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(editTextTitle.length() != 0){
                     String newEntry = editTextTitle.getText().toString();
                     if(sessionNewTask){
                         AddData(newEntry);
                     }else{
                         Update(sessionId, editTextTitle.getText().toString());
                         //DeleteData(sessionId);
                         //AddData(newEntry);
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
                DeleteData(sessionId);
                MainActivity.deleteListView();
                //deleteListView();
                finish();
            }
        });


        listViewSubTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listViewSubTask.getItemAtPosition(position);
                int idSubTask = Integer.parseInt(listItem.toString().substring(0, listItem.toString().indexOf(")")));  // fais un substr pour recupérer le chiffre

                intentEditSubActivity.putExtra("newSubTask", false);
                intentEditSubActivity.putExtra(databaseHelper.COL_0_EA, sessionId); // id de la tache
                intentEditSubActivity.putExtra(databaseHelper.COL_1_EA, idSubTask); // id de la sous-tâche


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
                //intentEditSubActivity.putExtra(databaseHelper.COL_1_EA, editTextTitle.getText().toString());
                intentEditSubActivity.putExtra("newSubTask", true);
                intentEditSubActivity.putExtra(databaseHelper.COL_0_EA, sessionId);
                //intentEditSubActivity.putExtra(databaseHelper.COL_0_EA, sessionId);

                startActivity(intentEditSubActivity);
            }
        });
        listDataContent.clear();
        listViewSubTask.setAdapter(null);


        populateListView(DatabaseHelper.TABLE_TASK_EDIT_ACTIVITY, sessionId);

    }

    public void populateTitleById(int id){
        Cursor data = databaseHelper.getData(DatabaseHelper.TABLE_NAME_MAIN_ACTIVITY, DatabaseHelper.COL_0_MA, id);
        while(data.moveToNext()){
            editTextTitle.setText(data.getString(1));
        }

        data.close();
    }


    public void DeleteData(int id){
        MainActivity.databaseHelper.deleteDataMainActivity(id);

    //    MainActivity.databaseHelper.deleteDataMainActivity(entry);
    }

    @Override
    public void onRestart(){
        super.onRestart();

        listDataContent.clear();
        listViewSubTask.setAdapter(null);
        populateListView(DatabaseHelper.TABLE_TASK_EDIT_ACTIVITY, sessionId);
        //populateListView(DatabaseHelper.TABLE_TASK_EDIT_ACTIVITY, editTextTitle.getText().toString());
    }

    public void Update(int id, String newTitle){
        MainActivity.databaseHelper.updateData(id, newTitle);
    }


    public void  AddData(String entry){
        MainActivity.databaseHelper.addDataMainActivity(entry);
    }

    public void populateListView(String table, int idActivity){
        Cursor data = databaseHelper.getData(table, databaseHelper.COL_1_EA, idActivity);

        while(data.moveToNext()){
            listDataContent.add(data.getInt(0)+ ") " +data.getString(2) + ", " + data.getString(3));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listDataContent);
        listViewSubTask.setAdapter(adapter);
        data.close();


    }

    public void populateListView(String table, String clause){
        Cursor data = databaseHelper.getData(table, clause);

        while(data.moveToNext()){
            listDataContent.add(data.getInt(0)+ ") " +data.getString(2));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listDataContent);
        listViewSubTask.setAdapter(adapter);
        data.close();

    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
