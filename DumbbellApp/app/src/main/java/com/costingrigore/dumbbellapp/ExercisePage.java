package com.costingrigore.dumbbellapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ExercisePage extends AppCompatActivity {
    int[] FlagId = {R.drawable.india, R.drawable.china, R.drawable.australia, R.drawable.portugal, R.drawable.us, R.drawable.nz};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_activity);
        ArrayList exerciseList = getListData();
        final ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(new CustomAdapter(this, exerciseList));
    }
    private ArrayList getListData() {
        ArrayList<Exercise> results = new ArrayList<>();
        Exercise exercise1 = new Exercise();
        exercise1.setIcon(R.drawable.india);
        exercise1.setName("Suresh Dasari");
        exercise1.setDifficulty("Team Leader");
        exercise1.setType("Hyderabad");
        results.add(exercise1);
        Exercise exercise2 = new Exercise();
        exercise2.setIcon(R.drawable.india);
        exercise2.setName("Rohini Alavala");
        exercise2.setDifficulty("Agricultural Officer");
        exercise2.setType("Guntur");
        results.add(exercise2);
        Exercise exercise3 = new Exercise();
        exercise3.setIcon(R.drawable.india);
        exercise3.setName("Trishika Dasari");
        exercise3.setDifficulty("Charted Accountant");
        exercise3.setType("Guntur");
        results.add(exercise3);
        return results;
    }
}