package com.costingrigore.dumbbellapp;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class WorkoutRoutineCreationAlgorithm {

    private String levelOfExperience = "Intermediate";
    private String goal = "Be fit";
    private View view;
    FirebaseDatabase database;
    private int beginnerID = 0;
    private int intermediateID = 1;
    private int advancedID = 2;
    private int professionalID = 3;

    private int beFitID = 0;
    private int loseWeightID = 1;
    private int gainStrengthID = 2;
    private int mColumnCount = 1;


    public ArrayList<Exercise> cardioExercises = new ArrayList<>();
    public ArrayList<Exercise> weightTrainingExercises= new ArrayList<>();
    public ArrayList<Exercise> coreExercises= new ArrayList<>();
    public ArrayList<Exercise> stretchingExercises= new ArrayList<>();

    int cardioAmountOfExercises;
    int weightTrainingAmountOfExercises;
    int coreAmountOfExercises;
    int currentAmountOfExercises;

    public ArrayList<Exercise> getCardioExercises() {
        return cardioExercises;
    }
    public ArrayList<Exercise> getWeightTrainingExercises() {
        return weightTrainingExercises;
    }
    public ArrayList<Exercise> getCoreExercises() {
        return coreExercises;
    }
    public ArrayList<Exercise> getStretchingExercises() {
        return stretchingExercises;
    }


    private int[][] cardioTimeValues =
            {
                    {15, 20, 25, 30},
                    {20, 25, 30, 30},
                    {15, 15, 20, 25}
            };
    private int[][] weightTrainingTimeValues =
            {
                    {10, 15, 15, 20},
                    {10, 10, 15, 20},
                    {20, 20, 25, 30}
            };
    private int[][] coreTimeValues =
            {
                    {10, 10, 15, 15},
                    {5, 5, 10, 10},
                    {10, 10, 15, 20}
            };

     public WorkoutRoutineCreationAlgorithm(String levelOfExperience, String goal, View view) {
         this.levelOfExperience = levelOfExperience;
         this.goal = goal;
         this.view = view;
     }

    public void ProcessUserPersonalData(){
        int levelOfExperienceID = 0;
        int goalID = 0;
        int cardioTime = 0;
        int weightTrainingTime = 0;
        int coreTime = 0;
        if(levelOfExperience.equals("Beginner")){
            levelOfExperienceID = beginnerID;
        }
        else if(levelOfExperience.equals("Intermediate")){
            levelOfExperienceID = intermediateID;
        }
        else if(levelOfExperience.equals("Advanced")){
            levelOfExperienceID = advancedID;
        }
        else if(levelOfExperience.equals("Professional")){
            levelOfExperienceID = professionalID;
        }
        if(goal.equals("Gain strength")){
            goalID = gainStrengthID;
        }
        else if(goal.equals("Lose weight")){
            goalID = loseWeightID;
        }
        else if(goal.equals("Be fit")){
            goalID = beFitID;
        }
        cardioTime = cardioTimeValues[goalID][levelOfExperienceID];
        weightTrainingTime = weightTrainingTimeValues[goalID][levelOfExperienceID];
        coreTime = coreTimeValues[goalID][levelOfExperienceID];
        System.out.println("User data processed: " + cardioTime +" , " + weightTrainingTime+ " , " + coreTime);
        cardioAmountOfExercises = cardioTime - (cardioTime / 5);
        weightTrainingAmountOfExercises = weightTrainingTime - (weightTrainingTime / 5);
        coreAmountOfExercises = coreTime - (coreTime / 5);
    }



    public void GetPersonalisedExercises(String exercise_type, String body_part){
        Context context = view.getContext();
        RecyclerView recyclerView = null;
        MyExerciseRecyclerViewAdapter adapter;
        database = FirebaseDatabase.getInstance();
        ArrayList<Exercise> exercises = new ArrayList<>();
        DatabaseReference databaseReference = null;
        int currentAmountOfExercises = 0;
        System.out.println("curr: " +cardioAmountOfExercises + ", "+ weightTrainingAmountOfExercises+ ", "+ coreAmountOfExercises);
        if(exercise_type.equals("cardio"))
        {
            databaseReference = database.getReference("exercises").child("cardio").child(body_part);
            currentAmountOfExercises = cardioAmountOfExercises;
        }
        else if(exercise_type.equals("strength") && !body_part.equals("core"))
        {
            databaseReference = database.getReference("exercises").child("strength").child(body_part);
            currentAmountOfExercises = weightTrainingAmountOfExercises;
        }
        else if(body_part.equals("core") && exercise_type.equals("strength"))
        {
            databaseReference = database.getReference("exercises").child("strength").child(body_part);
            currentAmountOfExercises = coreAmountOfExercises;
        }
        int finalCurrentAmountOfExercises = currentAmountOfExercises;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String exerciseDifficultyState = "easy";
                ArrayList<Integer> easyExercisesIDs = new ArrayList<Integer>();
                easyExercisesIDs.add(1);
                easyExercisesIDs.add(2);
                easyExercisesIDs.add(3);
                easyExercisesIDs.add(4);
                easyExercisesIDs.add(5);
                ArrayList<Integer> mediumExercisesIDs = new ArrayList<Integer>();
                mediumExercisesIDs.add(1);
                mediumExercisesIDs.add(2);
                mediumExercisesIDs.add(3);
                mediumExercisesIDs.add(4);
                mediumExercisesIDs.add(5);
                ArrayList<Integer> difficultExercisesIDs = new ArrayList<Integer>();
                difficultExercisesIDs.add(1);
                difficultExercisesIDs.add(2);
                difficultExercisesIDs.add(3);
                difficultExercisesIDs.add(4);
                difficultExercisesIDs.add(5);
                for(int i = 0; i< finalCurrentAmountOfExercises; i++)
                {
                    if(easyExercisesIDs.isEmpty())
                    {
                        easyExercisesIDs.add(1);
                        easyExercisesIDs.add(2);
                        easyExercisesIDs.add(3);
                        easyExercisesIDs.add(4);
                        easyExercisesIDs.add(5);
                    }
                    else if(mediumExercisesIDs.isEmpty())
                    {
                        mediumExercisesIDs.add(1);
                        mediumExercisesIDs.add(2);
                        mediumExercisesIDs.add(3);
                        mediumExercisesIDs.add(4);
                        mediumExercisesIDs.add(5);
                    }
                    else if(difficultExercisesIDs.isEmpty())
                    {
                        difficultExercisesIDs.add(1);
                        difficultExercisesIDs.add(2);
                        difficultExercisesIDs.add(3);
                        difficultExercisesIDs.add(4);
                        difficultExercisesIDs.add(5);
                    }
                    if(exerciseDifficultyState.equals("easy"))
                    {
                        Random random1 = new Random();
                        int randomInteger = random1.nextInt(easyExercisesIDs.size());
                        String indexValue = easyExercisesIDs.get(randomInteger).toString();
                        String name = dataSnapshot.child("easy").child(indexValue).child("name").getValue(String.class);
                        //String name = dataSnapshot.child("easy").child("1").child("name").getValue(String.class);
                        System.out.println("eazy exercise: " + name);
                        easyExercisesIDs.remove(randomInteger);
                        exerciseDifficultyState = "medium";
                        Exercise exercise = SetExerciseFields(name, exerciseDifficultyState,exercise_type,body_part);
                        if(exercise_type.equals("cardio"))
                        {
                            cardioExercises.add(exercise);
                        }
                        else if(exercise_type.equals("strength") && !body_part.equals("core"))
                        {
                            weightTrainingExercises.add(exercise);
                        }
                        else if(body_part.equals("core") && exercise_type.equals("strength"))
                        {
                            coreExercises.add(exercise);
                        }
                    }
                    else if(exerciseDifficultyState.equals("medium"))
                    {
                        Random random2 = new Random();
                        int randomInteger = random2.nextInt(mediumExercisesIDs.size());
                        String indexValue = mediumExercisesIDs.get(randomInteger).toString();
                        String name = dataSnapshot.child("medium").child(indexValue).child("name").getValue(String.class);
                        //String name = dataSnapshot.child("easy").child("2").child("name").getValue(String.class);
                        mediumExercisesIDs.remove(randomInteger);
                        System.out.println("medium exercise: " + name);
                        exerciseDifficultyState = "difficult";
                        Exercise exercise = SetExerciseFields(name, exerciseDifficultyState,exercise_type,body_part);
                        if(exercise_type.equals("cardio"))
                        {
                            cardioExercises.add(exercise);
                        }
                        else if(exercise_type.equals("strength") && !body_part.equals("core"))
                        {
                            weightTrainingExercises.add(exercise);
                        }
                        else if(body_part.equals("core") && exercise_type.equals("strength"))
                        {
                            coreExercises.add(exercise);
                        }
                    }
                    else if(exerciseDifficultyState.equals("difficult"))
                    {
                        Random random3 = new Random();
                        //System.out.println("Index value    " + difficultExercisesIDs.size());
                        int randomInteger = random3.nextInt(difficultExercisesIDs.size());
                        //System.out.println("Index value    " + randomInteger);
                        String indexValue = difficultExercisesIDs.get(randomInteger).toString();
                        //System.out.println("Index value" + indexValue);
                        String name = dataSnapshot.child("difficult").child(indexValue).child("name").getValue(String.class);
                        //String name = dataSnapshot.child("easy").child("3").child("name").getValue(String.class);
                        difficultExercisesIDs.remove(randomInteger);
                        //System.out.println("diffy exercise: " + name);
                        exerciseDifficultyState = "easy";
                        Exercise exercise = SetExerciseFields(name, exerciseDifficultyState,exercise_type,body_part);
                        if(exercise_type.equals("cardio"))
                        {
                            cardioExercises.add(exercise);
                        }
                        else if(exercise_type.equals("strength") && !body_part.equals("core"))
                        {
                            weightTrainingExercises.add(exercise);
                        }
                        else if(body_part.equals("core") && exercise_type.equals("strength"))
                        {
                            coreExercises.add(exercise);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(exercise_type.equals("cardio")){
            recyclerView = (RecyclerView) view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyExerciseRecyclerViewAdapter(cardioExercises);
            LinearLayoutManager horizontalLayoutManagaer
                    = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManagaer);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        if(exercise_type.equals("strength") && !body_part.equals("core")){
            recyclerView = (RecyclerView) view.findViewById(R.id.list2);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyExerciseRecyclerViewAdapter(weightTrainingExercises);
            LinearLayoutManager horizontalLayoutManagaer
                    = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManagaer);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        if(body_part.equals("core") && exercise_type.equals("strength")){
            recyclerView = (RecyclerView) view.findViewById(R.id.list3);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyExerciseRecyclerViewAdapter(coreExercises);
            LinearLayoutManager horizontalLayoutManagaer
                    = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManagaer);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public Exercise SetExerciseFields(String name, String difficulty, String type, String body_part){
        Exercise exercise = new Exercise();
        String imageFileName = name; //  this is image file name
        String PACKAGE_NAME = view.getContext().getPackageName();
        int imgId = view.getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + imageFileName, null, null);
        exercise.setIcon(imgId);
        exercise.setName(replace(name));
        exercise.setDifficulty(replace(difficulty));
        exercise.setType(replace(type));
        exercise.setBody_part(replace(body_part));
        return exercise;
    }

    public static String replace(String input) {
        String spacesReplaced = replace('_', ' ', input);
        String finalName = spacesReplaced.substring(0, 1).toUpperCase() + spacesReplaced.substring(1);
        return finalName;
    }

    public static String replace(char oldDelim, char newDelim, String input) {
        boolean wasOldDelim = false;
        int o = 0;
        char[] buf = input.toCharArray();
        for (int i = 0; i < buf.length; i++) {
            assert(o <= i);
            if (buf[i] == oldDelim) {
                if (wasOldDelim) { continue; }
                wasOldDelim = true;
                buf[o++] = newDelim;
            } else {
                wasOldDelim = false;
                buf[o++] = buf[i];
            }
        }
        return new String(buf, 0, o);
    }

}
