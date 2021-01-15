package com.costingrigore.dumbbellapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity" ;
    private BottomNavigationView bottomNavigationView;
    TextView textView;
    Button btn;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView2);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        bottomNavigationView=findViewById(R.id.bottomNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavmethod);
        bottomNavigationView.getMenu().findItem(R.id.trainer).setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new TrainerFragment()).commit();


        // Database stuff
        database = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("exercises");
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

        DatabaseReference myRef = database.getReference("exercises");
        myRef.child("1").child("name").setValue("pullups");
        myRef.child("1").child("difficulty").setValue("easy");
        myRef.child("1").child("type").setValue("strength");
        myRef.child("2").setValue("pain");
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

