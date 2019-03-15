package com.example.prests1.rushmeandroid;

import android.util.Log;

public class ActionLogging {

    public void Log(String event, String detail){
        switch(event){
            case "Fraternity Selected":
                Log.d("Action Logging", "Fraternity: " + detail + " was selected.");
                break;
            case "App Background":
                Log.d("Action Logging", "App has gone to the background");
                break;
            case "App Foreground":
                Log.d("Action Logging", "App has returned to the foreground");
                break;
        }
    }
}
