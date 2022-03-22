package com.joshuamccluskey.taskmaster;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Button submitTaskButton = findViewById (R.id.submitTaskButton);
        TextView submittedText = findViewById(R.id.submittedText);

        submitTaskButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                submittedText.setVisibility(View.VISIBLE);

            }
        });


    }

}
