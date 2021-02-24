package com.costingrigore.dumbbellapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import static android.view.View.GONE;

public class WorkoutActivity extends AppCompatActivity {

    Button startWorkoutButton;
    Button nextExercise;
    TextView countText;
    public int counter;
    private int mColumnCount = 1;
    private String levelOfExperience = "Intermediate";
    private String goal = "Be fit";
    FirebaseDatabase database;
    private int beginnerID = 0;
    private int intermediateID = 1;
    private int advancedID = 2;
    private int professionalID = 3;

    private int beFitID = 0;
    private int loseWeightID = 1;
    private int gainStrengthID = 2;


    public ArrayList<Exercise> cardioExercises = new ArrayList<>();
    public ArrayList<Exercise> weightTrainingExercises= new ArrayList<>();
    public ArrayList<Exercise> coreExercises= new ArrayList<>();
    public ArrayList<Exercise> stretchingExercises= new ArrayList<>();

    RecyclerView cardioExercisesRecyclerView = null;
    MyTrainerExerciseRecyclerViewAdapter cardioExercisesAdapter;
    RecyclerView weightTrainingExercisesRecyclerView = null;
    MyTrainerExerciseRecyclerViewAdapter weightTrainingExercisesAdapter;
    RecyclerView coreExercisesRecyclerView = null;
    MyTrainerExerciseRecyclerViewAdapter coreExercisesAdapter;
    RecyclerView stretchingExercisesRecyclerView = null;
    MyTrainerExerciseRecyclerViewAdapter stretchingExercisesAdapter;

    int cardioAmountOfExercises;
    int weightTrainingAmountOfExercises;
    int coreAmountOfExercises;
    int stretchingAmountOfExercises;
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
    private int stretchingTimeValue = 8;

    CheckBox cardioSetsAndRepsCB;
    LinearLayout cardioSetsAndRepsLayout;
    NumberPicker cardioSets;
    NumberPicker cardioReps;
    CheckBox cardioTimeCB;
    LinearLayout cardioTimeLayout;
    NumberPicker cardioTime;
    boolean cardioUsesTime = true;
    int maxCardioSets = 10;
    int maxCardioReps = 60;
    int maxCardioTime = 10;

    CheckBox wtSetsAndRepsCB;
    LinearLayout wtSetsAndRepsLayout;
    NumberPicker wtSets;
    NumberPicker wtReps;
    CheckBox wtTimeCB;
    LinearLayout wtTimeLayout;
    NumberPicker wtTime;
    boolean wtUsesTime = true;
    int maxWtSets = 10;
    int maxWtReps = 60;
    int maxWtTime = 10;

    CheckBox coreSetsAndRepsCB;
    LinearLayout coreSetsAndRepsLayout;
    NumberPicker coreSets;
    NumberPicker coreReps;
    CheckBox coreTimeCB;
    LinearLayout coreTimeLayout;
    NumberPicker coreTime;
    boolean coreUsesTime = true;
    int maxCoreSets = 10;
    int maxCoreReps = 60;
    int maxCoreTime = 10;

    int cardioSetsNumber;
    int cardioRepsNumber;
    int cardioTimeNumber;
    int wtSetsNumber;
    int wtRepsNumber;
    int wtTimeNumber;
    int coreSetsNumber;
    int coreRepsNumber;
    int coreTimeNumber;

    LinearLayout initialLayout;
    LinearLayout workoutLayout;
    ImageView exerciseImage;
    TextView exerciseName;
    LinearLayout exerciseSetsAndRepetitionsLayout;
    TextView exerciseSets;
    TextView exerciseReps;
    LinearLayout exerciseTimeLayout;
    TextView exerciseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        /** Workout Routine Creation Algorithm's fields set up
         *
         *
         *
         * **/
        database = FirebaseDatabase.getInstance();
        ProcessUserPersonalData();
        cardioExercisesRecyclerView = (RecyclerView) this.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            cardioExercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            cardioExercisesRecyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        }
        LinearLayoutManager horizontalLayoutManager1
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        cardioExercisesRecyclerView.setLayoutManager(horizontalLayoutManager1);
        cardioExercisesAdapter = new MyTrainerExerciseRecyclerViewAdapter(cardioExercises);
        cardioExercisesRecyclerView.setAdapter(cardioExercisesAdapter);


        weightTrainingExercisesRecyclerView = (RecyclerView) this.findViewById(R.id.list2);
        if (mColumnCount <= 1) {
            weightTrainingExercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            weightTrainingExercisesRecyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        }
        LinearLayoutManager horizontalLayoutManager2
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        weightTrainingExercisesRecyclerView.setLayoutManager(horizontalLayoutManager2);
        weightTrainingExercisesAdapter = new MyTrainerExerciseRecyclerViewAdapter(weightTrainingExercises);
        weightTrainingExercisesRecyclerView.setAdapter(weightTrainingExercisesAdapter);


        coreExercisesRecyclerView = (RecyclerView) this.findViewById(R.id.list3);
        if (mColumnCount <= 1) {
            coreExercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            coreExercisesRecyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        }
        LinearLayoutManager horizontalLayoutManager3
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        coreExercisesRecyclerView.setLayoutManager(horizontalLayoutManager3);
        coreExercisesAdapter = new MyTrainerExerciseRecyclerViewAdapter(coreExercises);
        coreExercisesRecyclerView.setAdapter(coreExercisesAdapter);

        stretchingExercisesRecyclerView = (RecyclerView) this.findViewById(R.id.list4);
        if (mColumnCount <= 1) {
            stretchingExercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            stretchingExercisesRecyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        }
        LinearLayoutManager horizontalLayoutManager4
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        stretchingExercisesRecyclerView.setLayoutManager(horizontalLayoutManager4);
        stretchingExercisesAdapter = new MyTrainerExerciseRecyclerViewAdapter(stretchingExercises);
        stretchingExercisesRecyclerView.setAdapter(stretchingExercisesAdapter);
        GetPersonalisedExercises("cardio","total_body");
        GetPersonalisedExercises("strength","upper_body");
        GetPersonalisedExercises("strength","core");
        GetPersonalisedExercises("flexibility","total_body");

        /** Setting up checkboxes and Number picker fields to retrieve repetitions, sets and time fro the user
         *
         *
         *
         **/
        cardioSetsAndRepsCB = (CheckBox) this.findViewById(R.id.cardioSetsandRepsCB);
        cardioSetsAndRepsLayout = (LinearLayout) this.findViewById(R.id.cardioSetsAndRepsLayout);
        cardioSets = (NumberPicker) this.findViewById(R.id.cardioSets);
        cardioSets.setMinValue(1);
        cardioSets.setMaxValue(maxCardioSets);
        cardioReps = (NumberPicker) this.findViewById(R.id.cardioReps);
        cardioReps.setMinValue(1);
        cardioReps.setMaxValue(maxCardioReps);
        cardioTimeCB = (CheckBox) this.findViewById(R.id.cardioTimeCB);
        cardioTimeLayout = (LinearLayout) this.findViewById(R.id.cardioTimeLayout);
        cardioTime = (NumberPicker) this.findViewById(R.id.cardioTime);
        cardioTime.setMinValue(1);
        cardioTime.setMaxValue(maxCardioTime);

        wtSetsAndRepsCB = (CheckBox) this.findViewById(R.id.wtSetsandRepsCB);
        wtSetsAndRepsLayout = (LinearLayout) this.findViewById(R.id.wtSetsAndRepsLayout);
        wtSets = (NumberPicker) this.findViewById(R.id.wtSets);
        wtSets.setMinValue(1);
        wtSets.setMaxValue(maxWtSets);
        wtReps = (NumberPicker) this.findViewById(R.id.wtReps);
        wtReps.setMinValue(1);
        wtReps.setMaxValue(maxWtReps);
        wtTimeCB = (CheckBox) this.findViewById(R.id.wtTimeCB);
        wtTimeLayout = (LinearLayout) this.findViewById(R.id.wtTimeLayout);
        wtTime = (NumberPicker) this.findViewById(R.id.wtTime);
        wtTime.setMinValue(1);
        wtTime.setMaxValue(maxWtTime);

        coreSetsAndRepsCB = (CheckBox) this.findViewById(R.id.coreSetsandRepsCB);
        coreSetsAndRepsLayout = (LinearLayout) this.findViewById(R.id.coreSetsAndRepsLayout);
        coreSets = (NumberPicker) this.findViewById(R.id.coreSets);
        coreSets.setMinValue(1);
        coreSets.setMaxValue(maxCoreSets);
        coreReps = (NumberPicker) this.findViewById(R.id.coreReps);
        coreReps.setMinValue(1);
        coreReps.setMaxValue(maxCoreReps);
        coreTimeCB = (CheckBox) this.findViewById(R.id.coreTimeCB);
        coreTimeLayout = (LinearLayout) this.findViewById(R.id.coreTimeLayout);
        coreTime = (NumberPicker) this.findViewById(R.id.coreTime);
        coreTime.setMinValue(1);
        coreTime.setMaxValue(maxCoreTime);

        cardioSetsAndRepsCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardioSetsAndRepsCB.isChecked()) {
                    cardioSetsAndRepsCB.setChecked(true);
                    cardioTimeCB.setChecked(false);
                    cardioUsesTime = false;
                    cardioSetsAndRepsLayout.setVisibility(View.VISIBLE);
                    cardioTimeLayout.setVisibility(GONE);
                }
            }
        });
        cardioTimeCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardioTimeCB.isChecked()) {
                    cardioTimeCB.setChecked(true);
                    cardioSetsAndRepsCB.setChecked(false);
                    cardioUsesTime = true;
                    cardioTimeLayout.setVisibility(View.VISIBLE);
                    cardioSetsAndRepsLayout.setVisibility(GONE);
                }
            }
        });

        wtSetsAndRepsCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wtSetsAndRepsCB.isChecked()) {
                    wtSetsAndRepsCB.setChecked(true);
                    wtTimeCB.setChecked(false);
                    wtUsesTime = false;
                    wtSetsAndRepsLayout.setVisibility(View.VISIBLE);
                    wtTimeLayout.setVisibility(GONE);
                }
            }
        });
        wtTimeCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wtTimeCB.isChecked()) {
                    wtTimeCB.setChecked(true);
                    wtSetsAndRepsCB.setChecked(false);
                    wtUsesTime = true;
                    wtTimeLayout.setVisibility(View.VISIBLE);
                    wtSetsAndRepsLayout.setVisibility(GONE);
                }
            }
        });

        coreSetsAndRepsCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coreSetsAndRepsCB.isChecked()) {
                    coreSetsAndRepsCB.setChecked(true);
                    coreTimeCB.setChecked(false);
                    coreUsesTime = false;
                    coreSetsAndRepsLayout.setVisibility(View.VISIBLE);
                    coreTimeLayout.setVisibility(GONE);
                }
            }
        });
        coreTimeCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coreTimeCB.isChecked()) {
                    coreTimeCB.setChecked(true);
                    coreSetsAndRepsCB.setChecked(false);
                    coreUsesTime = true;
                    coreTimeLayout.setVisibility(View.VISIBLE);
                    coreSetsAndRepsLayout.setVisibility(GONE);
                }
            }
        });

        cardioSets.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                cardioSetsNumber = cardioSets.getValue();
            }
        });
        cardioReps.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                cardioRepsNumber = cardioReps.getValue();
            }
        });
        cardioTime.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                cardioTimeNumber = cardioTime.getValue();
            }
        });

        wtSets.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                wtSetsNumber = wtSets.getValue();
            }
        });
        wtReps.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                wtRepsNumber = wtReps.getValue();
            }
        });
        wtTime.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                wtTimeNumber = wtTime.getValue();
            }
        });

        coreSets.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                coreSetsNumber = coreSets.getValue();
            }
        });
        coreReps.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                coreRepsNumber = coreReps.getValue();
            }
        });
        coreTime.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                coreTimeNumber = coreTime.getValue();
            }
        });


        initialLayout = (LinearLayout) this.findViewById(R.id.initial_layout);
        workoutLayout = (LinearLayout) this.findViewById(R.id.workout_layout);
        exerciseImage = (ImageView) this.findViewById(R.id.exerciseID);
        exerciseName = (TextView) this.findViewById(R.id.exercise_name);
        exerciseSetsAndRepetitionsLayout = (LinearLayout) this.findViewById(R.id.exercise_sets_and_reps_layout);
        exerciseSets = (TextView) this.findViewById(R.id.exercise_sets);
        exerciseReps = (TextView) this.findViewById(R.id.exercise_reps);
        exerciseTimeLayout = (LinearLayout) this.findViewById(R.id.exercise_time_layout);
        exerciseTime = (TextView) this.findViewById(R.id.exercise_time);

        startWorkoutButton = (Button) this.findViewById(R.id.startWorkoutButton);
        nextExercise = (Button) this.findViewById(R.id.nextButton);
        String currentComponent = "Cardio";
        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialLayout.setVisibility(GONE);
                workoutLayout.setVisibility(View.VISIBLE);
                ShowExercise(cardioExercises.get(0));
                if(cardioUsesTime)
                {
                    exerciseTimeLayout.setVisibility(View.VISIBLE);
                    exerciseSetsAndRepetitionsLayout.setVisibility(GONE);
                    exerciseTime.setText(String.valueOf(cardioTimeNumber));
                }
                else{
                    exerciseTimeLayout.setVisibility(GONE);
                    exerciseSetsAndRepetitionsLayout.setVisibility(View.VISIBLE);
                    exerciseSets.setText(String.valueOf(cardioSetsNumber));
                    exerciseReps.setText(String.valueOf(cardioRepsNumber));
                }
            }
        });
        nextExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                ShowExercise(cardioExercises.get(0));
                if(cardioUsesTime)
                {
                    exerciseTimeLayout.setVisibility(View.VISIBLE);
                    exerciseSetsAndRepetitionsLayout.setVisibility(GONE);
                    exerciseTime.setText(String.valueOf(cardioTimeNumber));
                }
                else{
                    exerciseTimeLayout.setVisibility(GONE);
                    exerciseSetsAndRepetitionsLayout.setVisibility(View.VISIBLE);
                    exerciseSets.setText(String.valueOf(cardioSetsNumber));
                    exerciseReps.setText(String.valueOf(cardioRepsNumber));
                }
            }
        });
    }

    private void ShowExercise(Exercise exercise)
    {
        exerciseImage.setImageResource(exercise.getIcon());
        exerciseName.setText(exercise.name);
    }

    private  void StartCountDown(){
        new CountDownTimer(50000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countText.setText(String.valueOf(counter));
                counter++;
            }
            @Override
            public void onFinish() {
                countText.setText("Finished");
            }
        }.start();
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
        stretchingAmountOfExercises = stretchingTimeValue;
    }

    public void GetPersonalisedExercises(String exercise_type, String body_part){
        DatabaseReference databaseReference = database.getReference("exercises").child(exercise_type).child(body_part);
        currentAmountOfExercises = 0;
        System.out.println("curr: " +cardioAmountOfExercises + ", "+ weightTrainingAmountOfExercises+ ", "+ coreAmountOfExercises);
        if(exercise_type.equals("cardio"))
        {
            currentAmountOfExercises = cardioAmountOfExercises;
        }
        else if(exercise_type.equals("strength") && !body_part.equals("core"))
        {
            currentAmountOfExercises = weightTrainingAmountOfExercises;
        }
        else if(body_part.equals("core") && exercise_type.equals("strength"))
        {
            currentAmountOfExercises = coreAmountOfExercises;
        }
        else if(body_part.equals("total_body") && exercise_type.equals("flexibility"))
        {
            currentAmountOfExercises = stretchingAmountOfExercises;
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
                        else if(exercise_type.equals("flexibility"))
                        {
                            stretchingExercises.add(exercise);
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
                        else if(exercise_type.equals("flexibility"))
                        {
                            stretchingExercises.add(exercise);
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
                        else if(exercise_type.equals("flexibility"))
                        {
                            stretchingExercises.add(exercise);
                        }
                    }
                }
                if(exercise_type.equals("cardio")){
                    cardioExercisesAdapter.notifyDataSetChanged();
                }
                if(exercise_type.equals("strength") && !body_part.equals("core")){
                    weightTrainingExercisesAdapter.notifyDataSetChanged();
                }
                if(body_part.equals("core") && exercise_type.equals("strength")){
                    coreExercisesAdapter.notifyDataSetChanged();
                }
                if(exercise_type.equals("flexibility")){
                    stretchingExercisesAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public Exercise SetExerciseFields(String name, String difficulty, String type, String body_part){
        Exercise exercise = new Exercise();
        String imageFileName = name; //  this is image file name
        String PACKAGE_NAME = this.getPackageName();
        int imgId = this.getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + imageFileName, null, null);
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