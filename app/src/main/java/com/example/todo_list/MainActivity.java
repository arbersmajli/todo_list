package com.example.todo_list;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final List<String> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = findViewById(R.id.listView);
        final TextAdapter adapter = new TextAdapter();

        adapter.setData(list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Voulez-vous supprimer cette tâche ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                adapter.setData(list);
                            }
                        } )
                        .setNegativeButton("Non", null)
                        .create();
                dialog.show();
            }
        });

        final Button newTaskButton = findViewById(R.id.newTaskButton);

        newTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final EditText taskInput = new EditText(MainActivity.this);
                taskInput.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Ajouter une nouvelle tâche")
                        .setMessage("Quelle est vôtre nouvelle tâche ?")
                        .setView(taskInput)
                        .setPositiveButton("Ajouter la tâche", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.add(taskInput.getText().toString());
                                adapter.setData(list);
                            }
                        })
                        .setNegativeButton("Annuler", null)
                        .create();
                dialog.show();
            }
        });
    }

    class TextAdapter extends BaseAdapter{

        List<String> list = new ArrayList<>();

        void setData(List<String> mList){
            list.clear();
            list.addAll(mList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount(){
            return list.size();
        }

        @Override
        public Object getItem(int position){
            return null;
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.item , parent,false);
            TextView textView = rowView.findViewById(R.id.task);
            textView.setText(list.get(position));
            return rowView;
        }



    }

}
