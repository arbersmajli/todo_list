package com.example.todo_list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "Edit";

    //DatabaseHelper mDatabaseHelper;
    private Button buttonSubmit, buttonCancel;
    private EditText editTextTitle, editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        editTextTitle = findViewById(R.id.editTextTitle);

        //mDatabaseHelper = new DatabaseHelper(this);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String newEntry = editTextTitle.getText().toString();
                 if(editTextTitle.length() != 0){
                     //AddData("test");
                     AddData(newEntry);
                     editTextTitle.setText("");
                     finish();
                 }else{
                     toastMessage("T'as rien mis ");
                 }
            }
        });

    }

    public void  AddData(String entry){
        boolean insertData = MainActivity.addTask(entry);
        if(insertData){
            toastMessage("Data succesfully inserted : " + entry);
        }else{
            toastMessage("t'es nul à chier");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
