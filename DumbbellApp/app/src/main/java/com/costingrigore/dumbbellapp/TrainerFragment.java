package com.costingrigore.dumbbellapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainerFragment extends Fragment {

    TextView textView;
    Button button;
    View view;
    FirebaseDatabase database;
    TextView dayText;
    CheckBox lowerBodyCB;
    CheckBox upperBodyCB;
    CheckBox totalBodyCB;
    CheckBox easyCB;
    CheckBox mediumCB;
    CheckBox difficultCB;
    String workoutBodyArea = "total_body";
    String workoutDifficulty = "easy";
    public TrainerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TrainerFragment newInstance() {
        TrainerFragment fragment = new TrainerFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void openNewActivity(){
        Intent intent = new Intent(view.getContext(), WorkoutActivity.class);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trainer, container, false);
        button = (Button) view.findViewById(R.id.button);
        dayText = (TextView) view.findViewById(R.id.dayText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });
        database = FirebaseDatabase.getInstance();
        String ID = GetPersonalID(view);
        DatabaseReference databaseReference = database.getReference("users").child(ID);
        lowerBodyCB = (CheckBox) view.findViewById(R.id.lower_body);
        upperBodyCB = (CheckBox) view.findViewById(R.id.upper_body);
        totalBodyCB = (CheckBox) view.findViewById(R.id.total_body);
        easyCB = (CheckBox) view.findViewById(R.id.easy);
        mediumCB = (CheckBox) view.findViewById(R.id.medium);
        difficultCB = (CheckBox) view.findViewById(R.id.difficult);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> days = new ArrayList<>();
                for (DataSnapshot snapshot_day : snapshot.child("days").getChildren()) {
                    String day = snapshot_day.getValue(String.class);
                    days.add(day);
                    }
                String daysArray[] = days.toArray(new String[days.size()]);
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < daysArray.length; i++) {
                    if(i == daysArray.length - 2) {
                        sb.append(daysArray[i] + " and ");
                    }
                    else if(i == daysArray.length - 1){
                        sb.append(daysArray[i] + ".");
                    }
                    else{
                        sb.append(daysArray[i] + " ");
                    }
                }
                String daysString = sb.toString();
                Calendar calendar = Calendar.getInstance();
                String day =calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.getDefault());
                System.out.println("dayz " + day + " "+ days);
                if(days.contains(day)){
                    dayText.setText("Are you ready for your  " + day + " workout?\nSelect what body area you would like to target and the difficulty, and press 'Start', once you are ready.");
                    System.out.println("dayz " + day);
                }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        lowerBodyCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lowerBodyCB.isChecked()) {
                    lowerBodyCB.setChecked(true);
                    upperBodyCB.setChecked(false);
                    totalBodyCB.setChecked(false);
                    workoutBodyArea = "lower_body";
                }
            }
        });
        upperBodyCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upperBodyCB.isChecked()) {
                    lowerBodyCB.setChecked(false);
                    upperBodyCB.setChecked(true);
                    totalBodyCB.setChecked(false);
                    workoutBodyArea = "upper_body";
                }
            }
        });
        totalBodyCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalBodyCB.isChecked()) {
                    lowerBodyCB.setChecked(false);
                    upperBodyCB.setChecked(false);
                    totalBodyCB.setChecked(true);
                    workoutBodyArea = "total_body";
                }
            }
        });
        easyCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (easyCB.isChecked()) {
                    easyCB.setChecked(true);
                    mediumCB.setChecked(false);
                    difficultCB.setChecked(false);
                    workoutDifficulty = "easy";
                }
            }
        });
        mediumCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediumCB.isChecked()) {
                    easyCB.setChecked(false);
                    mediumCB.setChecked(true);
                    difficultCB.setChecked(false);
                    workoutDifficulty = "medium";
                }
            }
        });
        difficultCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (difficultCB.isChecked()) {
                    easyCB.setChecked(false);
                    mediumCB.setChecked(false);
                    difficultCB.setChecked(true);
                    workoutDifficulty = "difficult";
                }
            }
        });
        return view;
    }
    public String GetPersonalID(View view) {
        try {
            FileInputStream fileIn = view.getContext().openFileInput("user_data.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[100];
            String s = "";
            String[] strArray = new String[100];
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                strArray = readstring.split(";");
            }
            InputRead.close();
            s = strArray[1];
            System.out.println("user " + s);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return "String not found";
        }
    }
}