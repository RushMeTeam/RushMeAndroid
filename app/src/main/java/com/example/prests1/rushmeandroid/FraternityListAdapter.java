package com.example.prests1.rushmeandroid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FraternityListAdapter extends RecyclerView.Adapter<FraternityListAdapter.ViewHolder> {
    //private HashMap<String, Fraternity> mData;
    private ArrayList<Fraternity> mData;
    private LayoutInflater mInflater;
    private EventRecyclerViewAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    FraternityListAdapter(Context context, ArrayList<Fraternity> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void updateData(ArrayList<Fraternity> frats) {
        mData = new ArrayList<Fraternity>();
        mData.clear();

        /*
        for (Map.Entry<String, Fraternity> frat : frats.entrySet()) {
            mData.put(frat.getKey(), frat.getValue());
        }
        */
        mData.addAll(frats);
        this.notifyDataSetChanged();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fraternity frat = mData.get(position);
        holder.FraternityNameTV.setText(frat.getName().toUpperCase());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView FraternityNameTV;

        ViewHolder(View itemView) {
            super(itemView);
            FraternityNameTV = itemView.findViewById(R.id.FraternityNameTV);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).getName();
    }

    // allows clicks events to be caught
    void setClickListener(EventRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
