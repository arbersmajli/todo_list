package com.example.todo_list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "Edit";
    private Button buttonSubmit, buttonDelete, buttonCancel;
    private EditText editTextTitle, editTextDate;
    private String sessionTitle;
    private Boolean sessionNewTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonCancel = findViewById(R.id.buttonCancel);
        editTextTitle = findViewById(R.id.editTextTitle);


        sessionTitle = getIntent().getStringExtra(DatabaseHelper.COL_1);
        sessionNewTask = getIntent().getBooleanExtra("sessionNewTask", false);

        toastMessage("pouloulou:"+ sessionNewTask);

        if(sessionNewTask){
            editTextTitle.setText("");
        }else {
           editTextTitle.setText(sessionTitle);
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String newEntry = editTextTitle.getText().toString();
                 if(editTextTitle.length() != 0){
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
                toastMessage("fdp tu veux vrmnt supprimer " + sessionTitle);
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void DeleteData(String entry){
        MainActivity.databaseHelper.deleteData(entry);
    }

    public void  AddData(String entry){
        MainActivity.databaseHelper.addData(entry);
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
