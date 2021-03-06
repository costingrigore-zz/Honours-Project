package com.costingrigore.dumbbellapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity" ;
    public BottomNavigationView bottomNavigationView;
    boolean registered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bottomNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavmethod);
        bottomNavigationView.getMenu().findItem(R.id.trainer).setChecked(true);
        ReadData(this);
        if(!registered){
            bottomNavigationView.setVisibility(View.INVISIBLE);
            // Set default fragment when application loads
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new StepOne()).commit();
        }
        else{
            bottomNavigationView.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new TrainerFragment()).commit();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavmethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId())
                    {
                        case R.id.exercises:
                            fragment = new ExercisesFragment();
                        break;
                        case R.id.insights:
                            fragment = new InsightsFragment();
                            break;
                        case R.id.trainer:
                            fragment = new TrainerFragment();
                            break;
                        case R.id.goals:
                            fragment = new GoalsFragment();
                            break;
                        case R.id.profile:
                            fragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    return true;
                }
            };
    public void ReadData(MainActivity view) {
        try {
            FileInputStream fileIn = view.openFileInput("user_data.txt");
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
            if (!s.equals("")) {
                System.out.println("profile " + s);
                registered = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            registered = false;
        }
    }
}

