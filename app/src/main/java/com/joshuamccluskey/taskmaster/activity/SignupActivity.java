package com.joshuamccluskey.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.joshuamccluskey.taskmaster.R;

public class SignupActivity extends AppCompatActivity {
    public final String TAG = "SignUpActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupButtonSetUp();
    }

    public void signupButtonSetUp(){
        Button signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(view -> {
            System.out.println("Signup signup Button!");
            Log.e(TAG, "onClick: Signup signup Button!");

            //TODO Sign up
            //TODO Send email to verify
            Intent goToVerifyIntent = new Intent(SignupActivity.this, VerifyAccountActivity.class);
            SignupActivity.this.startActivity(goToVerifyIntent);
        });
    }
}