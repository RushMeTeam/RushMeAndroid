package com.example.prests1.rushmeandroid;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Global object that holds all events and fraternities
 *
 * extends Application to make global in the AndroidManifest.xml
 */

public class Campus extends Application {
    private HashMap<String, Fraternity> frats;
    private HashMap<String, Fraternity> fratsByKey;
    private ArrayList<Fraternity.Event> events;

    public HashMap<String, Fraternity> getFrats(){ return frats; }

    public HashMap<String, Fraternity> getFratsByKey() { return  fratsByKey; }

    public ArrayList<Fraternity.Event> getEvents(){ return events; }

    public void setEvents(ArrayList<Fraternity.Event> newEvents) { events = newEvents; }

    public void setFrats (HashMap<String, Fraternity> newFrats) { frats = newFrats; }

    public void setFratsByKey (HashMap<String, Fraternity> newFratsByKey) { fratsByKey = newFratsByKey; }
}
