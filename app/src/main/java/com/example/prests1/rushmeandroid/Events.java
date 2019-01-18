package com.example.prests1.rushmeandroid;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Events extends AppCompatActivity {

    ArrayList<Fraternity.Event> events = new ArrayList<Fraternity.Event>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        /* Get events from last activity */
        events = (ArrayList<Fraternity.Event>) getIntent().getSerializableExtra("events");

        Log.d("EVENTSSIZENEW", Integer.toString(events.size()));
        LinearLayout eventsLayout = (LinearLayout) findViewById(R.id.eventLayout);

        /* Add all events to event scroll container */
        for(int i=0; i<events.size(); ++i){
            Log.d("EVENTSS", " > " + events.get(i).name);
            /* Container prep for event */
            RelativeLayout eventContainer = new RelativeLayout(Events.this);
            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            RelativeLayout.LayoutParams eventContainerLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) pixels);
            eventContainerLayout.setMargins(5,5,5,5);
            eventContainer.setPadding(5,5,5,5);
            eventContainer.setLayoutParams(eventContainerLayout);
            eventContainer.setBackgroundResource(R.drawable.rounded_corner);

            /* TextView prep for event name */
            TextView eventName = new TextView(Events.this);
            RelativeLayout.LayoutParams eventNameLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            eventNameLayout.addRule(RelativeLayout.CENTER_VERTICAL);
            eventNameLayout.setMargins(100,0,0,0);
            eventName.setLayoutParams(eventNameLayout);
            eventName.setText(events.get(i).name);
            eventName.setTextSize(35);
            eventContainer.addView(eventName);

            /* Subscribe button prep for event */
            CheckBox subscribe = new CheckBox(Events.this);
            RelativeLayout.LayoutParams subscribeLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            subscribeLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            subscribeLayout.addRule((RelativeLayout.CENTER_VERTICAL));
            subscribeLayout.setMargins(0,0,50,0);
            subscribe.setLayoutParams(subscribeLayout);
            subscribe.setScaleX(1.5f);
            subscribe.setScaleY(1.5f);
            eventContainer.addView(subscribe);

            eventsLayout.addView(eventContainer);
        }

    }
}
