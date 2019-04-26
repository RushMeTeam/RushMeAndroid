package com.example.prests1.rushmeandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FraternityList extends AppCompatActivity implements FraternityListAdapter.ItemClickListener {

    HashMap<String, Fraternity> fraternities = new HashMap<String, Fraternity>();
    FraternityListAdapter adapter;
    ArrayList<Fraternity> frats = new ArrayList<Fraternity>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fraternity_list);

        Campus campus = (Campus)getApplication();
        fraternities = campus.getFrats();
        Log.d("FRAT LIST FRATS: ", Integer.toString(fraternities.size()));

        for (Map.Entry<String, Fraternity> frat : fraternities.entrySet()) {
            frats.add(frat.getValue());
        }

        final RecyclerView rv = findViewById(R.id.fraternitiesList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FraternityListAdapter(this, frats);
        adapter.setClickListener(this);
        rv.setAdapter(adapter);
    }

    /**
     * onItemClick is called when a person selects a fraternities event from the selectedEvents RecyclerView
     * @param view
     * @param position
     */
    public void onItemClick(View view, int position) {
        if (position >= 0 && position < fraternities.size()) {
            Fraternity fraternity = frats.get(position); /* Grab fraternity based on position clicked */
            startActivityFor(fraternity); /* call startActivityFor function which loads individual fraternity detail */
        }
    }

    /**
     * Bundles up a fraternity into an Intent with the selected Fraternity. FraternityDetail activity is started with specified intent
     * @param fraternity
     */
    private void startActivityFor(Fraternity fraternity) {
        ActionLogging.Log("Fraternity selected", fraternity.getName());
        Intent intent = new Intent(this, FraternityDetail.class);
        intent.putExtra("Fraternity", fraternity);
        startActivity(intent);
    }
}
