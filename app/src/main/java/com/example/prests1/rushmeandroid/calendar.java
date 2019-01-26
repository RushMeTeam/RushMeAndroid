package com.example.prests1.rushmeandroid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class calendar extends AppCompatActivity {

    ListView parent;
    TextView test;
    private Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        parent = (ListView) findViewById(R.id.calendarList);

        CalendarView rushCalendar = (CalendarView) findViewById(R.id.rushCalendar);

        String rushStartStr = "01-01-2019";
        String rushEndStr = "28-02-2019";
        /**
         * Set date bounds
         */
        try{
            Date rushStartDate = formatter.parse(rushStartStr);
            rushCalendar.setMinDate(rushStartDate.getTime());
            Date rushEndDate = formatter.parse(rushEndStr);
            rushCalendar.setMaxDate((rushEndDate.getTime()));
        }catch (ParseException e) {
            e.printStackTrace();
        }

        final ArrayList<String> searchResults = GetSearchResults();

        final ListView lv = (ListView) findViewById(R.id.calendarList);
        adapter = new Adapter(this, searchResults);
        lv.setAdapter(adapter);

        rushCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                searchResults.add(Integer.toString(dayOfMonth));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private ArrayList<String> GetSearchResults(){
        ArrayList<String> results = new ArrayList<String>();

        results.add("Test");

        return results;
    }
}
