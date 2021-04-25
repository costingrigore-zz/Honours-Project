package com.costingrigore.dumbbellapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * This activity is responsible for containing all the pages available in the application apart from the workout activity
 * It is used as a placeholder for showing the StepOne, StepTwo, TrainerFragment, ExercisesFragment, GoalsFragment, ProfileFragment
 * and ProgressFragment, as well as the navigation bar of the application
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Activity log tag
     */
    private static final String LOG_TAG = "MainActivity";
    /**
     * Bottom navigation view used to display the bottom navigation bar
     */
    public BottomNavigationView bottomNavigationView;
    /**
     * Boolean used to check if the user has been registered in the application
     */
    boolean registered = false;

    /**
     * Called when the activity gets created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting the view of the activity
        setContentView(R.layout.activity_main);
        //Find the bottom navigation bar in the application
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavmethod);
        //Getting the menu used for the bottomNavigationView
        bottomNavigationView.getMenu().findItem(R.id.trainer).setChecked(true);
        //Calling the UserRegistrationCheck method to check if the user has previously registered in the application, by completing the user data set up page that appears whenever the user enters the application for the first time
        UserRegistrationCheck(this);
        if (!registered) {
            bottomNavigationView.setVisibility(View.INVISIBLE);
            // Set default fragment when application loads for the first time to be StepOne
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new StepOne()).commit();
        } else {
            // Set default fragment when application loads to be TrainerFragment
            bottomNavigationView.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new TrainerFragment()).commit();
        }

    }

    //Setting up the bottom navigation bar as well as the items that conform the menu
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavmethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    //Switch case used to switch between the different pages available in the application
                    switch (item.getItemId()) {
                        case R.id.exercises:
                            fragment = new ExercisesFragment();
                            break;
                        case R.id.progress:
                            fragment = new ProgressFragment();
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
                    //Committing the transaction to change the fragment that is being displayed in the main activity of the application
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    return true;
                }
            };

    /**
     * This method checks if the user has previously registered in the application, by checking if there is a user_data.txt file created containing the user ID of the current user
     *
     * @param view The view of the activity
     */
    public void UserRegistrationCheck(MainActivity view) {
        //Try reading the contents of the file containing the user's personal ID
        try {
            //Creating a file input stream, using user_data.txt as the input file
            FileInputStream fileIn = view.openFileInput("user_data.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            //Setting a character array to store the first 100 characters of the input stream
            char[] inputBuffer = new char[100];
            //String used to store the user's personal ID
            String s = "";
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
            s = strArray[1];
            //If the string is not empty, it means that the user has previously registered in the application
            if (!s.equals("")) {
                registered = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //If the string was found to be empty, it means that the user has not previously been registered by the application
            registered = false;
        }
    }
}

