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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

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
                Fragment fragment = new TrainerFragment();
                ft.replace(R.id.container, fragment, "Trainer Fragment").commit();
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    private void WriteData(View view, String weight, String height, String age) throws IOException {
        try {
            FileOutputStream fileout= view.getContext().openFileOutput("user_data.txt", view.getContext().MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
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