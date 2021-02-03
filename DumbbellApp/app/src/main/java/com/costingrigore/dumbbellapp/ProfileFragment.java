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
        FirebaseDatabase database;
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
}