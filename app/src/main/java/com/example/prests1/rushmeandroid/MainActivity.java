package com.example.prests1.rushmeandroid;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * The Main Activity is the activity loaded on the start of the android application
 *
 * Current main activity:
 *  - Calendar of all events
 */

public class MainActivity extends AppCompatActivity implements EventRecyclerViewAdapter.ItemClickListener {
    ProgressDialog pd; /* Loading blocker for events/fraternities */
    HashMap<String, Fraternity> fraternities = new HashMap<String, Fraternity>(); /* hash table of fraternities class organized by the names of fraternities on campus */
    HashMap<String, Fraternity> fraternitiesByKey = new HashMap<String, Fraternity>(); /* hash table of fraternities organized by the unique 3 letter queue for each fraternity */
    ArrayList<Fraternity.Event> events; /* List of all events on campus */
    ArrayList<Fraternity.Event> selectedEvents; /* List of events selected for calendar day selected */
    private ArrayMap<CalendarDay, Integer> eventNum; /* Number of events for each calendar day stored in an iterable hash table */
    MaterialCalendarView newCal; /* Calendar reference to Main Activity XML */
    EventRecyclerViewAdapter adapter; /* Adapter for recycler view of selected day's events */

    ActionLogging logger = new ActionLogging(); /* User Action logging for data */

    /**
     * onCreate is the function that gets called at the start of an activity tries to load a saved instance
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Get the Material Calendar from Main Activity XML file */
        newCal = (MaterialCalendarView) findViewById(R.id.newCal);

        /* Set MIN/MAX range */
        newCal.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2019,0,1))
                .setMaximumDate(CalendarDay.from(2019,3,28))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        /**
         * Call for All fraternityScrollView
         */
        new RemoteContentTask().execute("https://s3.us-east-2.amazonaws.com/rushmepublic/fraternites.rushme", "https://s3.us-east-2.amazonaws.com/rushmepublic/events.rushme");
        log("App Entered Foreground", "");


        /**
         * Setup for the adapter for the selected dates
         *
         * Adapter class stores and displays events based on day selected. List is updated when a new date is selected
         *
         * RecyclerView displays the events in the ArrayList. Only renders elements on screen and one above and one below out of screen
         */
        selectedEvents = new ArrayList<Fraternity.Event>();
        final RecyclerView rv = findViewById(R.id.fraternitiesRV);

        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EventRecyclerViewAdapter(this, selectedEvents);
        adapter.setClickListener(this);
        rv.setAdapter(adapter);

        
        /**
         * CalndarView date change listener
         *
         * This function passes the newest dates to the adapter to update the recyclerview
         */
        newCal.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedEvents.clear(); /* Empty out selected events array */
                for(int i=0; i<events.size(); ++i){
                    CalendarDay temp = new CalendarDay(events.get(i).starting);
                    if((temp.getDay() == date.getDay()) && (temp.getYear() == date.getYear()) && (temp.getMonth() == date.getMonth())){ /* Makes sure event matches the date selected */
                        selectedEvents.add(events.get(i));
                    }
                }
                adapter.updateData(selectedEvents); /* Update Adapter */
            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
        logger.Log("App Background", "");
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
                nameValuePairs.add(new BasicNameValuePair("appv", "0.5.1"));
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

    /**
     * onItemClick is called when a person selects a fraternities event from the selectedEvents RecyclerView
     * @param view
     * @param position
     */
    public void onItemClick(View view, int position) {
        if (position >= 0 && position < selectedEvents.size()) {
            Fraternity.Event event = selectedEvents.get(position); /* Grab fraternity based on position clicked */
            startActivityFor(event.frat); /* call startActivityFor function which loads individual fraternity detail */
        }
    }

    /**
     * Bundles up a fraternity into an Intent with the selected Fraternity. FraternityDetail activity is started with specified intent
     * @param fraternity
     */
    private void startActivityFor(Fraternity fraternity) {
        logger.Log("Fraternity Selected", fraternity.getName());
        Intent intent = new Intent(this, FraternityDetail.class);
        intent.putExtra("Fraternity", fraternity);
        startActivity(intent);
    }


    /**
     * Json Array from URL for fraternities and Events
     */
    private class RemoteContentTask extends AsyncTask<String, String, String> {

        /**
         * Function called before doInBackground()
         */
        protected void onPreExecute() {
            super.onPreExecute();

            /**
             * progress dialog to show events are being found
             */
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Loading events...");
            pd.setCancelable(false); /* Prevents user from using app while getting events */
            pd.show();
        }

        /**
         * Gets Fraternity JSON from URL
         *
         * @param fromSURL
         * @return
         */
        protected String loadFraternities(String fromSURL) {
            String rawGet = "{}";
            try {
                rawGet = this.get(fromSURL); //Gets JSON from URL with get function
            } catch (Exception e) {
                Log.e("LoadFraternities", e.getMessage());
            }
            JSONArray fraternitiesJSONArray;
            try {

                fraternitiesJSONArray = new JSONArray(rawGet);
                int length = fraternitiesJSONArray.length();
                for (int i = 0; i < length; i++) {
                    try {
                        /**
                         * Gets values from JSON to create a new Fraternity object
                         */
                        JSONObject fratJSON = fraternitiesJSONArray.getJSONObject(i);
                        String name = fratJSON.getString("name");
                        String chapter = fratJSON.getString("chapter");
                        int members = fratJSON.getInt("member_count");
                        String description = fratJSON.getString("description");
                        String key = fratJSON.getString("namekey");
                        Fraternity frat = new Fraternity(name, key, chapter, members, description);

                        /**
                         * add new fraternity to fraternities and fraternitiesByKey
                         */
                        fraternities.put(frat.getName(), frat);
                        fraternitiesByKey.put(frat.getKey(), frat);
                    } catch (Exception e) { /* Error handling */
                        Log.e("Error Initializing Fraternity " + (i+1), e.getMessage());
                    }
                }
                /* Successful load */
                return "Success";
            }
            /**
             * Error handling
             */
            catch (JSONException e) {
                Log.e("Load Fraternities", e.toString());
            } catch (Exception e) {
                Log.e("Load Fraternities", e.toString());
            }
            return "Failure";
        }

        /**
         * Get events from JSON by URL
         *
         * @param fromSURL
         * @return
         */
        protected String loadEvents(String fromSURL) {
            String rawGet = "{}";
            try {
                rawGet = this.get(fromSURL); // Get method that returns JSON from URL
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
                        /**
                         * Parse JSON and create new Event object
                         */
                        JSONObject eventJSON = eventsJSONArray.getJSONObject(i);
                        String name  = eventJSON.getString("event_name");
                        String duration = eventJSON.getString("duration");
                        String[] splitDuration = duration.split(":");

                        int intDuration = Integer.parseInt(splitDuration[0])*60 + Integer.parseInt(splitDuration[1]);
                        String description = eventJSON.has("description") ? eventJSON.getString("description") : "";
                        String fratKey = eventJSON.getString("frat_name_key");
                        String startTime = eventJSON.getString("start_time");
                        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                        Date starting = (Date) df1.parse(startTime);
                        Log.d("DBUG_EVENT_TIME", Integer.toString(starting.getDay()));
                        String inviteOnly = eventJSON.getString("invite_only");
                        String location = eventJSON.getString("location");
                        Fraternity frat = fraternitiesByKey.get(fratKey);
                        /**
                         * Some Frats were suspended which caused them to be null when adding to events list
                         */
                        if(frat != null){
                            Fraternity.Event event = new Fraternity.Event(name, location, frat, starting, intDuration);
                            events.add(event);
                        }else {
                            Log.d("NULL FRAT EVENT", " > " +name + " " + fratKey + " Month: " + Integer.toString(starting.getMonth()+1) + " Day: " + Integer.toString(starting.getDay()+1));
                        }

                    /* Error Handling */
                    } catch (Exception e) {
                        Log.e("Error Initializing Event " + (i+1), e.getMessage());
                    }
                }
                /* Successful load */
                return "Success";
            }
            /**
             * Error handling
             */
            catch (JSONException e) {
                Log.e("Load Events", "JSON Error");
            } catch (Exception e) {
                Log.e("Load Events", "Exception");
            }
            return "Failure";

        }

        /**
         * An Asynchronous task runs in the background so this function gets called to get JSON from url overtime
         *
         * @param params
         * @return
         */
        protected String doInBackground(String... params) {
            return loadFraternities(params[0]) + "\n" + loadEvents(params[1]);
        }

        /**
         * Takes a URL and returns contents (in our case a JSON object)
         * @param sURL
         * @return
         * @throws Exception
         */
        String get(String sURL) throws Exception {
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
         * After doInBackground completes this function is called to wrap up the asynchronous task
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            /**
             * Remove the progress dialog so user regains control of app
             */
            if (pd.isShowing()){
                pd.dismiss();
            }

            /* Sort events */
            events.sort(new SortChronologically());

            /**
             * Campus is a global class that holds all the events and fraternities to make it easier to pass large lists between activities
             */
            Campus campus = ((Campus) getApplicationContext());
            campus.setEvents(events);

            /**
             * Labeling event numbers on calendar
             */
            eventNum = parseEvents(events);
            for(CalendarDay i : eventNum.keySet()){
                ArrayList<CalendarDay> temp = new ArrayList<CalendarDay>();
                temp.add(i);
                newCal.addDecorator(new eventDecorator(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null), temp, eventNum.get(i)));
            }
        }
    }


    /**
     * Tally's up all the events based on similar dates and stores them to be displayed on the calendar
     *
     * @param events
     * @return
     */
    private ArrayMap<CalendarDay, Integer> parseEvents(ArrayList<Fraternity.Event> events){
        ArrayMap<CalendarDay, Integer> eventNums = new ArrayMap<CalendarDay, Integer>();
        for(int i=0; i<events.size(); ++i){
            CalendarDay temp = new CalendarDay(events.get(i).starting);
            if(!eventNums.containsKey(temp)){
                eventNums.put(temp, 1);
            } else {
                int num = eventNums.get(temp);
                ++num;
                eventNums.put(temp, num);
            }
        }
        return eventNums;
    }
}