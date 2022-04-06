package com.joshuamccluskey.taskmaster.activity;

import android.annotation.SuppressLint;
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

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.joshuamccluskey.taskmaster.R;
import com.joshuamccluskey.taskmaster.adapter.MyTasksListRecyclerViewAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;


public class MyTasksActivity extends AppCompatActivity {

    public final String TAG = "MainActivity";
    public static String TASK_ID_TAG = "TASK ID TAG";
    public static String TASK_DETAIL_TITLE_TAG = "TASK DETAIL TITLE";
    public static String TASK_DETAIL_BODY_TAG = "TASK BODY";
    public static String TASK_DETAIL_STATE_TAG = "TASK STATE";
    SharedPreferences userPreferences;

    MyTasksListRecyclerViewAdapter myTasksListRecyclerViewAdapter;
    List<Task> tasksList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks);

        userPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        tasksList = new ArrayList<>();

    File blankFile = new File(getApplicationContext().getFilesDir(), "blankTestFileName");
    String blankFileName =  "blankFile";
    try
    {
        BufferedWriter blankFileBufferWriter = new  BufferedWriter(new FileWriter(blankFile));
        blankFileBufferWriter.append("this is some test text");
        blankFileBufferWriter.close();
    } catch (IOException ioException){
        Log.e(TAG, "onCreate: File could not be created or written:  " + blankFileName, ioException);
    }

    String blankFileS3Key = "testS3File";

    Amplify.Storage.uploadFile(
            blankFileS3Key,
            blankFile,
            winning -> {
                Log.i(TAG, "onCreate: S3 was created winning! " + winning.getKey());
            },
            losing -> {
                Log.i(TAG, "onCreate: S3 failed and its a loss " + losing.getMessage());
            }
    );


//        Amplify.Auth.fetchAuthSession(
//                result -> Log.i("AmplifyQuickstart", result.toString()),
//                error -> Log.e("AmplifyQuickstart", error.toString())
//        );

//        String currentDate = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
//        com.amplifyframework.datastore.generated.model.Task tasker =
//            com.amplifyframework.datastore.generated.model.Task.builder()
//                .title("Taxes")
//                .body("Due this week!")
//                .state(com.amplifyframework.datastore.generated.model.StateEnum.New)
//                .date(new Temporal.DateTime(currentDate))
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(tasker),
//                successResponse -> Log.i(TAG, "MyTaskActivity.onCreate: made a Task"),
//                failureResponse -> Log.i(TAG, "MyTaskActivity.onCreate: failed" + failureResponse)
//        );

//        Team codeForLife =
//            Team.builder()
//                .teamName("Code For Life")
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(codeForLife),
//                successResponse -> Log.i(TAG, "MyTaskActivity.onCreate: made a Team"),
//                failureResponse -> Log.i(TAG, "MyTaskActivity.onCreate: failed" + failureResponse)
//        );
//        Team codeNinjas =
//                Team.builder()
//                        .teamName("Code Ninjas")
//                        .build();
//        Amplify.API.mutate(
//                ModelMutation.create(codeNinjas),
//                successResponse -> Log.i(TAG, "MyTaskActivity.onCreate: made a Team"),
//                failureResponse -> Log.i(TAG, "MyTaskActivity.onCreate: failed" + failureResponse)
//        );
//        Team codeBuilders =
//                Team.builder()
//                        .teamName("Code Builders")
//                        .build();
//        Amplify.API.mutate(
//                ModelMutation.create(codeBuilders),
//                successResponse -> Log.i(TAG, "MyTaskActivity.onCreate: made a Team"),
//                failureResponse -> Log.i(TAG, "MyTaskActivity.onCreate: failed" + failureResponse)
//        );


        loginButtonSetUp();
        logoutButtonSetup();
        addTaskButtonSetUp();
        allTasksButtonSetUp();
        settingsImageButtonSetUp();
        myTasksListRecycleViewSetUp();


    }


        // onResume needs to be outside of on Create all methods taken out of onCreate
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResume(){
            super.onResume();

//            String username = userPreferences.getString(SettingsActivity.USERNAME_TAG, "No Username");
//            ((TextView)findViewById(R.id.usernameTextView)).setText(getString(R.string.username_username, username));

            AuthUser currentUser = Amplify.Auth.getCurrentUser();
            String preferredUserName = "";
            Button loginButton = findViewById(R.id.logInMainButton);
            Button logoutButton = findViewById(R.id.logoutMainButton);

            if(currentUser == null){
                loginButton.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.INVISIBLE);
            } else {
                Log.i(TAG, "onCreate: Username:  " + preferredUserName);
                loginButton.setVisibility(View.INVISIBLE);
                logoutButton.setVisibility(View.VISIBLE);


                Amplify.Auth.fetchUserAttributes(
                        success ->
                        {
                            for (AuthUserAttribute userInfo : success) {
                                if (userInfo.getKey().getKeyString().equals("preferred_username")) {
                                    String usersName = userInfo.getValue();
                                    runOnUiThread(() -> {
                                        ((TextView) findViewById(R.id.usernameTextView)).setText(usersName);
                                    });


                                }
                            }
                        },
                        failure -> {
                            Log.i(TAG, "onCreate: Username not found : " + failure);
                        }
                );
            }

            Amplify.API.query(
                    ModelQuery.list(Task.class),
                    success -> {
                        Log.i(TAG, "Task successfully created");
                        tasksList.clear();
                        String teamNameString = userPreferences.getString(SettingsActivity.TEAM_TAG, "No Team Name");
                        for (Task databaseTask :success.getData()) {


                            if (databaseTask.getTeam().getTeamName().equals(teamNameString))
                            {
                                tasksList.add(databaseTask);
                            }

                        }
                        runOnUiThread(() -> myTasksListRecyclerViewAdapter.notifyDataSetChanged());

                    },
                    failure -> Log.i(TAG, "Task creation failed ")
            );


            myTasksListRecycleViewSetUp();
        }

    public void loginButtonSetUp(){
        Button loginMainButton = findViewById(R.id.logInMainButton);
        loginMainButton.setOnClickListener(view -> {
            System.out.println("Login Button!");
            Log.e(TAG, "onClick: Login Button!");
            Intent goToLoginIntent = new Intent(MyTasksActivity.this, LoginActivity.class);
            MyTasksActivity.this.startActivity(goToLoginIntent);
        });
    }

    public void logoutButtonSetup(){
        Button logoutMainButton = findViewById(R.id.logoutMainButton);
        logoutMainButton.setOnClickListener(view -> {
            System.out.println("LogoutButton!");
            Log.e(TAG, "onClick: Logout Button!");
            Amplify.Auth.signOut(
                        () -> {
                    Log.i(TAG, "Logout completed: ");
                },
                failure -> {
                    Log.i(TAG, "Logout not completed: " + failure.toString());
                }

        );
            Intent goToLoginIntent = new Intent(MyTasksActivity.this, LoginActivity.class);
            MyTasksActivity.this.startActivity(goToLoginIntent);
        });
    }

        public void addTaskButtonSetUp(){
            Button addTaskButton = findViewById(R.id.goToAddTaskButton);
            addTaskButton.setOnClickListener(view -> {
                System.out.println("Add Task Button!");
                Log.e(TAG, "onClick: Add Task Button!");
                Intent goToAddTaskIntent = new Intent(MyTasksActivity.this, AddTaskActivity.class);
                MyTasksActivity.this.startActivity(goToAddTaskIntent);
            });
        }

        public void allTasksButtonSetUp() {

            Button allTasksButton = findViewById(R.id.allTasksButton);
            allTasksButton.setOnClickListener(view -> {
                System.out.println("All Tasks Button!");
                Log.e(TAG, "onClick: All Tasks Button!");
                Intent goToAllTasksIntent = new Intent(MyTasksActivity.this, AllTasksActivity.class);
                startActivity(goToAllTasksIntent);
            });
        }

        public void settingsImageButtonSetUp() {
            ImageButton settingsImageButton = findViewById(R.id.settingsImageButton);
            settingsImageButton.setOnClickListener(view -> {
                System.out.println("Settings Image Button!");
                Log.e(TAG, "onClick: Settings Image Button!");
                Intent goToSettingsIntent = new Intent(MyTasksActivity.this, SettingsActivity.class);
                startActivity(goToSettingsIntent);
            });
        }


        public void myTasksListRecycleViewSetUp(){
            RecyclerView myTasksListRecycleView = findViewById(R.id.tasksListRecycleView);

            RecyclerView.LayoutManager taskLayoutManager =  new LinearLayoutManager(this);

            myTasksListRecycleView.setLayoutManager(taskLayoutManager);

            myTasksListRecyclerViewAdapter = new MyTasksListRecyclerViewAdapter(tasksList, this);

            myTasksListRecycleView.setAdapter(myTasksListRecyclerViewAdapter);
        }
    }
