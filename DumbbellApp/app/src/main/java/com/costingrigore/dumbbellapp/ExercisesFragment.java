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
import android.view.ViewGroup;

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
    ExercisesFragmentRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExercisesFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param columnCount Number of columns the layout will have
     * @return A new instance of fragment ExercisesFragment.
     */
    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ExercisesFragment newInstance(int columnCount) {
        ExercisesFragment fragment = new ExercisesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment gets created
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    /**
     * Called when the fragment's view is created
     *
     * @param inflater           Fragment's inflater
     * @param container          Fragment's ViewGroup
     * @param savedInstanceState Fragment's instance
     * @return It returns the fragments' view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercises_list, container, false);
        //Setting the context of this view
        Context context = view.getContext();
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        //Setting the recycler view for this layout
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(llm);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        //Array list used to store all the available exercises in the database
        final ArrayList<Exercise> exercises = new ArrayList<>();
        //Firebase initialisation
        DatabaseReference databaseReference = database.getReference("exercises");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Setting temporal variables to store the exercise type, body part target and difficulty of the exercises
                String exercise_type = "";
                String body_part = "";
                String difficulty = "";
                /**
                 * The following for loops are used to iterate through all the exercises
                 * available in the database and store them into the previous array list (exercises)
                 */
                for (DataSnapshot snapshot_type : dataSnapshot.getChildren()) {
                    exercise_type = snapshot_type.getKey();
                    for (DataSnapshot snapshot_body_part : snapshot_type.getChildren()) {
                        body_part = snapshot_body_part.getKey();
                        for (DataSnapshot snapshot_difficulty : snapshot_body_part.getChildren()) {
                            difficulty = snapshot_difficulty.getKey();
                            for (DataSnapshot snapshot_exercise_id : snapshot_difficulty.getChildren()) {
                                for (DataSnapshot snapshot_exercise_name : snapshot_exercise_id.getChildren()) {
                                    //Setting the exercise fields
                                    String name = snapshot_exercise_name.getValue(String.class);
                                    Exercise exercise1 = new Exercise();
                                    String fnm = name; //  this is image file name
                                    String PACKAGE_NAME = getContext().getPackageName();
                                    int imgId = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + fnm, null, null);
                                    exercise1.setIcon(imgId);
                                    name = replace(name);
                                    exercise1.setName(name);
                                    difficulty = replace(difficulty);
                                    exercise1.setDifficulty(difficulty);
                                    exercise_type = replace(exercise_type);
                                    exercise1.setType(exercise_type);
                                    body_part = replace(body_part);
                                    exercise1.setBody_part(body_part);
                                    //Add exercise to the array list
                                    exercises.add(exercise1);
                                }
                            }
                        }
                    }
                }
                //If the array list storing the exercises is not empty
                if (!exercises.isEmpty()) {
                    //Set the adapter for the recycler view, using the array list containing all the available exercises in the database
                    adapter = new ExercisesFragmentRecyclerViewAdapter(exercises);
                    recyclerView.setAdapter(adapter);
                    //Notify data has been added to the adapter
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
        return view;
    }

    /**
     * Capitalizes the input string and replaces the under score values from the input string to space values
     * I used the following source to implement this method:
     * https://codereview.stackexchange.com/questions/188996/replace-any-number-of-consecutive-underscores-with-a-single-space
     *
     * @param input input string
     * @return returns changed string
     */
    public static String replace(String input) {
        String spacesReplaced = replace('_', ' ', input);
        String finalName = spacesReplaced.substring(0, 1).toUpperCase() + spacesReplaced.substring(1);
        return finalName;
    }

    /**
     * Replaces the under score values from the input string to space values
     * I used the following source to implement this method:
     * https://codereview.stackexchange.com/questions/188996/replace-any-number-of-consecutive-underscores-with-a-single-space
     *
     * @param oldDelim delimiter to be replaced
     * @param newDelim delimiter replaced by
     * @param input    input string
     * @return returns changed string
     */
    public static String replace(char oldDelim, char newDelim, String input) {
        boolean wasOldDelim = false;
        int o = 0;
        char[] buf = input.toCharArray();
        for (int i = 0; i < buf.length; i++) {
            assert (o <= i);
            if (buf[i] == oldDelim) {
                if (wasOldDelim) {
                    continue;
                }
                wasOldDelim = true;
                buf[o++] = newDelim;
            } else {
                wasOldDelim = false;
                buf[o++] = buf[i];
            }
        }
        return new String(buf, 0, o);
    }
}