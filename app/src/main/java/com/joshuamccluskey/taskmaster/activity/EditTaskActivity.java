package com.joshuamccluskey.taskmaster.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.StateEnum;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.joshuamccluskey.taskmaster.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    private MediaPlayer mp = null;

    String imageS3Key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        taskCompletableFuture = new CompletableFuture<>();
        teamListFuture = new CompletableFuture<>();
        activityResultLauncher = getImgActivityResultLauncher();
        mp = new MediaPlayer();
        readerButtonSetUp();
        elementsSetUp();
        addImgButtonSetup();
        deleteImgButtonSetup();
        editSaveButtonSetup();
        deleteButtonSetup();




    }

    public void readerButtonSetUp(){
        Button readerButton = findViewById(R.id.readerButton);
        readerButton.setOnClickListener(r -> {
            String taskName = editTaskNameEditText.getText().toString();
            Amplify.Predictions.convertTextToSpeech(
                    taskName,
                    result -> playAudio(result.getAudioData()),
                    error -> Log.e(TAG, "Conversion failed", error)
            );
        });
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
            editTaskNameEditText = (findViewById(R.id.editTaskNameEditText));
            editTaskNameEditText.setText(taskToEdit.getTitle());
            editDescriptionEditText = (findViewById(R.id.editDescriptionEditText));
            editDescriptionEditText.setText(taskToEdit.getBody());


            imageS3Key = taskToEdit.getTaskImgS3Key();
        }
        if(imageS3Key != null && !imageS3Key.isEmpty()) {
            Amplify.Storage.downloadFile(
                    imageS3Key,
                    new File(getApplication().getFilesDir(), imageS3Key),
                    good -> {
                        runOnUiThread(()-> {
                            ImageView taskImageView = findViewById(R.id.taskImageView);
                            taskImageView.setImageBitmap(BitmapFactory.decodeFile(good.getFile().getPath()));
                        });
                    },
                    bad -> {
                        Log.e(TAG, "elementsSetUp: something went wrong with the S3 key for the image " + bad.getMessage());
                    });
        }
        if ( taskId != null){
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
    public ActivityResultLauncher<Intent> getImgActivityResultLauncher() {
        ActivityResultLauncher<Intent> imgActivityResultLauncher =
                registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if (result.getData() != null) {
                                Uri pickedImgFileUri = result.getData().getData();
                                try {
                                    InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImgFileUri);
                                    String pickedImgFilename = getFileNameFromUri(pickedImgFileUri);
                                    Log.i(TAG, "onActivityResult: Success on image input" + pickedImgFilename);
                                    uploadInputStreamToS3(pickedImageInputStream, pickedImgFilename, pickedImgFileUri);
                                } catch (FileNotFoundException fileNotFoundException) {
                                    Log.e(TAG, "onActivityResult: There was an error uploading an image", fileNotFoundException);
                                }
                            }
                        } else {
                            Log.e(TAG, "onActivityResult: There is an error in ActivityLauncher.onActivityResult");
                        }
                    }
                });
        return imgActivityResultLauncher;
    }

    public void uploadInputStreamToS3 (InputStream pickedImageInputStream, String pickedImgFilename, Uri pickedImgFileUri){
            Amplify.Storage.uploadInputStream(
                    pickedImgFilename,
                    pickedImageInputStream,
                    success ->
                    {
                        Log.i(TAG, "uploadInputStreamToS3: Upload was successful");
                        imageS3Key = success.getKey();
                        saveTask();
                        ImageView taskImageView = findViewById(R.id.taskImageView);
                        InputStream pickedImgInputStreamCopy = null;
                        try {
                            pickedImgInputStreamCopy = getContentResolver().openInputStream(pickedImgFileUri);
                        } catch (FileNotFoundException fileNotFoundException){
                            Log.e(TAG, "uploadInputStreamToS3: Couldn't receive file uri ",fileNotFoundException );
                        }
                        taskImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImgInputStreamCopy));
                    },
                    failure ->
                    {
                        Log.e(TAG, "uploadInputStreamToS3: There was an error uploading file" + failure.getMessage());
                    }
            );
    }

    public void addImgButtonSetup(){
        Button addImgButton = findViewById(R.id.editAddImgButton);
        addImgButton.setOnClickListener(view -> {
            launchImgSelection();
        });
    }

    public void deleteImgButtonSetup(){
        Button deleteImgButton = findViewById(R.id.editDeleteImgButton);
        deleteImgButton.setOnClickListener(view -> {
            deleteImageFromS3();

        });

    }

    public void deleteImageFromS3(){
        if (imageS3Key.isEmpty()){
            Amplify.Storage.remove(
                    imageS3Key,
                    good ->
                    {
                        imageS3Key = "";
                        saveTask();
                        ImageView taskImageView = findViewById(R.id.taskImageView);
                        taskImageView.setImageResource(android.R.color.transparent);
                        Log.i(TAG, "deleteImageFromS3: success" + good.getKey());
                    },
                    bad ->{
                        Log.e(TAG, "deleteImageFromS3: failed " + bad.getMessage());
                    }
            );
        }

    }

    public void launchImgSelection(){
        Intent imgFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imgFileIntent.setType("*/*");
        imgFileIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});
        activityResultLauncher.launch(imgFileIntent);
    }

    // StackOverflow https://stackoverflow.com/a/25005243/16889809
    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    public void editSaveButtonSetup(){
        Button editSaveTaskButton = findViewById(R.id.editSaveTaskButton);
        editSaveTaskButton.setOnClickListener(view -> {
            saveTask();
            Intent goToMyTasksActivity = new Intent(EditTaskActivity.this, MyTasksActivity.class);
            startActivity(goToMyTasksActivity);
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
                .taskImgS3Key(imageS3Key)
                .build();
        
        Amplify.API.mutate(
                ModelMutation.update(taskToSave),
                success -> {
                    Log.i(TAG, "saveTask: updated task success!" + success);
                    Snackbar.make(findViewById(R.id.editTaskActivity), "Task saved!", Snackbar.LENGTH_SHORT).show();
                },
                failure -> {
                    Log.i(TAG, "saveTask: your task didin't update" + failure);
                }
        );
    }
    public void deleteButtonSetup(){
        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(view -> {
            Amplify.API.mutate(
                    ModelMutation.delete(taskToEdit),
                    success -> {
                        Log.i(TAG, "deleteButtonSetup: Task was deleted");
                        deleteImageFromS3();
                        Intent goToMyTasksActivity = new Intent(EditTaskActivity.this, MyTasksActivity.class);
                        startActivity(goToMyTasksActivity);
                    },
                    failure -> {
                        Log.i(TAG, "deleteButtonSetup: Task deletion failed!", failure);
                    }
            );
        });
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
    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
    }
}