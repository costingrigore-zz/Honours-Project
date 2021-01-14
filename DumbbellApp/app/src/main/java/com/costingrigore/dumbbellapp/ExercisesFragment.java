package com.costingrigore.dumbbellapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.costingrigore.dumbbellapp.dummy.DummyContent;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ExercisesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

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

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercises_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            ArrayList exerciseList = getListData();
            recyclerView.setAdapter(new MyExerciseRecyclerViewAdapter(exerciseList));
        }
        return view;
    }

    private ArrayList getListData() {
        ArrayList<Exercise> results = new ArrayList<>();
        Exercise exercise1 = new Exercise();
        exercise1.setIcon(R.drawable.india);
        exercise1.setName("Suresh Dasari");
        exercise1.setDifficulty("Team Leader");
        exercise1.setType("Hyderabad");
        results.add(exercise1);
        Exercise exercise2 = new Exercise();
        exercise2.setIcon(R.drawable.india);
        exercise2.setName("Rohini Alavala");
        exercise2.setDifficulty("Agricultural Officer");
        exercise2.setType("Guntur");
        results.add(exercise2);
        Exercise exercise3 = new Exercise();
        exercise3.setIcon(R.drawable.india);
        exercise3.setName("Trishika Dasari");
        exercise3.setDifficulty("Charted Accountant");
        exercise3.setType("Guntur");
        results.add(exercise3);
        return results;
    }
}