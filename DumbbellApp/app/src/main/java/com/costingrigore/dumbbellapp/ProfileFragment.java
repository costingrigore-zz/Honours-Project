package com.costingrigore.dumbbellapp;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * This fragment is used to display the user's personal data to the user, in the form of a profile page
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    /**
     * Initializing the TextViews for the profile fragment
     */
    TextView heightTextV;
    TextView weightTextV;
    TextView ageTextV;
    TextView loeTextV;
    TextView goalTextV;
    TextView daysTextV;
    Button editProfile;
    /**
     * Firebase initialization
     */
    FirebaseDatabase database;
    /**
     * The following fields are used to set up the pop up used to edit the personal details of the user
     */
    private String levelOfExperience;
    private String goalForUsingTheApplication;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog profileEditionDialog;
    private EditText weight_popup, height_popup, age_popup;
    private Button submit_button, close_button;
    CheckBox beginner, intermediate, advanced, professional, gain_strength, lose_weight, be_fit, monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    /**
     * Public constructor of the class
     */
    public ProfileFragment() {
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
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //Getting the firebase's instance
        database = FirebaseDatabase.getInstance();
        //Getting the user's personal ID using the GetPersonalID method
        String ID = GetPersonalID(view);
        //Getting the database's reference, using the user's personal ID
        DatabaseReference databaseReference = database.getReference("users").child(ID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //String used to store the days of the week the user wishes to exercise
                ArrayList<String> days = new ArrayList<>();
                //Getting the user's age from the database
                String age = snapshot.child("age").getValue(String.class);
                //Getting the user's weight from the database
                String weight = snapshot.child("weight").getValue(String.class);
                //Getting the user's height from the database
                String height = snapshot.child("height").getValue(String.class);
                //Getting the user's level of experience from the database
                String loeString = snapshot.child("levelOfExperience").getValue(String.class);
                //Getting the user's goal for using the application from the database
                String goal = snapshot.child("purpose").getValue(String.class);
                //Getting the days the user wishes to exercise from the database
                for (DataSnapshot snapshot_day : snapshot.child("days").getChildren()) {
                    String day = snapshot_day.getValue(String.class);
                    days.add(day);
                }
                /**
                 * Setting the fragment's TextView objects
                 */
                editProfile = (Button) view.findViewById(R.id.edit_profile);
                ageTextV = (TextView) view.findViewById(R.id.ageText);
                weightTextV = (TextView) view.findViewById(R.id.weightText);
                heightTextV = (TextView) view.findViewById(R.id.heightText);
                loeTextV = (TextView) view.findViewById(R.id.level_of_experience_text);
                goalTextV = (TextView) view.findViewById(R.id.goal_text);
                daysTextV = (TextView) view.findViewById(R.id.days_of_the_week_text);
                ageTextV.setText(age);
                weightTextV.setText(weight);
                heightTextV.setText(height);
                loeTextV.setText(loeString);
                goalTextV.setText(goal);
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
                        String capitalisedString = daysArray[i].substring(0, 1).toUpperCase() + daysArray[i].substring(1);
                        sb.append(capitalisedString + " and ");
                    } else if (i == daysArray.length - 1) {
                        String capitalisedString = daysArray[i].substring(0, 1).toUpperCase() + daysArray[i].substring(1);
                        sb.append(capitalisedString + ".");
                    } else {
                        String capitalisedString = daysArray[i].substring(0, 1).toUpperCase() + daysArray[i].substring(1);
                        sb.append(capitalisedString + " ");
                    }
                }
                //Storing the string buffer into a string
                String str = sb.toString();
                daysTextV.setText(str);
                /**
                 * Setting the edit profile button's onClick listener
                 * When the button is pressed a new pop up will open, allowing the user to change its personal data, using the ChangePersonalDetails method
                 */
                editProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChangePersonalDetails(view, weight, height, age, loeString, goal, daysArray);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }


    public void ChangePersonalDetails(View view, String weight, String height, String age, String loeString, String goal, String[] daysArray) {
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View editPersonalData = getLayoutInflater().inflate(R.layout.profile_popup, null);
        /**
         * Setting the fragment's EditText objects, CheckBoxes and button
         */
        weight_popup = (EditText) editPersonalData.findViewById(R.id.weight_popup);
        height_popup = (EditText) editPersonalData.findViewById(R.id.height_popup);
        age_popup = (EditText) editPersonalData.findViewById(R.id.age_popup);
        submit_button = (Button) editPersonalData.findViewById(R.id.submit_button);
        close_button = (Button) editPersonalData.findViewById(R.id.close_button);
        beginner = (CheckBox) editPersonalData.findViewById(R.id.beginner);
        intermediate = (CheckBox) editPersonalData.findViewById(R.id.intermediate);
        advanced = (CheckBox) editPersonalData.findViewById(R.id.advanced);
        professional = (CheckBox) editPersonalData.findViewById(R.id.professional);
        gain_strength = (CheckBox) editPersonalData.findViewById(R.id.gain_strength);
        lose_weight = (CheckBox) editPersonalData.findViewById(R.id.lose_weight);
        be_fit = (CheckBox) editPersonalData.findViewById(R.id.be_fit);
        monday = (CheckBox) editPersonalData.findViewById(R.id.monday);
        tuesday = (CheckBox) editPersonalData.findViewById(R.id.tuesday);
        wednesday = (CheckBox) editPersonalData.findViewById(R.id.wednesday);
        thursday = (CheckBox) editPersonalData.findViewById(R.id.thursday);
        friday = (CheckBox) editPersonalData.findViewById(R.id.friday);
        saturday = (CheckBox) editPersonalData.findViewById(R.id.saturday);
        sunday = (CheckBox) editPersonalData.findViewById(R.id.sunday);
        /**
         * The following lines of code use the weight, the height and the age of the user
         * to set the EditText values that allow the user to change its personal details
         * This gives the user a better view of their current data on the database
         * and it also allows a better user experience for the user
         */
        weight_popup.setText(weight);
        height_popup.setText(height);
        age_popup.setText(age);
        /**
         * The following lines of code use the level of experience of the user,
         * the goal for using the application and the days of the week that the user wishes
         * to exercise to set the check boxes values that allow the user to change its personal details
         * This gives the user a better view of their current data on the database
         * and it also allows a better user experience for the user
         */
        if (loeString.equals("Beginner")) {
            beginner.setChecked(true);
            levelOfExperience = "Beginner";
        } else if (loeString.equals("Intermediate")) {
            intermediate.setChecked(true);
            levelOfExperience = "Intermediate";
        } else if (loeString.equals("Advanced")) {
            advanced.setChecked(true);
            levelOfExperience = "Advanced";
        } else if (loeString.equals("Professional")) {
            professional.setChecked(true);
            levelOfExperience = "Advanced";
        }
        if (goal.equals("Gain strength")) {
            gain_strength.setChecked(true);
            goalForUsingTheApplication = "Gain strength";
        } else if (goal.equals("Lose weight")) {
            lose_weight.setChecked(true);
            goalForUsingTheApplication = "Lose weight";
        } else if (goal.equals("Be fit")) {
            be_fit.setChecked(true);
            goalForUsingTheApplication = "Be fit";
        }
        //Check all the days of the week that the user picked to exercise and set the check boxes state to the days currently picked by the user
        for (int i = 0; i < daysArray.length; i++) {
            if (daysArray[i].equals("Monday")) {
                monday.setChecked(true);
            } else if (daysArray[i].equals("Tuesday")) {
                tuesday.setChecked(true);
            } else if (daysArray[i].equals("Wednesday")) {
                wednesday.setChecked(true);
            } else if (daysArray[i].equals("Thursday")) {
                thursday.setChecked(true);
            } else if (daysArray[i].equals("Friday")) {
                friday.setChecked(true);
            } else if (daysArray[i].equals("Saturday")) {
                saturday.setChecked(true);
            } else if (daysArray[i].equals("Sunday")) {
                sunday.setChecked(true);
            }
        }
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
        //Setting the view of the popup window used to change the personal details of the user
        dialogBuilder.setView(editPersonalData);
        //Create the popup dialog
        profileEditionDialog = dialogBuilder.create();
        profileEditionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        profileEditionDialog.show();
        profileEditionDialog.getWindow().setLayout(profileEditionDialog.getWindow().getAttributes().width, 2000);
        /**
         * The submit button, saves the changes made to the personal details of the user on the popup window, to the database
         */
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initializing the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //Getting the user's personal ID using the GetPersonalID method
                String personalID = GetPersonalID(view);
                //Get the days the user wishes to exercise using the CheckDays method
                ArrayList<String> daysToExercise = CheckDays();
                //Setting database reference
                DatabaseReference databaseReference = database.getReference("users");
                //Saving user's personal data to database, using the user's ID
                databaseReference.child(personalID).child("weight").setValue(weight_popup.getText().toString());
                databaseReference.child(personalID).child("height").setValue(height_popup.getText().toString());
                databaseReference.child(personalID).child("age").setValue(age_popup.getText().toString());
                databaseReference.child(personalID).child("levelOfExperience").setValue(levelOfExperience);
                databaseReference.child(personalID).child("purpose").setValue(goalForUsingTheApplication);
                databaseReference.child(personalID).child("days").removeValue();
                //Go through all the days that the user wishes to exercise and store them into the database, using the user's ID
                for (int i = 0; i < daysToExercise.size(); i++) {
                    databaseReference.child(personalID).child("days").child("day " + i).setValue(daysToExercise.get(i));
                }
                //Close the profile edition dialog/popup
                profileEditionDialog.dismiss();
            }
        });
        /**
         * The close button closes the popup window used to change the personal details of the user without saving the changes to the database
         */
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Close the profile edition dialog/popup
                profileEditionDialog.dismiss();
            }
        });
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