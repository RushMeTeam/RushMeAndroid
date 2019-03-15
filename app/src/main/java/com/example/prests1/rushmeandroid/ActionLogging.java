package com.example.prests1.rushmeandroid;

import android.util.Log;

/**
 * User Action Logging to be stored for analytic use
 */
public class ActionLogging {

    /**
     * This is the logging function that takes in an event which is a string of a list of event types and an optional detail about
     * what is happening in the event
     *
     * i.e.
     *  event = Fraternity Selected
     *  detail = Fraternity.getName()
     *
     * @param event <-- predefined events that can happen
     * @param detail <-- optional details from the event
     */
    public static void Log(String event, String detail){
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
