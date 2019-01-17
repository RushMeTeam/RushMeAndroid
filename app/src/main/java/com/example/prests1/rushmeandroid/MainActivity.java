package com.example.prests1.rushmeandroid;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ProgressDialog pd;
    LinearLayout parent;
    ScrollView fraternityScrollView;
    HashMap<String, Fraternity> fraternities = new HashMap<String, Fraternity>();
    HashMap<String, Fraternity> fraternitiesByKey = new HashMap<String, Fraternity>();
    ArrayList<Fraternity.Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parent = (LinearLayout) findViewById(R.id.fraternityView);

        /**
         * Call for All fraternityScrollView
         */
        new LoadFraternitiesTask().execute("https://s3.us-east-2.amazonaws.com/rushmepublic/fraternites.rushme");

        /**
         * Generate a bunch of clickables for fraternityScrollView
         */
//        for (int i = 0; i < 100; i++) {
//            btn = new Button(MainActivity.this);
//            btn.setHeight(300);
//            btn.setId(i + 1);
//            btn.setText(frat1.getName());
//            btn.setTag(i);
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openFraternityProfile();
//                }
//            });
//            parent.addView(btn);
//        }

        fraternityScrollView = (ScrollView) findViewById(R.id.fraternities);

//        /**
//         * Generate Calendar clickable
//         */
//        Button calendarBtn = (Button) findViewById(R.id.calendarBtn);
//        calendarBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openCalendar();
//            }
//        });
//
//        /**
//         * Generate Map clickable
//         */
//        Button mapBtn = (Button) findViewById(R.id.btnMap);
//        mapBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openMap();
//            }
//        });
    }

    static String get(String sURL) throws Exception {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(sURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            return buffer.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.e("Close Connection Error",e.getMessage());
            }
        }
    }
    /**
     * Store fraternity info in an intent and move to fraternityprofile
     */
//    public void openFraternityProfile() {
//        Intent intent = new Intent(this, FraternityProfile.class);
//        intent.putExtra("fraternity", frat1.getName());
//        intent.putExtra("chapter", frat1.getName());
//        intent.putExtra("memberCount", frat1.getMemberCount());
//        intent.putExtra("description", frat1.getDescription());
//        startActivity(intent);
//    }

    /**
     * move to calendar activity
     */
    public void openCalendar() {
        startActivity(new Intent(this, calendar.class));
    }

    /**
     * move to map activity
     */
    public void openMap() {
        startActivity(new Intent(this, map.class));
    }

    /**
     * Json Array from URL
     */
    private class LoadEventsTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Loading events...");
            pd.setCancelable(false);
            pd.show();
        }


        protected String doInBackground(String... params) {
            /*{
            "description": "This is a Greek wide event hosted by the Interfraternal Council to distribute chapter recruitment calendars and provide information about Greek Life at RPI.",
            "duration": "03:00",
            "event_name": "IFC Kickoff",
            "frat_name_key": "ACA",
            "start_time": "2018-10-19T16:00",
            "invite_only": "no",
            "location":
            "RPI Union - McNeil Room"}
            */
            String rawGet = "{}";
            try {
                rawGet = MainActivity.get(params[0]);
            } catch (Exception e) {
                Log.e("LoadFraternities", e.getMessage());
                return "Failure";
            }
            JSONArray eventsJSONArray;
            events = new ArrayList<>();
            try {
                eventsJSONArray = new JSONArray(rawGet);
                int length = eventsJSONArray.length();
                for (int i = 0; i < length; i++) {
                    try {
                        JSONObject eventJSON = eventsJSONArray.getJSONObject(i);
                        String name  = eventJSON.getString("event_name");
                        String duration = eventJSON.getString("duration");
                        String[] splitDuration = duration.split(":");

                        int intDuration = Integer.parseInt(splitDuration[0])*60 + Integer.parseInt(splitDuration[1]);
                        String description = eventJSON.getString("description");
                        String fratKey = eventJSON.getString("frat_name_key");
                        String startTime = eventJSON.getString("start_time");
                        Date starting = java.sql.Date.valueOf(startTime);
                        String inviteOnly = eventJSON.getString("invite_only");
                        String location = eventJSON.getString("location");
                        Fraternity frat = fraternitiesByKey.get(fratKey);
                        // String name,  String location, Fraternity frat, Date starting, Integer durationInMinutes
                        Fraternity.Event event = new Fraternity.Event(name, location, frat, starting, intDuration);
                        events.add(event);

                    } catch (Exception e) {
                        Log.e("Error Initializing Event " + (i+1), e.getMessage());
                    }
                }
                Log.d("Load Events", "Initialized " + events.size() + " events");
                return "Success";
            }
            catch (JSONException e) {
                Log.e("Load Events", "JSON Error");
            } catch (Exception e) {
                Log.e("Load Events", "Exception");
            }
            return "Failure";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Load Events", result);
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }

        }
    }

    private class LoadFraternitiesTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Loading fraternities...");
            pd.setCancelable(false);
            pd.show();
        }


        protected String doInBackground(String... params) {
            String rawGet = "{}";
            try {
                rawGet = MainActivity.get(params[0]);
            } catch (Exception e) {
                Log.e("LoadFraternities", e.getMessage());
            }
            JSONArray fraternitiesJSONArray;
            try {

                fraternitiesJSONArray = new JSONArray(rawGet);
                int length = fraternitiesJSONArray.length();
                for (int i = 0; i < length; i++) {
                    try {
                        JSONObject fratJSON = fraternitiesJSONArray.getJSONObject(i);
                        String name = fratJSON.getString("name");
                        String chapter = fratJSON.getString("chapter");
                        int members = fratJSON.getInt("member_count");
                        String description = fratJSON.getString("description");
                        String key = fratJSON.getString("namekey");
                        Fraternity frat = new Fraternity(name, key, chapter, members, description);
                        fraternities.put(frat.getName(), frat);
                        fraternitiesByKey.put(frat.getKey(), frat);
                    } catch (Exception e) {
                        Log.e("Error Initializing Fraternity " + (i+1), e.getMessage());
                    }
                }
                Log.d("Load Fraternities", "Initialized " + fraternities.size() + " fraternities");
                return "Success";
            }
            catch (JSONException e) {
                Log.e("Load Fraternities", e.toString());
            } catch (Exception e) {
                Log.e("Load Fraternities", e.toString());
            }
            return "Failure";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Load Fraternities", result);
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            new LoadEventsTask().execute("https://s3.us-east-2.amazonaws.com/rushmepublic/events.rushme");


        }
    }
}