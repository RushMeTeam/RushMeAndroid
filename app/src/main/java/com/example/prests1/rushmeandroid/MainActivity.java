package com.example.prests1.rushmeandroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String bigBoiString;
    TextView txtJson;
    ProgressDialog pd;
    LinearLayout parent;
    Button btn;
    ScrollView fraternities;
    Fraternity frat1 = new Fraternity("Zoo", "Zoo", 11, "test");
    Fraternity frat2 = new Fraternity("Chi Phi", "something", 100, "this is chi phi");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parent = (LinearLayout) findViewById(R.id.fraternityView);

        /**
         * Call for All fraternities
         */
        new JsonTask().execute("https://s3.us-east-2.amazonaws.com/rushmepublic/fraternites.rushme");
        JsonTask task = new JsonTask();

        /**
         * Generate a bunch of clickables for fraternities
         */
        for (int i = 0; i < 100; i++) {
            btn = new Button(MainActivity.this);
            btn.setHeight(300);
            btn.setId(i + 1);
            btn.setText(frat1.getName());
            btn.setTag(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFraternityProfile();
                }
            });
            parent.addView(btn);
        }

        fraternities = (ScrollView) findViewById(R.id.fraternities);

        /**
         * Generate Calendar clickable
         */
        Button calendarBtn = (Button) findViewById(R.id.calendarBtn);
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });

        /**
         * Generate Map clickable
         */
        Button mapBtn = (Button) findViewById(R.id.btnMap);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });
    }

    /**
     * Store fraternity info in an intent and move to fraternityprofile
     */
    public void openFraternityProfile() {
        Intent intent = new Intent(this, FraternityProfile.class);
        intent.putExtra("fraternity", frat1.getName());
        intent.putExtra("chapter", frat1.getName());
        intent.putExtra("memberCount", frat1.getMemberCount());
        intent.putExtra("description", frat1.getDescription());
        startActivity(intent);
    }

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
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }
                JSONArray test;
                try {
                    test = new JSONArray(buffer.toString());
                    JSONObject temp = test.getJSONObject(0);
                    Log.d("Test", temp.getString("chapter"));
                }
                catch (JSONException e) {
                    Log.e(e.toString(), "way to go");
                }
                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            Log.d("Results", "> " + result);
            //txtJson.setText(result);
        }
    }
}