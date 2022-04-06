package com.joshuamccluskey.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import com.joshuamccluskey.taskmaster.R;

public class TaskDetailActivity extends AppCompatActivity {
    SharedPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        userPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }
    @SuppressLint("StringFormatInvalid")
    @Override
    public void onResume(){
        super.onResume();
        Intent callingIntent = getIntent();
        String taskTitle = callingIntent.getStringExtra(MyTasksActivity.TASK_DETAIL_TITLE_TAG);
        String taskBody = callingIntent.getStringExtra(MyTasksActivity.TASK_DETAIL_BODY_TAG);
        String taskState = callingIntent.getStringExtra(MyTasksActivity.TASK_DETAIL_STATE_TAG);
        ((TextView)findViewById(R.id.titleTaskDetailTextView)).setText(getString(R.string.task_title, taskTitle));
        ((TextView)findViewById(R.id.taskBodyTextView)).setText(getString(R.string.task_body, taskBody));
        ((TextView)findViewById(R.id.taskStateTextView)).setText(getString(R.string.task_state, taskState));
        Button editTaskButton = findViewById(R.id.editTaskButton);
        editTaskButton.setOnClickListener(view -> {
            Intent goToEditTaskActivity = new Intent(TaskDetailActivity.this, EditTaskActivity.class);
            TaskDetailActivity.this.startActivity(goToEditTaskActivity);
                });
    }

}