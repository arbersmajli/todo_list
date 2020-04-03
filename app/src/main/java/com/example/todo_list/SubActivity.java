package com.example.todo_list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class SubActivity extends AppCompatActivity {

    private Button add, cancel, delete;
    private EditText title, description, date;
    private Switch switchFinished;
    private boolean finished;
    public static DatabaseHelper databaseHelper;
    String sessionTitle, sessionDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        add = findViewById(R.id.buttonSubAdd);
        cancel = findViewById(R.id.buttonSubCancel);
        delete = findViewById(R.id.buttonSubDelete);

        databaseHelper = new DatabaseHelper(this);

        description = findViewById(R.id.editTextSubDescription);
        date = findViewById(R.id.editTextSubDate);
        switchFinished = findViewById(R.id.switchFinished);
        title = findViewById(R.id.editTextSubTitle);

        // enlever le clavier
        description.setInputType(InputType.TYPE_NULL);
        date.setInputType(InputType.TYPE_NULL);


        sessionTitle = getIntent().getStringExtra(databaseHelper.COL_1_EA);
        sessionDescription  = getIntent().getStringExtra(databaseHelper.COL_2_EA);

        toastMessage(sessionDescription);
        title.setText(sessionTitle);
        description.setText(sessionDescription);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finished = switchFinished.isChecked();
                AddData(sessionTitle, description.getText().toString(), date.getText().toString(), finished);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditActivity.listDataContent = null;
                //EditActivity.listViewSubTask.setAdapter(null);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteData(sessionTitle,description.getText().toString() );
                finish();
            }
        });
    }

    public void  AddData(String title, String description, String dateFin, boolean finished){
        MainActivity.databaseHelper.addDataEditActivity(title, description, dateFin, finished);
    }

    public void DeleteData(String title, String description){
        MainActivity.databaseHelper.deleteDataEditActivity(title, description);
    }



    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
