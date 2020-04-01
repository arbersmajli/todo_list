package com.example.todo_list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SubActivity extends AppCompatActivity {

    private Button add, cancel, delete;
    private EditText description, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        add = findViewById(R.id.buttonSubAdd);
        cancel = findViewById(R.id.buttonSubCancel);
        delete = findViewById(R.id.buttonSubDelete);
        description = findViewById(R.id.editTextSubDescription);
        date = findViewById(R.id.editTextSubDate);

        description.setInputType(InputType.TYPE_NULL);
        date.setInputType(InputType.TYPE_NULL);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
