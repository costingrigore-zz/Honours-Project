package com.costingrigore.dumbbellapp;

import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyExerciseRecyclerViewAdapter extends RecyclerView.Adapter<MyExerciseRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Exercise> mValues;

    public MyExerciseRecyclerViewAdapter(ArrayList<Exercise> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_exercises, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mImageView.setImageResource(mValues.get(position).getIcon());
        holder.mNameView.setText(mValues.get(position).name);
        holder.mDifficultyView.setText(mValues.get(position).difficulty);
        holder.mTypeView.setText(mValues.get(position).type);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mNameView;
        public final TextView mDifficultyView;
        public final TextView mTypeView;
        public Exercise mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.exerciseID);
            mNameView = (TextView) view.findViewById(R.id.name);
            mDifficultyView = (TextView) view.findViewById(R.id.difficulty);
            mTypeView = (TextView) view.findViewById(R.id.type);
        }

    }
}