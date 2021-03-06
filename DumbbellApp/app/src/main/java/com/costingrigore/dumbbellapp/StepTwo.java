package com.costingrigore.dumbbellapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

/**
 * This fragment is used to display a description of each of the available pages in the application
 * A simple {@link Fragment} subclass.
 * Use the {@link StepTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepTwo extends Fragment {
    /**
     * Field storing the class' view
     */
    View view;
    /**
     * Initializing the next button of the fragment
     */
    Button nextButton;
    /**
     * Public constructor of the class
     */
    public StepTwo() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StepTwo.
     */
    // TODO: Rename and change types and number of parameters
    public static StepTwo newInstance(String param1, String param2) {
        StepTwo fragment = new StepTwo();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * Called when the fragment gets created
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     * Called when the fragment's view is created
     * @param inflater Fragment's inflater
     * @param container Fragment's ViewGroup
     * @param savedInstanceState Fragment's instance
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_step_two, container, false);
        //Setting the next button
        nextButton = (Button) view.findViewById(R.id.nextButton);
        //Setting the next button's onClick listener
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating a fragment transaction
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                //Creating a new TrainerFragment fragment
                Fragment fragment = new TrainerFragment();
                //Replace the current fragment (StepTwo) by the fragment TrainerFragment
                ft.replace(R.id.container, fragment, "Trainer Fragment").commit();
                //Finding the bottom navigation bar
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
                //Making the bottom navigation view visible
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
}