package com.example.prests1.rushmeandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class calendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        CalendarView rushCalendar = (CalendarView) findViewById(R.id.rushCalendar);
        String rushStartStr = "01-01-2019";
        String rushEndStr = "28-02-2019";
        try{
            Date rushStartDate = formatter.parse(rushStartStr);
            rushCalendar.setMinDate(rushStartDate.getTime());
            Date rushEndDate = formatter.parse(rushEndStr);
            rushCalendar.setMaxDate((rushEndDate.getTime()));
        }catch (ParseException e){
            e.printStackTrace();
        }
    }
}
