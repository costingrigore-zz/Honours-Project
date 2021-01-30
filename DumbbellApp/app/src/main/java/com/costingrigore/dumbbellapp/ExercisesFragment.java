package com.costingrigore.dumbbellapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ExercisesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    FirebaseDatabase database;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExercisesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ExercisesFragment newInstance(int columnCount) {
        ExercisesFragment fragment = new ExercisesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercises_list, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(llm);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            // Firebase initialisation
            final ArrayList<Exercise> exercises = new ArrayList<>();
            final MyExerciseRecyclerViewAdapter adapter = new MyExerciseRecyclerViewAdapter(exercises);
            recyclerView.setAdapter(adapter);
            DatabaseReference databaseReference = database.getReference("exercises");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // for cardio
                    String exercise_type = "";
                    String body_part = "";
                    String difficulty = "";
                    for ( DataSnapshot snapshot_type : dataSnapshot.getChildren()) {
                        exercise_type = snapshot_type.getKey();
                        for (DataSnapshot snapshot_body_part : snapshot_type.getChildren()) {
                            body_part = snapshot_body_part.getKey();
                            for (DataSnapshot snapshot_difficulty : snapshot_body_part.getChildren()) {
                                difficulty = snapshot_difficulty.getKey();
                                for (DataSnapshot snapshot_exercise_id : snapshot_difficulty.getChildren()) {
                                    for (DataSnapshot snapshot_exercise_name : snapshot_exercise_id.getChildren()) {

                                        String name = snapshot_exercise_name.getValue(String.class);
                                        Exercise exercise1 = new Exercise();
                                        String fnm = name; //  this is image file name
                                        String PACKAGE_NAME = getContext().getPackageName();
                                        int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);
                                        exercise1.setIcon(imgId);
                                        exercise1.setName(name);
                                        exercise1.setDifficulty(difficulty);
                                        exercise1.setType(exercise_type);
                                        exercise1.setBody_part(body_part);
                                        exercises.add(exercise1);
                                    }
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }

            });

        }
        return view;
    }
}