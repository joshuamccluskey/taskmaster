package com.joshuamccluskey.taskmaster.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.StateEnum;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.joshuamccluskey.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class EditTaskActivity extends AppCompatActivity {
    public static String TAG = "editTaskActivity";
    Task taskToEdit;
    CompletableFuture<Task> taskCompletableFuture;
    Spinner editStatusSpinner = null;
    Spinner editTeamSpinner = null;
    CompletableFuture<List<Team>> teamListFuture = null;
    EditText editTaskNameEditText;
    EditText editDescriptionEditText;
    ActivityResultLauncher<Intent> activityResultLauncher;
    SharedPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        taskCompletableFuture = new CompletableFuture<>();
        teamListFuture = new CompletableFuture<>();
        elementsSetUp();
        editSaveButtonSetup();

//        Intent gettingIntent = getIntent();
//        if((gettingIntent != null) && (gettingIntent.getType() != null) && (gettingIntent.getType().startsWith("image")))



    }

 public void elementsSetUp(){
     Intent gettingIntent = getIntent();
     String taskId = null;
     if (gettingIntent != null){
         taskId = gettingIntent.getStringExtra(MyTasksActivity.TASK_ID_TAG);
     }
     String taskId2 = taskId;

     Amplify.API.query(
             ModelQuery.list(Task.class),
             success -> {
                 Log.i(TAG, "elementsSetUp: everything is good");
                 boolean taskFound = false;
                 for (Task databaseTask : success.getData()){
                     if (databaseTask.getId().equals(taskId2)){
                         taskCompletableFuture.complete(databaseTask);
                         taskFound = true;
                     }
                 }
                if (!taskFound){
                    taskCompletableFuture.complete(null);
                }

             },
             failure -> {
                 Log.i(TAG, "elementsSetUp: somehting went wrong can't get task", failure);
             }
     );
    try {
        taskToEdit = taskCompletableFuture.get();
    }catch (InterruptedException interruptedException) {
        Log.e(TAG, "elementsSetUp: There is an error ", interruptedException);
        Thread.currentThread().interrupt();
    }catch (ExecutionException executionException) {
        Log.e(TAG, "elementsSetUp: There is an error ", executionException );
    }
    if(taskId != null){
        editTaskNameEditText = ((EditText) findViewById(R.id.editTaskNameEditText));
        editTaskNameEditText.setText(taskToEdit.getTitle());
        editDescriptionEditText = ((EditText) findViewById(R.id.editDescriptionEditText));
        editDescriptionEditText.setText(taskToEdit.getBody());
        editSpinnerSetup();
    }




   }

    public void editSpinnerSetup(){
        editTeamSpinner =  findViewById(R.id.editTeamSpinner);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Spinner Info Retrieved");
                    List<String> teamNames = new  ArrayList<>();
                    List<Team> teamList = new  ArrayList<>();
                    for (Team team :success.getData()) {
                        teamList.add(team);
                        teamNames.add(team.getTeamName());
                    }
                    teamListFuture.complete(teamList);
                    runOnUiThread(() -> {
                        editTeamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                teamNames));
                        editTeamSpinner.setSelection(getSpinnerIndex(editTeamSpinner, taskToEdit.getTeam().getTeamName()));
                    });


                },
                failure -> {
                    teamListFuture.complete(null);
                    Log.i(TAG, "Spinner info not retrieved ");

                }
        );

        editStatusSpinner = findViewById(R.id.editStatusSpinner);
        editStatusSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                StateEnum.values()));
        editStatusSpinner.setSelection(getSpinnerIndex(editStatusSpinner, taskToEdit.getState().toString()));
    }
    public void editSaveButtonSetup(){
        Button editSaveTaskButton = findViewById(R.id.editSaveTaskButton);
        editSaveTaskButton.setOnClickListener(view -> {
            saveTask();
        });
    }

    public void saveTask() {
        List<Team> teamList = null;
        String getTeamToSave = editTeamSpinner.getSelectedItem().toString();
        try {
            teamList = teamListFuture.get();
        }catch (InterruptedException interruptedException) {
            Log.e(TAG, "saveTask: Didn't work task not retrieved",interruptedException);
            Thread.currentThread().interrupt();
        } catch (ExecutionException executionException){
            Log.e(TAG, "saveTask: Didn't work task encountered execution exception", executionException);
        }
        Team teamToSave = teamList.stream().filter(team -> team.getTeamName().equals(getTeamToSave)).findAny().orElseThrow(RuntimeException::new);
        Task taskToSave = Task.builder()
                .title(editTaskNameEditText.getText().toString())
                .id(taskToEdit.getId())
                .body(editDescriptionEditText.getText().toString())
                .state(taskStateString(editStatusSpinner.getSelectedItem().toString()))
                .team(teamToSave)
                .build();
        
        Amplify.API.mutate(
                ModelMutation.update(taskToSave),
                success -> {
                    Log.i(TAG, "saveTask: updated task success!" + success);
                    Snackbar.make(findViewById(R.id.editTaskActivity), "Product saved!", Snackbar.LENGTH_SHORT).show();
                },
                failure -> {
                    Log.i(TAG, "saveTask: your task didin't update" + failure);
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