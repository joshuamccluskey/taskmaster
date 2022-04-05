package com.joshuamccluskey.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.joshuamccluskey.taskmaster.R;

public class SignupActivity extends AppCompatActivity {
    public final String TAG = "SignUpActivity";
    static String TAG_SIGNUP_EMAIL = "Signup_Email";
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

            String email = ((EditText) findViewById(R.id.signupTextEmailAddress)).getText().toString();
            String password = ((EditText) findViewById(R.id.signupTextPassword)).getText().toString();
            String username = ((EditText) findViewById(R.id.signupTextUserame)).getText().toString();


            Amplify.Auth.signUp(
                    email,
                    password,
                    AuthSignUpOptions.builder()
                            .userAttribute(AuthUserAttributeKey.email(), email)
                            .userAttribute(AuthUserAttributeKey.preferredUsername(), username)
                            .build(),
                    good -> {
                        Log.i(TAG, "Signup completed: " + good);
                        Intent goToVerifyIntent = new Intent(SignupActivity.this, VerifyAccountActivity.class);
                        goToVerifyIntent.putExtra(TAG_SIGNUP_EMAIL, email);
                        SignupActivity.this.startActivity(goToVerifyIntent);
                    },
                    bad -> {
                        Log.i(TAG, "Signup not completed: " + bad);
                        runOnUiThread(()->
                        {
                            Toast.makeText(SignupActivity.this, "Signup not successful!", Toast.LENGTH_SHORT);
                        });

                    }
            );
        });
    }
}