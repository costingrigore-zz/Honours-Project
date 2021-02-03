package com.costingrigore.dumbbellapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainerFragment extends Fragment {

    TextView textView;

    String name;

    public TrainerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TrainerFragment newInstance() {
        TrainerFragment fragment = new TrainerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainer, container, false);
        FirebaseDatabase database;
        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("exercises").child("cardio").child("lower_body").child("easy").child("2").child("name");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.getValue(String.class);
                textView= (TextView) view.findViewById(R.id.name);
                textView.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}