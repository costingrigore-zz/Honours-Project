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
 * A simple {@link Fragment} subclass.
 * Use the {@link StepTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepTwo extends Fragment {

    View view;
    Button nextButton;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_step_two, container, false);
        nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = new TrainerFragment();
                ft.replace(R.id.container, fragment, "Trainer Fragment").commit();
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
}