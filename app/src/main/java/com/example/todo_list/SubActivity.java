package com.example.todo_list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class SubActivity extends AppCompatActivity {

    private Button add, cancel, delete, uploadImageButton;
    private EditText title, description, date;
    private Switch switchFinished;
    private boolean finished, sessionNewSubTask;
    public static DatabaseHelper databaseHelper;
    String sessionTitle, sessionDescription, regexDate;
    int sessionIdTask, sessionIdSubTask;
    public LinearLayout linearLayoutSwitch;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERSMISSION_CODE = 1001;

    boolean withImage = false;

    Uri imageURI;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        add = findViewById(R.id.buttonSubAdd);
        cancel = findViewById(R.id.buttonSubCancel);
        delete = findViewById(R.id.buttonSubDelete);
        uploadImageButton = findViewById(R.id.uploadImage);

        databaseHelper = new DatabaseHelper(this);

        description = findViewById(R.id.editTextSubDescription);
        date = findViewById(R.id.editTextSubDate);
        switchFinished = findViewById(R.id.switchFinished);
        title = findViewById(R.id.editTextSubTitle);
        imageView = findViewById(R.id.image);

        linearLayoutSwitch =  findViewById(R.id.switchFinishedLayout);

        // enlever le clavier
        description.setInputType(InputType.TYPE_NULL);
        date.setInputType(InputType.TYPE_NULL);
        regexDate = "^([0-2][0-9]|(3)[0-1])(\\.)(((0)[0-9])|((1)[0-2]))(\\.)\\d{4}$";

        sessionIdTask = getIntent().getIntExtra(databaseHelper.COL_0_EA,-1);
        sessionIdSubTask = getIntent().getIntExtra(databaseHelper.COL_1_EA,-1);
        sessionNewSubTask = getIntent().getBooleanExtra("newSubTask", false);


        if(!sessionNewSubTask) {
            populateDataSubActivity(sessionIdTask, sessionIdSubTask);
            add.setText("Update");
        }else{
            getTitleById(sessionIdTask);
            linearLayoutSwitch.setVisibility(View.GONE);
        }
        imageURI = null;

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withImage = true;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERSMISSION_CODE);
                    }else{
                        // permission already granted
                        pickImageFromGallery();
                    }
                }else{
                    // system OS is less than marshmallow
                    pickImageFromGallery();
                }
            }
        });



        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                finished = switchFinished.isChecked();
                if(Pattern.matches(regexDate, date.getText().toString())){
                    String link_picture = "";
                    if(withImage){
                        link_picture = imageURI.getPath();
                    }
                    if (sessionNewSubTask) {
                        AddData(sessionIdTask, description.getText().toString(), date.getText().toString(), finished, link_picture);
                    } else {
                        UpdateData(sessionIdTask, sessionIdSubTask, description.getText().toString(), date.getText().toString(), finished, link_picture);
                    }
                    finish();
                }else{
                    ShapeDrawable shape = new ShapeDrawable(new RectShape());
                    shape.getPaint().setColor(Color.RED);
                    shape.getPaint().setStyle(Paint.Style.STROKE);
                    shape.getPaint().setStrokeWidth(3);
                    date.setBackground(shape);
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteData(sessionIdTask, sessionIdSubTask);
                finish();
            }
        });


        if(switchFinished.isChecked()){
            DeleteData(sessionIdTask, sessionIdSubTask);
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case PERSMISSION_CODE : {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }else{
                    toastMessage("Permission denied !");
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageView.setImageURI(data.getData());
            imageURI = data.getData();
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

        if(data.moveToNext()){
            getTitleById(data.getInt(1));
            description.setText(data.getString(2));
            date.setText(data.getString(3));
            //toastMessage(data.getString(5));
            Drawable drawable  = Drawable.createFromPath(data.getString(5));
            // imageView.setImageURI(myUri);

            if(data.getInt(4) == 0){
                switchFinished.setChecked(false);
            }else{
                switchFinished.setChecked(true);
            }
        }
    }

    public void  AddData(int idTask, String description, String dateFin, boolean finished, String link_picture){
        MainActivity.databaseHelper.addDataEditActivity(idTask, description, dateFin, finished, link_picture);
    }

    public void UpdateData(int idTask, int idSubTask, String description, String dateFin, boolean finished, String link_picture){
        MainActivity.databaseHelper.updateData(idTask, idSubTask, description, dateFin, finished, link_picture);
    }

    public void DeleteData(int idTask, int idSubTask){
        MainActivity.databaseHelper.deleteDataEditActivity(idTask, idSubTask);
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
