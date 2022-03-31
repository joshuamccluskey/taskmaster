package com.joshuamccluskey.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.joshuamccluskey.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences userPreferences;
    SharedPreferences teamPreferences;
    public static final String TAG = "CHOOSE TEAM";
    public static final String USERNAME_TAG = "username";
    Spinner teamSettingsSpinner = null;
    List<String> teamNames = new ArrayList<>();
    List<Team> teamList = new  ArrayList<>();
    CompletableFuture<List<Team>> teamFuture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button settingsSaveButton = findViewById(R.id.settingsSaveButton);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Task successfully created");


                    for (Team databaseTeam :success.getData()) {
                        teamList.add(databaseTeam);
                        teamNames.add(databaseTeam.getTeamName());
                    }
                    teamFuture.complete(teamList);
                    runOnUiThread(() ->{
                        teamSettingsSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                teamNames));


                    });

                },
                failure -> {
                    teamFuture.complete(null);
                    Log.i(TAG, "Task creation failed ");

                }
        );



        settingsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor userPreferencesEditor = userPreferences.edit();
                EditText usernameEditText = findViewById(R.id.usernameInputEditText);
                String usernameStringify = usernameEditText.getText().toString();
                userPreferencesEditor.putString(USERNAME_TAG, usernameStringify);
                userPreferencesEditor.apply();

                SharedPreferences.Editor teamPreferencesEditor = teamPreferences.edit();
                teamSettingsSpinner = findViewById(R.id.teamSettingsSpinner);
                String teamNameSelected = teamSettingsSpinner.getSelectedItem().toString();
                teamPreferencesEditor.putString(TAG, teamNameSelected);
                teamPreferencesEditor.apply();

                Intent goToMyTasksActivityIntent = new Intent(SettingsActivity.this, MyTasksActivity.class);
                startActivity(goToMyTasksActivityIntent);
            }
        });

        userPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = userPreferences.getString(USERNAME_TAG, "");
        if (!username.isEmpty()) {
            EditText usernameEidtText = findViewById(R.id.usernameInputEditText);
            usernameEidtText.setText(username);
        }


    }

}