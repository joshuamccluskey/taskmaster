package com.joshuamccluskey.taskmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.util.DateUtils;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.StateEnum;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.joshuamccluskey.taskmaster.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AddTaskActivity extends AppCompatActivity {
    public static final String TAG = "ADD TASK";
    Spinner teamSpinner = null;
    Spinner statusSpinner = null;
    List<String> teamNames = new  ArrayList<>();
    List<Team> teamList = new  ArrayList<>();
    CompletableFuture<List<Team>> teamFuture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        TextView submittedText = findViewById(R.id.submittedText);

        teamFuture = new CompletableFuture<>();
        statusSpinner =  findViewById(R.id.statusSpinner);
        statusSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                StateEnum.values()));

        teamSpinner =  findViewById(R.id.teamSpinner);



        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Task successfully created");


                    for (Team databaseTeam :success.getData()) {
                        teamList.add(databaseTeam);
                        teamNames.add(databaseTeam.getTeamName());
                    }
                    teamFuture.complete(teamList);
                    runOnUiThread(() -> teamSpinner.setAdapter(new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        teamNames)));

                },
                failure -> {
                    teamFuture.complete(null);
                    Log.i(TAG, "Task creation failed ");

                }
        );




        Button submitTaskButton = findViewById (R.id.editSaveTaskButton);
        submitTaskButton.setOnClickListener(view -> {
            String title = ((EditText)findViewById(R.id.taskTitleEditText)).getText().toString();
            String body = ((EditText)findViewById(R.id.doSomethingEditText)).getText().toString();
            String currentDate = DateUtils.formatISO8601Date(new Date());
            String selectedTeamString = teamSpinner.getSelectedItem().toString();
            Team selectedTeam = teamList.stream().filter(team -> team.getTeamName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);

            Task newTask  = Task.builder()
                    .title(title)
                    .body(body)
                    .state((StateEnum)statusSpinner.getSelectedItem())
                    .date(new Temporal.DateTime(currentDate))
                    .team(selectedTeam)
                    .build();

    Amplify.API.mutate(
            ModelMutation.create(newTask),
            successResponse -> Log.i(TAG, "AddTaskActivity.onClick: made a Task"),
            failureResponse -> Log.i(TAG, "AddTaskActivity.onClick: failed" + failureResponse)
    );

            submittedText.setVisibility(View.VISIBLE);
            Intent goToAllTasksIntent = new Intent(AddTaskActivity.this, MyTasksActivity.class);

            startActivity(goToAllTasksIntent);




        });


    }

}
