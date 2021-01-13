package com.costingrigore.dumbbellapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity" ;

    TextView textView;
    Button btn;
    FirebaseDatabase database;

    String[] Countries = {"India", "China", "Australia", "Portugal", "America", "New Zealand"};
    int[] FlagId = {R.drawable.india, R.drawable.china, R.drawable.australia, R.drawable.portugal, R.drawable.us, R.drawable.nz};
    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView2);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openExercisesPage();
            }
        });
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

    public void openExercisesPage()
    {
        Intent intent = new Intent(this, ExercisePage.class);
        startActivity(intent);
    }
}

