package com.example.prests1.rushmeandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class FraternityList extends AppCompatActivity {
    Campus campus;
    HashMap<String, Fraternity> fraternities;
    HashMap<String, Fraternity> fraternitiesByKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fraternity_list);

        campus = ((Campus) getApplicationContext());
        fraternities = campus.getFrats();
        fraternitiesByKey = campus.getFratsByKey();
    }
    
}
