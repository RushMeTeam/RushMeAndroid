package com.example.prests1.rushmeandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class FraternityProfile extends AppCompatActivity {
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fraternity_profile);

        String fraternityName;
        String chapterName;
        int memberCount;
        String description;
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras==null){
                fraternityName = null;
                chapterName = null;
                memberCount = 0;
                description = null;
            } else {
                fraternityName = extras.getString("fraternity");
                chapterName = extras.getString("chapter");
                description = extras.getString("description");
                memberCount = extras.getInt("memberCount");
            }
        } else {
            fraternityName = (String) savedInstanceState.getSerializable("fraternity");
            chapterName = (String) savedInstanceState.getSerializable("chapter");
            memberCount = (int) savedInstanceState.getSerializable("memberCount");
            description = (String) savedInstanceState.getSerializable("description");
        }

        TextView chapterView = (TextView) findViewById(R.id.chapter);
        chapterView.setText(chapterName);

        TextView memberCountView = (TextView) findViewById(R.id.memberCount);
        memberCountView.setText("" + memberCount);

        TextView descriptionView = (TextView) findViewById(R.id.description);
        descriptionView.setText(description);

        setTitle(fraternityName);

        ImageView chapterImage = (ImageView) findViewById(R.id.chapterImage);

        /*
        Glide.with(context)
                .load("http://via.placeholder.com/300.png")
                .into(chapterImage);
        */
    }
}
