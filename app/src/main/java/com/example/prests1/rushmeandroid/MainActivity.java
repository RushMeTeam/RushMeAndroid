package com.example.prests1.rushmeandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EventRecyclerViewAdapter.ItemClickListener {
    ProgressDialog pd;
    LinearLayout parent;
    CardView fraternityScrollView;
    HashMap<String, Fraternity> fraternities = new HashMap<String, Fraternity>();
    HashMap<String, Fraternity> fraternitiesByKey = new HashMap<String, Fraternity>();
    ArrayList<Fraternity.Event> events;

    EventRecyclerViewAdapter adapter;



    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }


    protected void log(String action, String options) {
        new LogAction().execute(action, options);
    }
    public class LogAction extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlString = "http://ec2-18-188-8-243.us-east-2.compute.amazonaws.com/request.php";
            OutputStream out = null;

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                out = new BufferedOutputStream(urlConnection.getOutputStream());

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                // duuid // rtime // dtype // dsoft // appv
                nameValuePairs.add(new BasicNameValuePair("duuid", Settings.Secure.ANDROID_ID));
                String SQLDateFormat = "yyyy-MM-dd HH:mm:ss.SSSZ";
                DateFormat sqlDF = new SimpleDateFormat(SQLDateFormat);
                String now = sqlDF.format(new Date());

                nameValuePairs.add(new BasicNameValuePair("pact", params[0]));
                nameValuePairs.add(new BasicNameValuePair("popt", params[1]));
                nameValuePairs.add(new BasicNameValuePair("rtime", now));
                nameValuePairs.add(new BasicNameValuePair("dtype", android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL));

                nameValuePairs.add(new BasicNameValuePair("dsoft", android.os.Build.VERSION.RELEASE));
                nameValuePairs.add(new BasicNameValuePair("appv", "0001"));
                new UrlEncodedFormEntity(nameValuePairs).writeTo(out);
//                Log.d("WOW", done);
//                // Execute HTTP Post Request
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
//                writer.write(done);
//                writer.flush();
//                writer.close();
                out.close();
                String response = "";
                urlConnection.connect();
                int responseCode=urlConnection.getResponseCode();
                Log.d("GETTING POST RESPONSE", "!");
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    int i = 0;
                    while ((line=br.readLine()) != null && i < 10) {
                        response+=line;
                        Log.e("LINE " + i, line);
                        i++;
                    }
                }
                else {
                    response="";
                }
                Log.e("RECEIVED POST RESPONSE", response);
            } catch (Exception e) {
                Log.e("LOG FAILED", e.getMessage());
            }
            return "Success";

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        parent = (LinearLayout) findViewById(R.id.fraternitiesRV);

        new LoadFraternitiesTask().execute("https://s3.us-east-2.amazonaws.com/rushmepublic/fraternites.rushme");
        log("APP DID LOAD", "");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.fraternitiesRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EventRecyclerViewAdapter(this, events);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
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
        ArrayList<Fraternity.Event> clone = new ArrayList<Fraternity.Event>();
        Log.d("EVENTSIZE", Integer.toString(events.size()));
        for(int i=0; i<10; ++i){
            Log.d("MAINEVENT", events.get(i).name);
            clone.add(events.get(i));
        }
        
        Intent intent = new Intent(this, Events.class);
        Bundle b = new Bundle();
        //b.putParcelableArrayList("events", events);
        intent.putExtra("events", clone);
        startActivity(intent);
    }


    protected void reloadData() {
        adapter.updateData(events);
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
                        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                        Date starting = df1.parse(startTime);
//                        String inviteOnly = eventJSON.getString("invite_only");
                        String location = eventJSON.getString("location");
                        Fraternity frat = fraternitiesByKey.get(fratKey);
                        if (frat != null) {
                            // String name,  String location, Fraternity frat, Date starting, Integer durationInMinutes
                            Fraternity.Event event = new Fraternity.Event(name, location, frat, starting, intDuration);
                            events.add(event);
                        }

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
            reloadData();
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