package com.example.prests1.rushmeandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class FraternityDetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fraternity_detail);
        
        Intent i = getIntent();
        Fraternity fraternity = (Fraternity) i.getParcelableExtra("Fraternity");

        TextView fratName = (TextView) findViewById(R.id.titleView);
        fratName.setText(fraternity.getName());

        TextView chapter = (TextView) findViewById(R.id.chapter);
        chapter.setText(fraternity.getChapter());

        TextView description = (TextView) findViewById(R.id.description);
        description.setText(fraternity.getDescription());

        TextView memberCount = (TextView) findViewById(R.id.memberCount);
        if (fraternity.getMemberCount() == 1) {
            memberCount.setText("1 member");
        } else {
            memberCount.setText(Integer.toString(fraternity.getMemberCount()) + " members");
        }

    }
}
