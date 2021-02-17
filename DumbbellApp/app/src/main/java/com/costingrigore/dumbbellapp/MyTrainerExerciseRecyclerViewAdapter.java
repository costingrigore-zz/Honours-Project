package com.costingrigore.dumbbellapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTrainerExerciseRecyclerViewAdapter extends RecyclerView.Adapter<MyTrainerExerciseRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Exercise> mValues;

    public MyTrainerExerciseRecyclerViewAdapter(ArrayList<Exercise> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trainer_exercises, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mImageView.setImageResource(mValues.get(position).getIcon());
        holder.mNameView.setText(mValues.get(position).name);
        holder.mDifficultyView.setText(mValues.get(position).difficulty);
        holder.mBodyPartView.setText(mValues.get(position).body_part);
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
        public final TextView mBodyPartView;
        public Exercise mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.exerciseID);
            mNameView = (TextView) view.findViewById(R.id.name);
            mDifficultyView = (TextView) view.findViewById(R.id.difficulty);
            mBodyPartView = (TextView) view.findViewById(R.id.body_part);
        }

    }
}