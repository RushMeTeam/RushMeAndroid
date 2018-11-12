package com.example.prests1.rushmeandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity{
LinearLayout parent;
    Button btn;
    ScrollView fraternities;
    Fraternity frat1 = new Fraternity("Zoo", "Zoo", 11, "test");
    Fraternity frat2 = new Fraternity("Chi Phi", "something", 100, "this is chi phi");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);

        parent = (LinearLayout)findViewById(R.id.fraternityView);

        //Filling a temporary array for the sake of testing the app's offline fuctionality.
        for(int i=0; i<100; i++){
            btn = new Button(MainActivity.this);
            btn.setHeight(300);
            btn.setId(i+1);
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

        fraternities = (ScrollView)findViewById(R.id.fraternities);

    }

    public void openFraternityProfile() {
        Intent intent = new Intent(this, FraternityProfile.class);
        intent.putExtra("name", frat1.getName());
        startActivity(intent);
    }
}