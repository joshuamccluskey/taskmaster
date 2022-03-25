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

import com.joshuamccluskey.taskmaster.R;
import com.joshuamccluskey.taskmaster.adapter.MyTasksListRecyclerViewAdapter;
import com.joshuamccluskey.taskmaster.model.StateEnum;
import com.joshuamccluskey.taskmaster.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyTasksActivity extends AppCompatActivity {

    public final String TAG = "MainActivity";
    public static String TASK_DETAIL_TITLE_TAG = "TASK DETAIL TITLE";
    SharedPreferences userPreferences;
    MyTasksListRecyclerViewAdapter myTasksListRecyclerViewAdapter;
    List<Task> tasksList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks);
        userPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        addTaskButtonSetUp();
        allTasksButtonSetUp();
        settingsImageButtonSetUp();
//        doTaxesButtonSetUp();
//        groceriesButtonSetUp();
//        dogFoodButtonSetUp();
        myTasksListRecycleViewSetUp();


    }


        // onResume needs to be outside of on Create all methods taken out of onCreate
        @Override
        public void onResume(){
            super.onResume();

            String username = userPreferences.getString(SettingsActivity.USERNAME_TAG, "No Username");
            ((TextView)findViewById(R.id.usernameTextView)).setText(getString(R.string.username_username, username));
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

//        public void doTaxesButtonSetUp(){
//            Button doTaxesButton = findViewById(R.id.doTaxesButton);
//            doTaxesButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    System.out.println("Do Taxes Button!");
//                    Log.e(TAG, "onClick: Do Taxes Button!");
//                    Intent goToTaskDetails = new Intent(MyTasksActivity.this, TaskDetailActivity.class);
//                    startActivity(goToTaskDetails);
//                    String taskTitle = "Do Taxes";
//                    SharedPreferences.Editor userPreferencesEditor = userPreferences.edit();
//                    userPreferencesEditor.putString(TASK_DETAIL_TITLE_TAG, taskTitle);
//                    userPreferencesEditor.apply();
//                }
//            });
//        }

//        public void groceriesButtonSetUp(){
//            Button groceriesButton = findViewById(R.id.groceriesButton);
//            groceriesButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    System.out.println("Groceries Button!");
//                    Log.e(TAG, "onClick: Groceries Button!");
//                    Intent goToTaskDetails = new Intent(MyTasksActivity.this, TaskDetailActivity.class);
//                    startActivity(goToTaskDetails);
//                    String taskTitle = "Groceries";
//                    SharedPreferences.Editor userPreferencesEditor = userPreferences.edit();
//                    userPreferencesEditor.putString(TASK_DETAIL_TITLE_TAG, taskTitle);
//                    userPreferencesEditor.apply();
//                }
//            });
//        }
//        public void dogFoodButtonSetUp(){
//            Button dogFoodButton = findViewById(R.id.dogFoodButton);
//            dogFoodButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    System.out.println("Dog Food Button!");
//                    Log.e(TAG, "onClick: Dog Food Button!");
//                    Intent goToTaskDetails = new Intent(MyTasksActivity.this, TaskDetailActivity.class);
//                    startActivity(goToTaskDetails);
//                    String taskTitle = "Dog Food";
//                    SharedPreferences.Editor userPreferencesEditor = userPreferences.edit();
//                    userPreferencesEditor.putString(TASK_DETAIL_TITLE_TAG, taskTitle);
//                    userPreferencesEditor.apply();
//                }
//            });
//        }

        public void myTasksListRecycleViewSetUp(){
            RecyclerView myTasksListRecycleView = findViewById(R.id.tasksListRecycleView);

            RecyclerView.LayoutManager taskLayoutManager =  new LinearLayoutManager(this);

            myTasksListRecycleView.setLayoutManager(taskLayoutManager);

            tasksList.add(new Task("Do Taxes", "Do this weekend", StateEnum.NEW, new Date()));
            tasksList.add(new Task("Groceries", "See Trello List For Snacks", StateEnum.NEW, new Date()));
            tasksList.add(new Task("Dog Food", "Don't get whole grain", StateEnum.NEW, new Date()));
            tasksList.add(new Task("Give Puppy Bath", "Don't use hot water bad for fur and skin", StateEnum.NEW, new Date()));
            tasksList.add(new Task("Code Challenge", "Time box for 1 hour", StateEnum.NEW, new Date()));
            tasksList.add(new Task("Learning Journal", "Don't Forget before signing off for the day", StateEnum.NEW, new Date()));

            myTasksListRecyclerViewAdapter = new MyTasksListRecyclerViewAdapter(tasksList, this);

            myTasksListRecycleView.setAdapter(myTasksListRecyclerViewAdapter);
        }
    }
