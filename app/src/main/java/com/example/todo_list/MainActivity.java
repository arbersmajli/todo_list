package com.example.todo_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private static final String TAG = "MainActivity";
    public static DatabaseHelper databaseHelper;
    private static ListView listView; // affichage user
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


        createNotificationChannel();

        listView = findViewById(R.id.listView);
        searchEditText = findViewById(R.id.searchTask);
        databaseHelper = new DatabaseHelper(this);
        buttonSearch = findViewById(R.id.searchTaskButton);
        floatingActionButton = findViewById(R.id.floating_button);
        final String contentEditText = searchEditText.getText().toString();
        final Intent intentEditActivity = new Intent(this, EditActivity.class);

        searchEditText.setInputType(InputType.TYPE_NULL);
        notificationByTask();

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
                intentEditActivity.putExtra("sessionNewTask", newTask);
                intentEditActivity.putExtra("sessionId", idPosition);
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
                    sendNotification("b", "ser");
                }
            }
        });

        populateListView(DatabaseHelper.TABLE_NAME_MAIN_ACTIVITY,"");

    }

    @Override
    public void onRestart(){
        super.onRestart();
        listDataContent.clear();
        notificationByTask();
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


        if(listDataContent.isEmpty()){
            buttonSearch.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            searchEditText.setVisibility(View.GONE);
        }else{
            buttonSearch.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            searchEditText.setVisibility(View.VISIBLE);
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listDataContent);
        listView.setAdapter(adapter);
        data.close();
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void sendNotification(String title, String description){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "667")
                .setSmallIcon(android.R.drawable.alert_dark_frame)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(667, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("667", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void notificationByTask(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Cursor data = databaseHelper.getData(databaseHelper.TABLE_TASK_EDIT_ACTIVITY, databaseHelper.COL_0_EA, -1);
        String date = null;

        while(data.moveToNext()){
            date = data.getString(3);
            if(date.equals(simpleDateFormat.format(currentTime))){
                sendNotification(data.getString(2), data.getString(3) + "; " + data.getString(2));
            }
        }

    }



}
