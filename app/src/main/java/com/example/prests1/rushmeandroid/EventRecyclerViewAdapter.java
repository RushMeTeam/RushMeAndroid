package com.example.prests1.rushmeandroid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.client.util.DateTime;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {

    private List<Fraternity.Event> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    EventRecyclerViewAdapter(Context context, List<Fraternity.Event> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void updateData(ArrayList<Fraternity.Event> events) {
        mData = new ArrayList<Fraternity.Event>();
        mData.clear();
        mData.addAll(events);
        notifyDataSetChanged();
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
        Fraternity.Event event = mData.get(position);
        holder.fratNameTV.setText(event.frat.getName().toUpperCase() + " | " + event.location);
        holder.eventNameTV.setText(event.name);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String strOut = (df.format("MM.dd.yy h:mm a", event.starting)).toString();
        strOut += " - " + (df.format("h:mm a", event.ending)).toString();
        holder.infoTV.setText(strOut);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView fratNameTV;
        TextView eventNameTV;
        TextView infoTV;

        ViewHolder(View itemView) {
            super(itemView);
            eventNameTV = itemView.findViewById(R.id.eventNameTV);
            fratNameTV = itemView.findViewById(R.id.fratNameTV);
            infoTV = itemView.findViewById(R.id.infoTV);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).name;
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
