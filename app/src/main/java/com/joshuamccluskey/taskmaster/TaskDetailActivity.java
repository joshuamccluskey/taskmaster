package com.joshuamccluskey.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {
    SharedPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        userPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }
    @Override
    public void onResume(){
        super.onResume();

        String taskTitle = userPreferences.getString(MyTasksActivity.DO_TAXES_TAG, "No Task");
        ((TextView)findViewById(R.id.titleTaskDetailTextView)).setText(getString(R.string.task_title, taskTitle));
    }

}