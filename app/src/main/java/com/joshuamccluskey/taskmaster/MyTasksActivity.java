package com.joshuamccluskey.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MyTasksActivity extends AppCompatActivity {

    public final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks);

        Button addTaskButton = findViewById(R.id.submitTaskButton);
        Button addAllTasksButton = findViewById(R.id.allTasksButton);
        addTaskButton.setOnClickListener(view -> {
            System.out.println("Add Task Button!");
            Log.e(TAG, "onClick: Add Task Button!");
            Intent goToAddTaskIntent = new Intent(MyTasksActivity.this, AddTaskActivity.class);
            MyTasksActivity.this.startActivity(goToAddTaskIntent);
        });
        addAllTasksButton.setOnClickListener(view -> {
            System.out.println("All Tasks Button!");
            Log.e(TAG, "onClick: All Tasks Button!");
            Intent goToAllTasksIntent = new Intent(MyTasksActivity.this, AddTaskActivity.class);
            startActivity(goToAllTasksIntent);
        });
    }
}