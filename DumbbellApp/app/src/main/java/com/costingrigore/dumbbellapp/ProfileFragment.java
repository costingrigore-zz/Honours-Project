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

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    TextView heightTextV;
    TextView weightTextV;
    TextView ageTextV;
    TextView loeTextV;
    TextView goalTextV;
    TextView daysTextV;
    Button editProfile;
    FirebaseDatabase database;

    // Pop-up window
    private String levelOfExperience;
    private String purposeForUseOfApplication;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText weight_popup, height_popup, age_popup;
    private Button submit_button, close_button;
    CheckBox beginner;
    CheckBox intermediate;
    CheckBox advanced;
    CheckBox professional;
    CheckBox gain_strength;
    CheckBox lose_weight;
    CheckBox be_fit;
    CheckBox monday;
    CheckBox tuesday;
    CheckBox wednesday;
    CheckBox thursday;
    CheckBox friday;
    CheckBox saturday;
    CheckBox sunday;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        database = FirebaseDatabase.getInstance();
        String ID = GetPersonalID(view);
        DatabaseReference databaseReference = database.getReference("users").child(ID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String age = snapshot.child("age").getValue(String.class);
                ArrayList<String> days = new ArrayList<>();
                String weight = snapshot.child("weight").getValue(String.class);
                String height = snapshot.child("height").getValue(String.class);
                String loeString = snapshot.child("levelOfExperience").getValue(String.class);
                String goal = snapshot.child("purpose").getValue(String.class);
                for ( DataSnapshot snapshot_day : snapshot.child("days").getChildren()) {
                    String day = snapshot_day.getValue(String.class);
                    days.add(day);
                }
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
                String daysArray[] = days.toArray(new String[days.size()]);
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < daysArray.length; i++) {
                    if(i == daysArray.length - 2) {
                        String capitalisedString = daysArray[i].substring(0, 1).toUpperCase() + daysArray[i].substring(1);
                        sb.append(capitalisedString + " and ");
                    }
                    else if(i == daysArray.length - 1){
                        String capitalisedString = daysArray[i].substring(0, 1).toUpperCase() + daysArray[i].substring(1);
                        sb.append(capitalisedString + ".");
                    }
                    else{
                        String capitalisedString = daysArray[i].substring(0, 1).toUpperCase() + daysArray[i].substring(1);
                        sb.append(capitalisedString + " ");
                    }
                }
                String str = sb.toString();
                daysTextV.setText(str);
                // Edit button behaviour
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

    public void ChangePersonalDetails(View view, String weight, String height, String age, String loeString, String goal, String[] daysArray){
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View editPersonalData = getLayoutInflater().inflate(R.layout.profile_popup, null);
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
        weight_popup.setText(weight);
        height_popup.setText(height);
        age_popup.setText(age);
        if(loeString.equals("Beginner")){
            beginner.setChecked(true);
            levelOfExperience = "Beginner";
        }
        else if(loeString.equals("Intermediate")){
            intermediate.setChecked(true);
            levelOfExperience = "Intermediate";
        }
        else if(loeString.equals("Advanced")){
            advanced.setChecked(true);
            levelOfExperience = "Advanced";
        }
        else if(loeString.equals("Professional")){
            professional.setChecked(true);
            levelOfExperience = "Advanced";
        }
        if(goal.equals("Gain strength")){
            gain_strength.setChecked(true);
            purposeForUseOfApplication = "Gain strength";
        }
        else if(goal.equals("Lose weight")){
            lose_weight.setChecked(true);
            purposeForUseOfApplication = "Lose weight";
        }
        else if(goal.equals("Be fit")){
            be_fit.setChecked(true);
            purposeForUseOfApplication = "Be fit";
        }
        for(int i = 0; i < daysArray.length; i++)
        {
            if(daysArray[i].equals("Monday"))
            {
                monday.setChecked(true);
            }
            else if(daysArray[i].equals("Tuesday"))
            {
                tuesday.setChecked(true);
            }
            else if(daysArray[i].equals("Wednesday"))
            {
                wednesday.setChecked(true);
            }
            else if(daysArray[i].equals("Thursday"))
            {
                thursday.setChecked(true);
            }
            else if(daysArray[i].equals("Friday"))
            {
                friday.setChecked(true);
            }
            else if(daysArray[i].equals("Saturday"))
            {
                saturday.setChecked(true);
            }
            else if(daysArray[i].equals("Sunday"))
            {
                sunday.setChecked(true);
            }
        }
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
        gain_strength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gain_strength.isChecked()) {
                    gain_strength.setChecked(true);
                    lose_weight.setChecked(false);
                    be_fit.setChecked(false);
                    purposeForUseOfApplication = "Gain strength";
                }
            }
        });
        lose_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lose_weight.isChecked()) {
                    gain_strength.setChecked(false);
                    lose_weight.setChecked(true);
                    be_fit.setChecked(false);
                    purposeForUseOfApplication = "Lose weight";
                }
            }
        });
        be_fit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (be_fit.isChecked()) {
                    gain_strength.setChecked(false);
                    lose_weight.setChecked(false);
                    be_fit.setChecked(true);
                    purposeForUseOfApplication = "Be fit";
                }
            }
        });
        dialogBuilder.setView(editPersonalData);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setLayout(dialog.getWindow().getAttributes().width,2000);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String personalID = GetPersonalID(view);
                ArrayList<String> daysToExercise = CheckDays();
                DatabaseReference databaseReference = database.getReference("users");
                databaseReference.child(personalID).child("weight").setValue(weight_popup.getText().toString());
                databaseReference.child(personalID).child("height").setValue(height_popup.getText().toString());
                databaseReference.child(personalID).child("age").setValue(age_popup.getText().toString());
                databaseReference.child(personalID).child("levelOfExperience").setValue(levelOfExperience);
                databaseReference.child(personalID).child("purpose").setValue(purposeForUseOfApplication);
                databaseReference.child(personalID).child("days").removeValue();
                for(int i = 0; i < daysToExercise.size(); i++)
                {
                    databaseReference.child(personalID).child("days").child("day " + i).setValue(daysToExercise.get(i));
                }
                dialog.dismiss();
            }
        });
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // define close button
                dialog.dismiss();
            }
        });
    }
    private ArrayList<String> CheckDays()
    {
        ArrayList<String> daysToExercise = new ArrayList<>();
        if(monday.isChecked())
        {
            daysToExercise.add("Monday");
        }
        if(tuesday.isChecked())
        {
            daysToExercise.add("Tuesday");
        }
        if(wednesday.isChecked())
        {
            daysToExercise.add("Wednesday");
        }
        if(thursday.isChecked())
        {
            daysToExercise.add("Thursday");
        }
        if(friday.isChecked())
        {
            daysToExercise.add("Friday");
        }
        if(saturday.isChecked())
        {
            daysToExercise.add("Saturday");
        }
        if(sunday.isChecked())
        {
            daysToExercise.add("Sunday");
        }
        return daysToExercise;
    }
}