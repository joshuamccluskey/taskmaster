package com.joshuamccluskey.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.joshuamccluskey.taskmaster.R;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences userPreferences;
    public static final String USERNAME_TAG = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button settingsSaveButton = findViewById(R.id.settingsSaveButton);
        settingsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor userPreferencesEditor = userPreferences.edit();
                EditText usernameEditText = findViewById(R.id.usernameInputEditText);
                String usernameStringify = usernameEditText.getText().toString();
                userPreferencesEditor.putString(USERNAME_TAG, usernameStringify);
                userPreferencesEditor.apply();
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