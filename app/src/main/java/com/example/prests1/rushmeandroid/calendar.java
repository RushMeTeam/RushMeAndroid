package com.example.prests1.rushmeandroid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class calendar extends AppCompatActivity {

    LinearLayout parent;
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        parent = (LinearLayout) findViewById(R.id.calendarView);

        CalendarView rushCalendar = (CalendarView) findViewById(R.id.rushCalendar);
        /**
         * Clickable Dates **Not Working**
         */
        rushCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //test = new TextView(calendar.this);
                //test.setHeight(300);
                //test.setText(dayOfMonth);
                //parent.addView(test);
            }
        });


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
    }
}