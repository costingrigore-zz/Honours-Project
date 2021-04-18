package com.costingrigore.dumbbellapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Whenever the user opens the application for the first time, this fragment is used to retrieve personal data from the user to be used for creating workout routines
 * A simple {@link Fragment} subclass.
 * Use the {@link StepOne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepOne extends Fragment {
    /**
     * Field storing the class' view
     */
    View view;
    /**
     * Initializing the input fields and the button for the data retrieval form for the fragment
     */
    EditText editTextWeight;
    EditText editTextHeight;
    EditText editTextAge;
    CheckBox beginner, intermediate, advanced, professional, gain_strength, lose_weight, be_fit, monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    Button nextButton;
    /**
     * String used to store the user's level of experience and goal for using the application
     */
    private String levelOfExperience;
    private String goalForUsingTheApplication;

    /**
     * Public constructor of the class
     */
    public StepOne() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StepOne.
     */
    // TODO: Rename and change types and number of parameters
    public static StepOne newInstance(String param1, String param2) {
        StepOne fragment = new StepOne();
        Bundle args = new Bundle();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_step_one, container, false);
        /**
         * Setting the fragment's EditText objects, CheckBoxes and button
         */
        editTextWeight = (EditText) view.findViewById(R.id.editTextWeight);
        editTextHeight = (EditText) view.findViewById(R.id.editTextHeight);
        editTextAge = (EditText) view.findViewById(R.id.editTextAge);
        nextButton = (Button) view.findViewById(R.id.nextButton);
        beginner = (CheckBox) view.findViewById(R.id.beginner);
        intermediate = (CheckBox) view.findViewById(R.id.intermediate);
        advanced = (CheckBox) view.findViewById(R.id.advanced);
        professional = (CheckBox) view.findViewById(R.id.professional);
        gain_strength = (CheckBox) view.findViewById(R.id.gain_strength);
        lose_weight = (CheckBox) view.findViewById(R.id.lose_weight);
        be_fit = (CheckBox) view.findViewById(R.id.be_fit);
        monday = (CheckBox) view.findViewById(R.id.monday);
        tuesday = (CheckBox) view.findViewById(R.id.tuesday);
        wednesday = (CheckBox) view.findViewById(R.id.wednesday);
        thursday = (CheckBox) view.findViewById(R.id.thursday);
        friday = (CheckBox) view.findViewById(R.id.friday);
        saturday = (CheckBox) view.findViewById(R.id.saturday);
        sunday = (CheckBox) view.findViewById(R.id.sunday);
        /**
         * If the beginner check box is selected, the other check boxes get unselected and the level of experience is set to beginner
         */
        beginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beginner.isChecked()) {
                    beginner.setChecked(true);
                    intermediate.setChecked(false);
                    advanced.setChecked(false);
                    professional.setChecked(false);
                    levelOfExperience = "Beginner";
                }
            }
        });
        /**
         * If the intermediate check box is selected, the other check boxes get unselected and the level of experience is set to intermediate
         */
        intermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intermediate.isChecked()) {
                    beginner.setChecked(false);
                    intermediate.setChecked(true);
                    advanced.setChecked(false);
                    professional.setChecked(false);
                    levelOfExperience = "Intermediate";
                }
            }
        });
        /**
         * If the advanced check box is selected, the other check boxes get unselected and the level of experience is set to advanced
         */
        advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (advanced.isChecked()) {
                    beginner.setChecked(false);
                    intermediate.setChecked(false);
                    advanced.setChecked(true);
                    professional.setChecked(false);
                    levelOfExperience = "Advanced";
                }
            }
        });
        /**
         * If the professional check box is selected, the other check boxes get unselected and the level of experience is set to professional
         */
        professional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (professional.isChecked()) {
                    beginner.setChecked(false);
                    intermediate.setChecked(false);
                    advanced.setChecked(false);
                    professional.setChecked(true);
                    levelOfExperience = "Professional";
                }
            }
        });
        /**
         * If the gain strength check box is selected, the other check boxes get unselected and the goal for using the application is set to gain strength
         */
        gain_strength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gain_strength.isChecked()) {
                    gain_strength.setChecked(true);
                    lose_weight.setChecked(false);
                    be_fit.setChecked(false);
                    goalForUsingTheApplication = "Gain strength";
                }
            }
        });
        /**
         * If the lose weight check box is selected, the other check boxes get unselected and the goal for using the application is set to lose weight
         */
        lose_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lose_weight.isChecked()) {
                    gain_strength.setChecked(false);
                    lose_weight.setChecked(true);
                    be_fit.setChecked(false);
                    goalForUsingTheApplication = "Lose weight";
                }
            }
        });
        /**
         * If the be fit check box is selected, the other check boxes get unselected and the goal for using the application is set to be fit
         */
        be_fit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (be_fit.isChecked()) {
                    gain_strength.setChecked(false);
                    lose_weight.setChecked(false);
                    be_fit.setChecked(true);
                    goalForUsingTheApplication = "Be fit";
                }
            }
        });
        /**
         * Setting the onClick listener for the next button
         */
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the weight, height and age from the edit text input fields and store them in temporal variables
                String weight = editTextWeight.getText().toString();
                String height = editTextHeight.getText().toString();
                String age = editTextAge.getText().toString();
                //Get the days the user wishes to exercise using the CheckDays method
                ArrayList<String> daysToExercise = CheckDays();
                //Saving user personal ID to local file and saving user personal data to FireBase
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //Creating random
                Random random = new Random();
                //Creating random number between 0 and 99999
                int x = random.nextInt(100000);
                //Personal ID consists of the current time in millis plus the random integer
                String personalID = System.currentTimeMillis() + String.valueOf(x);
                //Setting database reference
                DatabaseReference databaseReference = database.getReference("users");
                //Saving user's personal data to database, using the user's ID
                databaseReference.child(personalID).child("weight").setValue(weight);
                databaseReference.child(personalID).child("height").setValue(height);
                databaseReference.child(personalID).child("age").setValue(age);
                databaseReference.child(personalID).child("levelOfExperience").setValue(levelOfExperience);
                databaseReference.child(personalID).child("purpose").setValue(goalForUsingTheApplication);
                //Go through all the days that the user wishes to exercise and store them into the database, using the user's ID
                for (int i = 0; i < daysToExercise.size(); i++) {
                    databaseReference.child(personalID).child("days").child("day " + i).setValue(daysToExercise.get(i));
                }
                //Try storing the user's personal ID into the device's storage using the WriteData method
                try {
                    WriteData(view, personalID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Creating a fragment transaction
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                //Creating a new StepTwo fragment
                Fragment fragment = new StepTwo();
                //Replace the current fragment (StepOne) by the fragment StepTwo
                ft.replace(R.id.container, fragment, "Step Two").commit();
            }
        });
        return view;
    }

    /**
     * This method checks all the check boxes representing the seven days of the week and adds the selected days to an array list
     *
     * @return The method returns the array list containing the days of the week the user wishes to exercise
     */
    private ArrayList<String> CheckDays() {
        //Array list to store the days of the week the user wishes to exercise
        ArrayList<String> daysToExercise = new ArrayList<>();
        //If the monday check box is selected add Monday to the array list storing the days of the week the user wishes to exercise
        if (monday.isChecked()) {
            daysToExercise.add("Monday");
        }
        //If the tuesday check box is selected add Tuesday to the array list storing the days of the week the user wishes to exercise
        if (tuesday.isChecked()) {
            daysToExercise.add("Tuesday");
        }
        //If the wednesday check box is selected add Wednesday to the array list storing the days of the week the user wishes to exercise
        if (wednesday.isChecked()) {
            daysToExercise.add("Wednesday");
        }
        //If the thursday check box is selected add Thursday to the array list storing the days of the week the user wishes to exercise
        if (thursday.isChecked()) {
            daysToExercise.add("Thursday");
        }
        //If the friday check box is selected add Friday to the array list storing the days of the week the user wishes to exercise
        if (friday.isChecked()) {
            daysToExercise.add("Friday");
        }
        //If the saturday check box is selected add Saturday to the array list storing the days of the week the user wishes to exercise
        if (saturday.isChecked()) {
            daysToExercise.add("Saturday");
        }
        //If the sunday check box is selected add Sunday to the array list storing the days of the week the user wishes to exercise
        if (sunday.isChecked()) {
            daysToExercise.add("Sunday");
        }
        return daysToExercise;
    }

    /**
     * This method is used to store data (the user's personal ID) to a text file in the device
     *
     * @param view       The fragment's view
     * @param personalID The String containing the user's personal ID
     * @throws IOException The method throws an exception if the file writing did not work
     */
    private void WriteData(View view, String personalID) throws IOException {
        //Try storing the user's personal ID into a file inside the device
        try {
            //Creating file output stream, using user_data.txt as the file to save the user's personal ID
            FileOutputStream fileOut = view.getContext().openFileOutput("user_data.txt", view.getContext().MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
            outputWriter.write("Personal ID;");
            //Saving the user's personal ID to the file
            outputWriter.write(personalID);
            //Closing the file output stream
            outputWriter.close();
        }
        //Catch exception if the file writing did not work
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}