package com.costingrigore.dumbbellapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * This fragment is used for the user to select the difficulty and the body area target for the workout routine
 * Once the user sets the fields, the user can press the start workout button to be taken to the Workout activity
 * This is also the default fragment loaded when the application loads and the user has already filled the personal details form from StepOne
 * A simple {@link Fragment} subclass.
 * Use the {@link TrainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainerFragment extends Fragment {
    /**
     * Field storing the class' view
     */
    View view;
    /**
     * Initializing the input fields for the trainer fragment
     */
    Button startWorkoutButton;
    TextView dayText;
    CheckBox lowerBodyCB;
    CheckBox upperBodyCB;
    CheckBox totalBodyCB;
    CheckBox easyCB;
    CheckBox mediumCB;
    CheckBox difficultCB;
    /**
     * Firebase initialization
     */
    FirebaseDatabase database;
    /**
     * Initializing the strings used to store the workout's body area target, difficulty,
     * goal for using the application/working out and the level of experience of the user
     */
    String workoutBodyArea = "total_body";
    String workoutDifficulty = "easy";
    String workoutGoal = "Be fit";
    String workoutExperience = "Beginner";
    LinearLayout workoutSetUpLayout;

    /**
     * Public constructor of the fragment
     */
    public TrainerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment
     *
     * @return A new instance of fragment StepOne.
     */
    // TODO: Rename and change types and number of parameters
    public static TrainerFragment newInstance() {
        TrainerFragment fragment = new TrainerFragment();
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
    }

    /**
     * This method is used to store this fragments' fields into an intent
     * The intent is then used to pass this fragment's data to the Workout Activity
     */
    public void openNewActivity() {
        //Creating intent
        Intent intent = new Intent(view.getContext(), WorkoutActivity.class);
        //Storing this fragment's fields into the intent
        intent.putExtra("Body Area", workoutBodyArea);
        intent.putExtra("Difficulty", workoutDifficulty);
        intent.putExtra("Level of experience", workoutExperience);
        intent.putExtra("Goal", workoutGoal);
        startActivity(intent);
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_trainer, container, false);
        //Getting the firebase's instance
        database = FirebaseDatabase.getInstance();
        //Getting the user's personal ID using the GetPersonalID method
        String ID = GetPersonalID(view);
        //Getting the database's reference, using the user's personal ID
        DatabaseReference databaseReference = database.getReference("users").child(ID);
        /**
         * Setting the fragment's TextBoxes, CheckBoxes and button
         */
        startWorkoutButton = (Button) view.findViewById(R.id.button);
        dayText = (TextView) view.findViewById(R.id.dayText);
        lowerBodyCB = (CheckBox) view.findViewById(R.id.lower_body);
        upperBodyCB = (CheckBox) view.findViewById(R.id.upper_body);
        totalBodyCB = (CheckBox) view.findViewById(R.id.total_body);
        easyCB = (CheckBox) view.findViewById(R.id.easy);
        mediumCB = (CheckBox) view.findViewById(R.id.medium);
        difficultCB = (CheckBox) view.findViewById(R.id.difficult);
        workoutSetUpLayout = (LinearLayout) view.findViewById(R.id.workout_setup_layout);
        //Setting the start workout button onClick listener
        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the user's clicks the button a new activity opens, using the openNewActivity method
                openNewActivity();
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //String used to store the days of the week the user wishes to exercise
                ArrayList<String> days = new ArrayList<>();
                //Getting the user's level of experience from the database
                workoutExperience = snapshot.child("levelOfExperience").getValue(String.class);
                //Getting the user's goal for using the application from the database
                workoutGoal = snapshot.child("purpose").getValue(String.class);
                //Getting the days the user wishes to exercise from the database
                for (DataSnapshot snapshot_day : snapshot.child("days").getChildren()) {
                    String day = snapshot_day.getValue(String.class);
                    days.add(day);
                }
                /**
                 * Parsing the array list storing the days of the week the user wishes to exercise
                 * to a regular array
                 */
                String daysArray[] = days.toArray(new String[days.size()]);
                //Creating a new string buffer
                StringBuffer sb = new StringBuffer();
                /**
                 * The following for loop is used to add a space between every day of the week,
                 * adding the and word and a dot at the end of the sentence containing the
                 * days of the week the user wishes to exercise.
                 * The result should look like the following:
                 * Monday, Tuesday and Sunday.
                 */
                for (int i = 0; i < daysArray.length; i++) {
                    if (i == daysArray.length - 2) {
                        sb.append(daysArray[i] + " and ");
                    } else if (i == daysArray.length - 1) {
                        sb.append(daysArray[i] + ".");
                    } else {
                        sb.append(daysArray[i] + " ");
                    }
                }
                //Storing the string buffer into a string
                String daysString = sb.toString();
                //Creating a calendar object
                Calendar calendar = Calendar.getInstance();
                //Getting the current day of the week, using the calendar object's instance
                String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                //If the days array list containing the days of the week the user wishes to workout contains the current day of the week
                if (days.contains(day)) {
                    dayText.setText("Are you ready for your  " + day + " workout?\nSelect what body area you would like to target and the difficulty, and press 'Start', once you are ready.");
                    workoutSetUpLayout.setVisibility(View.VISIBLE);
                }
                //If it does not contain it
                else {
                    dayText.setText("Today is " + day + ".\nYou do not have any workouts planned for today, as your workout days are: " + daysString + "\nYou can change your workout active days in the Profile page.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /**
         * If the lower body check box is selected, the other check boxes get unselected and the workout body area target is set to lower body
         */
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
        /**
         * If the upper body check box is selected, the other check boxes get unselected and the workout body area target is set to upper body
         */
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
        /**
         * If the total body check box is selected, the other check boxes get unselected and the workout body area target is set to total body
         */
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
        /**
         * If the easy check box is selected, the other check boxes get unselected and the workout difficulty is set to easy
         */
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
        /**
         * If the medium check box is selected, the other check boxes get unselected and the workout difficulty is set to medium
         */
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
        /**
         * If the difficult check box is selected, the other check boxes get unselected and the workout difficulty is set to difficult
         */
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

    /**
     * This method is used to retrieve the user's personal ID from the device's storage
     *
     * @param view The fragment's view
     * @return It returns the user's personal ID as a String
     */
    public String GetPersonalID(View view) {
        //Try reading the contents of the file containing the user's personal ID
        try {
            //Creating a file input stream, using user_data.txt as the input file
            FileInputStream fileIn = view.getContext().openFileInput("user_data.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            //Setting a character array to store the first 100 characters of the input stream
            char[] inputBuffer = new char[100];
            //String used to store the user's personal ID
            String userPersonalID = "";
            //String array used to store the different parts of the input stream
            String[] strArray = new String[100];
            int charRead;
            //While there is something to read from the file
            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                //A semicolon is used to split the read string from the input buffer
                strArray = readstring.split(";");
            }
            //Close the input reader
            InputRead.close();
            //Set the user personal ID to the second element of the string array
            userPersonalID = strArray[1];
            //Return the the user's personal ID
            return userPersonalID;
        } catch (Exception e) {
            e.printStackTrace();
            //If the file could not be read, return "String not found"
            return "String not found";
        }
    }
}