package com.joshuamccluskey.taskmaster.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Insert;
import androidx.room.Room;

import com.joshuamccluskey.taskmaster.R;
import com.joshuamccluskey.taskmaster.adapter.MyTasksListRecyclerViewAdapter;
import com.joshuamccluskey.taskmaster.database.TaskMasterDatabase;
import com.joshuamccluskey.taskmaster.model.StateEnum;
import com.joshuamccluskey.taskmaster.model.Task;


import java.util.Date;
import java.util.List;

public class MyTasksActivity extends AppCompatActivity {

    public final String TAG = "MainActivity";
    public static String TASK_DETAIL_TITLE_TAG = "TASK DETAIL TITLE";
    public static String TASK_DETAIL_BODY_TAG = "TASK BODY";
    public static String TASK_DETAIL_STATE_TAG = "TASK STATE";
    SharedPreferences userPreferences;
    MyTasksListRecyclerViewAdapter myTasksListRecyclerViewAdapter;
    List<Task> tasksList = null;
    TaskMasterDatabase taskMasterDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks);
        userPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        taskMasterDatabase = Room.databaseBuilder(
                getApplicationContext(),
                TaskMasterDatabase.class,
                "josh_task_master")
                .allowMainThreadQueries()  // Caution don't use in a real app
                .fallbackToDestructiveMigration()
                .build();
        tasksList = taskMasterDatabase.taskDao().findAll();

        addTaskButtonSetUp();
        allTasksButtonSetUp();
        settingsImageButtonSetUp();
        myTasksListRecycleViewSetUp();


    }


        // onResume needs to be outside of on Create all methods taken out of onCreate
        @Override
        public void onResume(){
            super.onResume();

            String username = userPreferences.getString(SettingsActivity.USERNAME_TAG, "No Username");
            ((TextView)findViewById(R.id.usernameTextView)).setText(getString(R.string.username_username, username));
            tasksList = taskMasterDatabase.taskDao().findAll();
            myTasksListRecycleViewSetUp();
        }

        public void addTaskButtonSetUp(){
            Button addTaskButton = findViewById(R.id.goToAddTaskButton);
            addTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Add Task Button!");
                    Log.e(TAG, "onClick: Add Task Button!");
                    Intent goToAddTaskIntent = new Intent(MyTasksActivity.this, AddTaskActivity.class);
                    MyTasksActivity.this.startActivity(goToAddTaskIntent);
                }
            });
        }

        public void allTasksButtonSetUp() {

            Button allTasksButton = findViewById(R.id.allTasksButton);
            allTasksButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("All Tasks Button!");
                    Log.e(TAG, "onClick: All Tasks Button!");
                    Intent goToAllTasksIntent = new Intent(MyTasksActivity.this, AllTasksActivity.class);
                    startActivity(goToAllTasksIntent);
                }
            });
        }

        public void settingsImageButtonSetUp() {
            ImageButton settingsImageButton = findViewById(R.id.settingsImageButton);
            settingsImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Settings Image Button!");
                    Log.e(TAG, "onClick: Settings Image Button!");
                    Intent goToSettingsIntent = new Intent(MyTasksActivity.this, SettingsActivity.class);
                    startActivity(goToSettingsIntent);
                }
            });
        }


        public void myTasksListRecycleViewSetUp(){
            RecyclerView myTasksListRecycleView = findViewById(R.id.tasksListRecycleView);

            RecyclerView.LayoutManager taskLayoutManager =  new LinearLayoutManager(this);

            myTasksListRecycleView.setLayoutManager(taskLayoutManager);

            myTasksListRecyclerViewAdapter = new MyTasksListRecyclerViewAdapter(tasksList, this);

            myTasksListRecycleView.setAdapter(myTasksListRecyclerViewAdapter);
        }
    }
