package com.joshuamccluskey.taskmaster;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;

public class TaskMasterAmplifyApplication extends Application {
    public static final String TAG = "TASKMASTER APPLICATION"
    @Override
    public void onCreate(){
        super.onCreate();
        try{
            Amplify.configure(getApplicationContext());
        } catch (AmplifyException ae) {
            Log.e(TAG, "onCreate: " + ae.getMessage(), ae);
            
        }
        
    }
}
