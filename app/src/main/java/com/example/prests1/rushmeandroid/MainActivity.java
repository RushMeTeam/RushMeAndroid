package com.example.prests1.rushmeandroid;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {
    ProgressDialog pd;
    HashMap<String, Fraternity> fraternities = new HashMap<String, Fraternity>();
    HashMap<String, Fraternity> fraternitiesByKey = new HashMap<String, Fraternity>();
    ArrayList<Fraternity.Event> events;
    ListView eventList;
    private ArrayMap<CalendarDay, Integer> eventNum;
    MaterialCalendarView newCal;
    EventRecyclerViewAdapter adapter;

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
        log("APP DID LOAD", "");

        final ArrayList<Fraternity.Event> eventResults = new ArrayList<Fraternity.Event>();
        final RecyclerView rv = findViewById(R.id.fraternitiesRV);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventRecyclerViewAdapter(this, eventResults);
        rv.setAdapter(adapter);
        newCal.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                eventResults.removeAll(eventResults);
                for(int i=0; i<events.size(); ++i){

                    CalendarDay temp = new CalendarDay(events.get(i).starting);
                    if((temp.getDay() == date.getDay()) && (temp.getYear() == date.getYear()) && (temp.getMonth() == date.getMonth())){
                        eventResults.add(events.get(i));
                    }
                }
                Log.d("SIZER", Integer.toString(eventResults.size()));
                adapter.updateData(eventResults);
            }
        });

        Button fratList = (Button) findViewById(R.id.fratList);
        fratList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFratList();
            }
        });
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
     * Json Array from URL
     */
    private class RemoteContentTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Loading events...");
            pd.setCancelable(false);
            pd.show();
        }
        protected String loadFraternities(String fromSURL) {
            String rawGet = "{}";
            try {
                rawGet = this.get(fromSURL);
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
                        if(key == "PDP"){
                            Log.d("PDP FRAT", name);
                        }
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

        protected String loadEvents(String fromSURL) {
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
                rawGet = this.get(fromSURL);
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
                        String description = eventJSON.has("description") ? eventJSON.getString("description") : "";
                        String fratKey = eventJSON.getString("frat_name_key");
                        String startTime = eventJSON.getString("start_time");
                        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                        Date starting = (Date) df1.parse(startTime);
                        Log.d("DBUG_EVENT_TIME", Integer.toString(starting.getDay()));
                        String inviteOnly = eventJSON.getString("invite_only");
                        String location = eventJSON.getString("location");
                        Fraternity frat = fraternitiesByKey.get(fratKey);
                        if(frat != null){
                            // String name,  String location, Fraternity frat, Date starting, Integer durationInMinutes
                            Fraternity.Event event = new Fraternity.Event(name, location, frat, starting, intDuration);
                            events.add(event);
                        }else {
                            Log.d("NULL FRAT EVENT", " > " +name + " " + fratKey + " Month: " + Integer.toString(starting.getMonth()+1) + " Day: " + Integer.toString(starting.getDay()+1));
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

        protected String doInBackground(String... params) {
            return loadFraternities(params[0]) + "\n" + loadEvents(params[1]);

        }

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

        @Override
        protected void onPostExecute(String result) {
            Log.d("Load Events", result);
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }

            /* Sort events */
            events.sort(new SortChronologically());

            Campus campus = ((Campus) getApplicationContext());
            campus.setEvents(events);
            /* Labeling event numbers on calendar */
            eventNum = parseEvents(events);
            for(CalendarDay i : eventNum.keySet()){
                Log.d("EVENTINFO", Integer.toString(i.getMonth()) + "/" +Integer.toString(i.getDay()) + "/" + Integer.toString(i.getYear()));
                ArrayList<CalendarDay> temp = new ArrayList<CalendarDay>();
                temp.add(i);
                newCal.addDecorator(new eventDecorator(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null), temp, eventNum.get(i)));
            }
        }
    }



    /**
     * Take the event arraylist and convert the dates into an arraymap to see show the total # of events on a certain day
     *
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


    private void toFratList(){
        Intent intent = new Intent(this, FraternityList.class);
        startActivity(intent);
    }
}