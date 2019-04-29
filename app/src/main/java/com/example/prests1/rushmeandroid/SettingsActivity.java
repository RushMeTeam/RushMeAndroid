package com.example.prests1.rushmeandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

// Initializes the Settings page and creates the layout specified in activity_settings.xml
public class SettingsActivity extends AppCompatActivity {

    TextView versionName; // The Version Number
    TextView versionCode; // The Build Number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initializes variables for retrieving and displaying the version and build number
        versionName = (TextView) findViewById(R.id.textView2);
        versionCode = (TextView) findViewById(R.id.textView4);

        String vName = BuildConfig.VERSION_NAME;
        int vCode = BuildConfig.VERSION_CODE;

        versionName.setText(vName);
        versionCode.setText(String.valueOf(vCode));
    }
}
