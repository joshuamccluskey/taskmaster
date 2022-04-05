package com.joshuamccluskey.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.joshuamccluskey.taskmaster.R;

public class VerifyAccountActivity extends AppCompatActivity {
    String TAG = "VerifyAccountActivity";
    String VERIFY_TAG =  "VerifyAccountTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        verifyButtonSetUp();
    }

    public void verifyButtonSetUp(){
        Button verifyButton = findViewById(R.id.verifyButton);
        Intent gettingIntent = getIntent();
        String email = gettingIntent.getStringExtra(SignupActivity.TAG_SIGNUP_EMAIL);
        EditText verifyTextEmailAddress = findViewById(R.id.verifyTextEmailAddress);
        verifyTextEmailAddress.setText(email);
        verifyButton.setOnClickListener(view -> {

            String userEmail = verifyTextEmailAddress.getText().toString();
            String verification = ((EditText) findViewById(R.id.verifyTextPassword)).getText().toString();

            Amplify.Auth.confirmSignUp(userEmail,
                    verification,
                    good -> {
                        System.out.println("Verify Button!");
                        Log.i(TAG, "onClick: Verify Button!" + good);
                        Intent goToMainIntent = new Intent(VerifyAccountActivity.this, LoginActivity.class);
                        VerifyAccountActivity.this.startActivity(goToMainIntent);
                    },
                    bad -> {
                        Log.i(TAG, "onClick: Verify Button!" + bad);
                        runOnUiThread(()->
                        {
                            Toast.makeText(VerifyAccountActivity.this, "Verification not successful!", Toast.LENGTH_SHORT);
                        });
                    });



        });
    }


}