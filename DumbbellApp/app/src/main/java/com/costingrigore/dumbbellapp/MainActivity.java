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

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity" ;
    public BottomNavigationView bottomNavigationView;
    TextView textView;
    Button btn;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView2);
        //btn = findViewById(R.id.btn);
        /**btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/
        bottomNavigationView=findViewById(R.id.bottomNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavmethod);
        bottomNavigationView.getMenu().findItem(R.id.trainer).setChecked(true);
        bottomNavigationView.setVisibility(View.INVISIBLE);
        // Set default fragment when application loads
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new StepOne()).commit();


        // Database stuff
        database = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = database.getReference("exercises");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("1").child("name").getValue(String.class);
                textView.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

}

