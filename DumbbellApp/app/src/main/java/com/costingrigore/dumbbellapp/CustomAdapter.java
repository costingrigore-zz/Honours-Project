package com.costingrigore.dumbbellapp;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
public class CustomAdapter extends BaseAdapter {
    private ArrayList<Exercise> listData;
    private LayoutInflater layoutInflater;
    public CustomAdapter(Context aContext, ArrayList<Exercise> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }
    @Override
    public int getCount() {
        return listData.size();
    }
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View v, ViewGroup vg) {
        ViewHolder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.exerciseID = (ImageView) v.findViewById(R.id.exerciseID);
            holder.uName = (TextView) v.findViewById(R.id.name);
            holder.uDifficulty = (TextView) v.findViewById(R.id.difficulty);
            holder.uType = (TextView) v.findViewById(R.id.type);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.exerciseID.setImageResource(listData.get(position).getIcon());
        holder.uName.setText(listData.get(position).getName());
        holder.uDifficulty.setText(listData.get(position).getDifficulty());
        holder.uType.setText(listData.get(position).getType());
        return v;
    }
    static class ViewHolder {
        ImageView exerciseID;
        TextView uName;
        TextView uDifficulty;
        TextView uType;
    }
}