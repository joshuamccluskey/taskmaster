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
        Intent gettingIntent = getIntent();
        String email = gettingIntent.getStringExtra(VerifyAccountActivity.TAG_VERIFY);
        EditText loginTextEmailAddress = findViewById(R.id.loginTextEmailAddress);
        EditText loginTextPassword = findViewById(R.id.loginTextPassword);
        loginTextEmailAddress.setText(email);
        loginButton.setOnClickListener(view -> {
        String userEmail = loginTextEmailAddress.getText().toString();
        String password = loginTextPassword.getText().toString();

            Amplify.Auth.signIn(
                userEmail,
                password,

                success -> {
                    Log.i(TAG, "Login completed: " + success);
                    System.out.println("Login Button!");
                    Intent goToLoginIntent = new Intent(LoginActivity.this, MyTasksActivity.class);
                    LoginActivity.this.startActivity(goToLoginIntent);
                },
                failure -> {
                    Log.i(TAG, "Login not completed: " + failure);
                    runOnUiThread(()->
                    {
                        Toast.makeText(LoginActivity.this, "Login not successful! Check Email or Password", Toast.LENGTH_SHORT);
                    });
                }

        );



            //TODO Add Login

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