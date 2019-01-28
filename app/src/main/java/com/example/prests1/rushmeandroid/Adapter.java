package com.example.prests1.rushmeandroid;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    private static ArrayList<Fraternity.Event> searchArrayList;

    private LayoutInflater mInflater;

    public Adapter(Context context, ArrayList<Fraternity.Event> results){
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount(){
        return searchArrayList.size();
    }

    public Fraternity.Event getItem(int position){
        return searchArrayList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.custom_row_view, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(searchArrayList.get(position).name);

        return convertView;
    }

    static class ViewHolder {
        TextView txtName;
    }
}
