package com.joshuamccluskey.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.joshuamccluskey.taskmaster.R;

public class VerifyAccountActivity extends AppCompatActivity {
    String TAG = "VerifyAccountActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        verifyButtonSetUp();
    }

    public void verifyButtonSetUp(){
        Button verifyButton = findViewById(R.id.verifyButton);
        verifyButton.setOnClickListener(view -> {
            System.out.println("Verify Button!");
            Log.e(TAG, "onClick: Verify Button!");

            Intent goToMainIntent = new Intent(VerifyAccountActivity.this, LoginActivity.class);
            VerifyAccountActivity.this.startActivity(goToMainIntent);
        });
    }


}