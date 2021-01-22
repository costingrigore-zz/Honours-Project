package com.costingrigore.dumbbellapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepOne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepOne extends Fragment {

    View view;
    TextView weightText;
    EditText editTextWeight;
    EditText editTextHeight;
    EditText editTextAge;
    Button nextButton;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_step_one, container, false);
        weightText = (TextView) view.findViewById(R.id.weightText);
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
        beginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beginner.isChecked()) {
                    beginner.setChecked(true);
                    intermediate.setChecked(false);
                    advanced.setChecked(false);
                    professional.setChecked(false);
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
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weight = editTextWeight.getText().toString();
                String height = editTextHeight.getText().toString();
                String age = editTextAge.getText().toString();
                try {
                    WriteData(view, weight, height, age);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = new StepTwo();
                ft.replace(R.id.container, fragment, "Step Two").commit();
            }
        });
        return view;
    }

    private void WriteData(View view, String weight, String height, String age) throws IOException {
        try {
            FileOutputStream fileOut= view.getContext().openFileOutput("user_data.txt", view.getContext().MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileOut);
            outputWriter.write("Personal Data;");
            outputWriter.write(weight + ";" + height +";" + age + ";");
            outputWriter.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ReadData(View view) {
        try {
            FileInputStream fileIn = view.getContext().openFileInput("user_data.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[100];
            String s = "";
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            InputRead.close();
            weightText.setText(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}