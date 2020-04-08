package com.example.todo_list;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SubActivity extends AppCompatActivity {

    private Button add, cancel, delete;
    private EditText title, description, date;
    private Switch switchFinished;
    private boolean finished, sessionNewSubTask;
    public static DatabaseHelper databaseHelper;
    String sessionTitle, sessionDescription, regexDate;
    int sessionIdTask, sessionIdSubTask;

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
        regexDate = "^([0-2][0-9]|(3)[0-1])(\\.)(((0)[0-9])|((1)[0-2]))(\\.)\\d{4}$";


        //sessionTitle = getIntent().getStringExtra(databaseHelper.COL_1_EA);
        //sessionDescription  = getIntent().getStringExtra(databaseHelper.COL_2_EA);
        sessionIdTask = getIntent().getIntExtra(databaseHelper.COL_0_EA,-1);
        sessionIdSubTask = getIntent().getIntExtra(databaseHelper.COL_1_EA,-1);
        sessionNewSubTask = getIntent().getBooleanExtra("newSubTask", false);



        if(!sessionNewSubTask) {
            //toastMessage("task : " + sessionIdTask + ", subTask : " + sessionIdSubTask);
            populateDataSubActivity(sessionIdTask, sessionIdSubTask);
            add.setText("Update");
        }else{
            getTitleById(sessionIdTask);
        }

        //toastMessage(databaseHelper.COL_0_EA +" : " +sessionIdTask + ", " + databaseHelper.COL_1_EA + " : " + sessionIdSubTask);
        //title.setText(sessionTitle);
        //description.setText(sessionDescription);


        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                finished = switchFinished.isChecked();
                if(Pattern.matches(regexDate, date.getText().toString())){
                    if (sessionNewSubTask) {
                        AddData(sessionIdTask, description.getText().toString(), date.getText().toString(), finished);
                    } else {
                        UpdateData(sessionIdTask, sessionIdSubTask, description.getText().toString(), date.getText().toString(), finished);
                    }
                }else{
                    ShapeDrawable shape = new ShapeDrawable(new RectShape());
                    shape.getPaint().setColor(Color.RED);
                    shape.getPaint().setStyle(Paint.Style.STROKE);
                    shape.getPaint().setStrokeWidth(3);
                    date.setBackground(shape);
                }

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
                //DeleteData(sessionIdTask);
                //toastMessage(sessionIdTask + ", " + sessionIdSubTask);
                DeleteData(sessionIdTask, sessionIdSubTask);
                finish();
            }
        });


        if(switchFinished.isChecked()){
            DeleteData(sessionIdTask, sessionIdSubTask);
        }
    }
    public void getTitleById(int id){
        Cursor data = databaseHelper.getData(databaseHelper.TABLE_NAME_MAIN_ACTIVITY,databaseHelper.COL_0_MA, id);

        if(data.moveToNext()){
            title.setText(data.getString(1));
        }
    }

    public void populateDataSubActivity(int idTask, int idSubTask){
        Cursor data = databaseHelper.getData(databaseHelper.TABLE_TASK_EDIT_ACTIVITY, databaseHelper.COL_0_EA, databaseHelper.COL_1_EA, idTask, idSubTask);
        //Cursor dataJoin = databaseHelper.getData(databaseHelper.TABLE_NAME_MAIN_ACTIVITY, id);

        if(data.moveToNext()){
            //getTitleById(id);
            getTitleById(data.getInt(1));
            description.setText(data.getString(2));
            date.setText(data.getString(3));
            if(data.getInt(4) == 0){
                switchFinished.setChecked(false);
            }else{
                switchFinished.setChecked(true);
            }
        }
    }

    public void  AddData(int idTask, String description, String dateFin, boolean finished){
        MainActivity.databaseHelper.addDataEditActivity(idTask, description, dateFin, finished);
    }

    public void UpdateData(int idTask, int idSubTask, String description, String dateFin, boolean finished){
        MainActivity.databaseHelper.updateData(idTask, idSubTask, description, dateFin, finished);
    }


    public void DeleteData(int idTask, int idSubTask){
        MainActivity.databaseHelper.deleteDataEditActivity(idTask, idSubTask);
    }



    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
