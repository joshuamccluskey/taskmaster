package com.joshuamccluskey.taskmaster.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amazonaws.util.DateUtils;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.StateEnum;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.joshuamccluskey.taskmaster.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity {
    public static final String TAG = "ADD TASK";
    Spinner teamSpinner = null;
    Spinner statusSpinner = null;
    List<String> teamNames = new ArrayList<>();
    List<Team> teamList = new ArrayList<>();
    CompletableFuture<List<Team>> teamFuture = null;
    ActivityResultLauncher<Intent> activityResultLauncher;
    FusedLocationProviderClient locationProvider = null;
    Geocoder geocoder;
    String imageS3Key = "";
    String pickedImgFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        activityResultLauncher = getImgActivityResultLauncher();
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        locationProvider = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationProvider.flushLocations();

        Intent gettingIntent = getIntent();
        if ((gettingIntent != null) && (gettingIntent.getType() != null) && (gettingIntent.getType().startsWith("image"))) {
            Uri incomingImageFileUri = gettingIntent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (incomingImageFileUri != null) {
                InputStream incomingImageFileInputStream = null;
                try {
                    incomingImageFileInputStream = getContentResolver().openInputStream(incomingImageFileUri);
                    pickedImgFilename = getFileNameFromUri(incomingImageFileUri);
                    uploadInputStreamToS3(incomingImageFileInputStream, pickedImgFilename, incomingImageFileUri);

                } catch (FileNotFoundException fileNotFoundException) {
                    Log.e(TAG, "onCreate: There was an error witht he imageFile View" + fileNotFoundException.getMessage());
                }

                ImageView productImageView = findViewById(R.id.addTaskImageView);
                productImageView.setImageBitmap(BitmapFactory.decodeStream(incomingImageFileInputStream));
            }
        }


        addImgButtonSetup();
        deleteImgButtonSetup();
        saveButtonSetup();

        teamFuture = new CompletableFuture<>();
        statusSpinner = findViewById(R.id.statusSpinner);
        statusSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                StateEnum.values()));

        teamSpinner = findViewById(R.id.teamSpinner);


        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Task successfully created");


                    for (Team databaseTeam : success.getData()) {
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
    }

    public void saveTask(String title,String body,String lat,String lon,Team selectedTeam) {


        Task newTask = Task.builder()
                .title(title)
                .body(body)
                .state((StateEnum) statusSpinner.getSelectedItem())
                .lat(lat)
                .lon(lon)
                .team(selectedTeam)
                .taskImgS3Key(imageS3Key)
                .build();

        Amplify.API.mutate(
                ModelMutation.create(newTask),
                success -> {

                    Log.i(TAG, "saveTask: updated task success!" + success);

                    Snackbar.make(findViewById(R.id.addTaskActivity), "Task saved!", Snackbar.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    Intent goToMyTasksActivity = new Intent(AddTaskActivity.this, MyTasksActivity.class);
                    startActivity(goToMyTasksActivity);

                },
                failure -> {
                    Log.i(TAG, "saveTask: your task didin't update" + failure);
                }
        );
    }

    public void saveButtonSetup() {
        Button saveTaskButton = findViewById(R.id.saveTaskButton);
        saveTaskButton.setOnClickListener(view -> {
            String title = ((EditText) findViewById(R.id.taskNameEditText)).getText().toString();
            String body = ((EditText) findViewById(R.id.taskDescriptionEditText)).getText().toString();
            String currentDate = DateUtils.formatISO8601Date(new Date());
            String selectedTeamString = teamSpinner.getSelectedItem().toString();
            Team selectedTeam = teamList.stream().filter(team -> team.getTeamName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);

            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)

            {
                Log.e(TAG, "Application does not have access to either ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION!");
                return;
            }
            locationProvider.getLastLocation().addOnSuccessListener(location ->  // "location" here could be null if no one else has request a location prior!
                            // Try running Google Maps first if you have a null callback here!
                    {
                        if (location == null)
                        {
                            Log.e(TAG, "Location callback was null!");
                        }
                        String lat = Double.toString(location.getLatitude());
                        String lon = Double.toString(location.getLongitude());
                        Log.i(TAG, "Our latitude: " + location.getLatitude());
                        Log.i(TAG, "Our longitude: " + location.getLongitude());
                        saveTask(title, body, lat, lon, selectedTeam);
                    }
            ).addOnCanceledListener(() ->
            {
                Log.e(TAG, "Location request was canceled!");
            })
                    .addOnFailureListener(failure ->
                    {
                        Log.e(TAG, "Location request failed! Error was: " + failure.getMessage(), failure.getCause());
                    })
                    .addOnCompleteListener(complete ->
                    {
                        Log.e(TAG, "Location request completed!");
                    });
        });
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
                                            pickedImgFilename = getFileNameFromUri(pickedImgFileUri);
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
                    ImageView taskImageView = findViewById(R.id.addTaskImageView);
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
        Button addImgButton = findViewById(R.id.addImgButton);
        addImgButton.setOnClickListener(view -> {
            launchImgSelection();
        });
    }

    public void deleteImgButtonSetup(){
        Button deleteImgButton = findViewById(R.id.deleteImgButton);
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

}
