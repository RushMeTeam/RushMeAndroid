package com.example.prests1.rushmeandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FraternityProfile extends AppCompatActivity {
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fraternity_profile);

        String fraternityName;
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras==null){
                fraternityName = null;
            } else {
                fraternityName = extras.getString("name");
            }
        } else {
            fraternityName = (String) savedInstanceState.getSerializable("name");
        }

        setTitle(fraternityName);

    }
}
