package com.joshuamccluskey.taskmaster.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.StateEnum;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.joshuamccluskey.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EditTaskActivity extends AppCompatActivity {
    String TAG = "editTaskActivity";
    Task taskToEdit = null;
    CompletableFuture<Task> taskCompletableFuture = null;
    Spinner editStatusSpinner = null;
    Spinner editTeamSpinner = null;
    CompletableFuture<List<Team>> teamListFuture = null;
    EditText editTaskNameEditText;
    EditText editDescriptionEditText;
    List<Team> teamList = new  ArrayList<>();
    List<String> teamNames = new  ArrayList<>();
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
    }

//    public void setupEditTaskData(){
//        Intent
//    }

    public void setupEditSpinners(){
        editStatusSpinner = findViewById(R.id.editStatusSpinner);

        teamListFuture = new CompletableFuture<>();
        editStatusSpinner =  findViewById(R.id.editStatusSpinner);
        editStatusSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                StateEnum.values()));

        editTeamSpinner =  findViewById(R.id.editTeamSpinner);

        editStatusSpinner.setSelection(getSpinnerIndex(editStatusSpinner, taskToEdit.getState().toString()));



        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Spinner Info Retrieved");


                    for (Team databaseTeam :success.getData()) {
                        teamList.add(databaseTeam);
                        teamNames.add(databaseTeam.getTeamName());
                    }
                    teamListFuture.complete(teamList);
                    runOnUiThread(() -> editTeamSpinner.setAdapter(new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            teamNames)));

                },
                failure -> {
                    teamListFuture.complete(null);
                    Log.i(TAG, "Spinner info not retrieved ");

                }
        );


    }



    public int getSpinnerIndex(Spinner editStatusSpinner, String stringCheck){
        for (int i = 0;i < editStatusSpinner.getCount(); i++){
            if (editStatusSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(stringCheck)){
                return i;
            }
        }

        return 0;
    }

    public static StateEnum taskStateString(String stringStateStatus) {
        for (StateEnum stateEnum : StateEnum.values()) {
            if (stateEnum.toString().equals(stringStateStatus)) {
                return stateEnum;
            }
        }
        return null;
    }
}