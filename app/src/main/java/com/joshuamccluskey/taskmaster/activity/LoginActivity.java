package com.joshuamccluskey.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.joshuamccluskey.taskmaster.R;

public class LoginActivity extends AppCompatActivity {
    public final String TAG = "LogInActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginLoginButtonSetUp();
        loginSignUpButtonSetUp();
    }

    public void loginLoginButtonSetUp(){
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            System.out.println("Login Button!");
            Log.e(TAG, "onClick: Login Button!");

            //TODO Add Login
            Intent goToLoginIntent = new Intent(LoginActivity.this, VerifyAccountActivity.class);
            LoginActivity.this.startActivity(goToLoginIntent);
        });
    }


    public void loginSignUpButtonSetUp(){
        Button loginSignUpButton = findViewById(R.id.loginSignUpButton);
        loginSignUpButton.setOnClickListener(view -> {
            System.out.println("Signup Button!");
            Log.e(TAG, "onClick: Signup Button!");
            Intent goToSignUpIntent = new Intent(LoginActivity.this, SignupActivity.class);
            LoginActivity.this.startActivity(goToSignUpIntent);
        });
    }
}