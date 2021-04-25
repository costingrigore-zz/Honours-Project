package com.costingrigore.dumbbellapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * This class handles the behaviour shown in the Progress page in the application
 * It handles showing the exercises data for the week, as well as the BMI calculation
 */
public class ProgressFragment extends Fragment {
    /**
     * Initialising the Firebase database
     */
    FirebaseDatabase database;
    /**
     * View storing the view of the fragment
     */
    View view;
    /**
     * Fields used to store the number of easy, medium, and difficult exercises for the cardio, weight training, core, and stretching components, for the current week that the
     * user accesses the application ([0] = Monday, [1] = Tuesday, [2] = Wednesday, [3] = Thursday, [4] = Friday, [5] = Saturday, [6] = Sunday)
     */
    int[] cardioEasyExercises = {0, 0, 0, 0, 0, 0, 0};
    int[] cardioMediumExercises = {0, 0, 0, 0, 0, 0, 0};
    int[] cardioDifficultExercises = {0, 0, 0, 0, 0, 0, 0};
    int[] weightTrainingEasyExercises = {0, 0, 0, 0, 0, 0, 0};
    int[] weightTrainingMediumExercises = {0, 0, 0, 0, 0, 0, 0};
    int[] weightTrainingDifficultExercises = {0, 0, 0, 0, 0, 0, 0};
    int[] coreEasyExercises = {0, 0, 0, 0, 0, 0, 0};
    int[] coreMediumExercises = {0, 0, 0, 0, 0, 0, 0};
    int[] coreDifficultExercises = {0, 0, 0, 0, 0, 0, 0};
    int[] stretchingEasyExercises = {0, 0, 0, 0, 0, 0, 0};
    int[] stretchingMediumExercises = {0, 0, 0, 0, 0, 0, 0};
    int[] stretchingDifficultExercises = {0, 0, 0, 0, 0, 0, 0};
    /**
     * Buttons used to switch between the different components when showing data in the exercises graph in the Progress page
     */
    Button cardioButton;
    Button weightTrainingButton;
    Button coreButton;
    Button stretchingButton;
    /**
     * Fields used to store the weight and height of the user, used for calculating and displaying the user's BMI (Body Mass Index)
     */
    int weight = 0;
    int height = 0;

    /**
     * Constructor of the class
     */
    public ProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressFragment newInstance() {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment gets created
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called when the fragment's view is created
     *
     * @param inflater           Fragment's inflater
     * @param container          Fragment's ViewGroup
     * @param savedInstanceState Fragment's instance
     * @return It returns the fragments' view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_progress, container, false);
        //Getting the Firebase instance
        database = FirebaseDatabase.getInstance();
        //Setting the buttons used to switch between the different components' exercise data
        cardioButton = (Button) view.findViewById(R.id.cardioButton);
        weightTrainingButton = (Button) view.findViewById(R.id.weightTrainingButton);
        coreButton = (Button) view.findViewById(R.id.coreButton);
        stretchingButton = (Button) view.findViewById(R.id.stretchingButton);
        cardioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///When the user's clicks the button the contents of the graph showing exercises will show data from the cardio exercises
                GetExercisesForThisWeek(cardioEasyExercises, cardioMediumExercises, cardioDifficultExercises);
            }
        });
        weightTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the user's clicks the button the contents of the graph showing exercises will show data from the weight training exercises
                GetExercisesForThisWeek(weightTrainingEasyExercises, weightTrainingMediumExercises, weightTrainingDifficultExercises);
            }
        });
        coreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the user's clicks the button the contents of the graph showing exercises will show data from the core exercises
                GetExercisesForThisWeek(coreEasyExercises, coreMediumExercises, coreDifficultExercises);
            }
        });
        stretchingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the user's clicks the button the contents of the graph showing exercises will show data from the stretching exercises
                GetExercisesForThisWeek(stretchingEasyExercises, stretchingMediumExercises, stretchingDifficultExercises);
            }
        });
        //Getting the user's personal ID using the GetPersonalID method
        String ID = GetPersonalID(view);
        //Getting the database's reference, using the user's personal ID
        DatabaseReference databaseReference = database.getReference("users").child(ID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Getting the current week of the month
                String weekOfMonth = String.valueOf(Calendar.getInstance().get(Calendar.WEEK_OF_MONTH));
                //Getting the current year
                String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                //Getting the current month, as the month list starts with 0 for January, to get the current month one is added to the instance
                String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
                //String storing the days of the week
                String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                //Getting the weight and height of the current user
                weight = Integer.parseInt(snapshot.child("weight").getValue(String.class));
                height = Integer.parseInt(snapshot.child("height").getValue(String.class));
                //Loop used to go through all the days of the current week, and retrieve exercise data for all the components shown in the graph (cardio, weight training, core, and stretching)
                for (int i = 0; i < days.length; i++) {
                    //String used to store the day of the week to be accessed from the database
                    String day = days[i];
                    //Checking if the day of the week contains data, by checking if the day has data about the number of cardio easy exercises
                    if (snapshot.child("workouts").child(year).child(month).child(weekOfMonth).child(days[i]).child("cardio_easy_exercises").getValue(String.class) != null) {
                        //Strings used to contain the easy, medium, and difficult number of exercises for the cardio, weight training, core, and stretching components
                        String cardioEasyExercisesString = snapshot.child("workouts").child(year).child(month).child(weekOfMonth).child(day).child("cardio_easy_exercises").getValue(String.class);
                        String cardioMediumExercisesString = snapshot.child("workouts").child(year).child(month).child(weekOfMonth).child(day).child("cardio_medium_exercises").getValue(String.class);
                        String cardioDifficultExercisesString = snapshot.child("workouts").child(year).child(month).child(weekOfMonth).child(day).child("cardio_difficult_exercises").getValue(String.class);
                        String weightTrainingEasyExercisesString = snapshot.child("workouts").child(year).child(month).child(weekOfMonth).child(day).child("weight_training_easy_exercises").getValue(String.class);
                        String weightTrainingMediumExercisesString = snapshot.child("workouts").child(year).child(month).child(weekOfMonth).child(day).child("weight_training_medium_exercises").getValue(String.class);
                        String weightTrainingDifficultExercisesString = snapshot.child("workouts").child(year).child(month).child(weekOfMonth).child(day).child("weight_training_difficult_exercises").getValue(String.class);
                        String coreEasyExercisesString = snapshot.child("workouts").child(year).child(month).child(weekOfMonth).child(day).child("core_easy_exercises").getValue(String.class);
                        String coreMediumExercisesString = snapshot.child("workouts").child(year).child(month).child(weekOfMonth).child(day).child("core_medium_exercises").getValue(String.class);
                        String coreDifficultExercisesString = snapshot.child("workouts").child(year).child(month).child(weekOfMonth).child(day).child("core_difficult_exercises").getValue(String.class);
                        String stretchingEasyExercisesString = snapshot.child("workouts").child(year).child(month).child(weekOfMonth).child(day).child("stretching_easy_exercises").getValue(String.class);
                        String stretchingMediumExercisesString = snapshot.child("workouts").child(year).child(month).child(weekOfMonth).child(day).child("stretching_medium_exercises").getValue(String.class);
                        String stretchingDifficultExercisesString = snapshot.child("workouts").child(year).child(month).child(weekOfMonth).child(day).child("stretching_difficult_exercises").getValue(String.class);
                        if (cardioEasyExercisesString != null) {
                            cardioEasyExercises[i] = Integer.parseInt(cardioEasyExercisesString);
                        }
                        if (cardioMediumExercisesString != null) {
                            cardioMediumExercises[i] = Integer.parseInt(cardioMediumExercisesString);
                        }
                        if (cardioDifficultExercisesString != null) {
                            cardioDifficultExercises[i] = Integer.parseInt(cardioDifficultExercisesString);
                        }
                        if (weightTrainingEasyExercisesString != null) {
                            weightTrainingEasyExercises[i] = Integer.parseInt(weightTrainingEasyExercisesString);
                        }
                        if (weightTrainingMediumExercisesString != null) {

                            weightTrainingMediumExercises[i] = Integer.parseInt(weightTrainingMediumExercisesString);
                        }
                        if (weightTrainingDifficultExercisesString != null) {
                            weightTrainingDifficultExercises[i] = Integer.parseInt(weightTrainingDifficultExercisesString);
                        }
                        if (coreEasyExercisesString != null) {
                            coreEasyExercises[i] = Integer.parseInt(coreEasyExercisesString);
                        }
                        if (coreMediumExercisesString != null) {
                            coreMediumExercises[i] = Integer.parseInt(coreMediumExercisesString);
                        }
                        if (coreDifficultExercisesString != null) {
                            coreDifficultExercises[i] = Integer.parseInt(coreDifficultExercisesString);
                        }
                        if (stretchingEasyExercisesString != null) {
                            stretchingEasyExercises[i] = Integer.parseInt(stretchingEasyExercisesString);
                        }
                        if (stretchingMediumExercisesString != null) {
                            stretchingMediumExercises[i] = Integer.parseInt(stretchingMediumExercisesString);
                        }
                        if (stretchingDifficultExercisesString != null) {
                            stretchingDifficultExercises[i] = Integer.parseInt(stretchingDifficultExercisesString);
                        }
                    }
                }
                //By default the graph will show the exercises data from the cardio component
                GetExercisesForThisWeek(cardioEasyExercises, cardioMediumExercises, cardioDifficultExercises);
                //Calling the method that hadles the BMI calculation and displaying the data to the user in the form of a graph
                BMICalculator();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Returning the view
        return view;
    }

    /**
     * This method is used to show the user's BMI (Body Mass Index) in a graph
     */
    public void BMICalculator() {
        //Getting the graph where the data will be displayed
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        //Array used to store the height, representing the height of the user in the X-axis of the array
        double[] heightArray = new double[100];
        //Filling the height array with height values between 140cm and 220cm
        for (int i = 140; i < (heightArray.length + 140); i++) {
            heightArray[i - 140] = i;
        }
        //Array used to store the weight, representing the weight of the user in the Y-axis of the array
        double[] weightArray = new double[heightArray.length];
        //Double used to to calculate the BMI of an obese person
        double BMI = 30;
        //Creating the values needed to show a line representing the obese line in the BMI graph
        for (int i = 0; i < heightArray.length; i++) {
            double weightToAdd = (heightArray[i] / 100) * (heightArray[i] / 100) * BMI;
            weightArray[i] = weightToAdd;
        }
        //Creating the line/series representing the top of the obese region of the BMI graph
        LineGraphSeries<DataPoint> series0 = new LineGraphSeries<DataPoint>();
        //Appending the data from the heightArray and from the weightArray to the series
        for (int z = 0; z < heightArray.length; z++) {
            series0.appendData(new DataPoint(heightArray[z], 200), true, weightArray.length);
        }
        //Setting the background of the series
        series0.setBackgroundColor(Color.parseColor("#BFff0059"));
        series0.setDrawBackground(true);
        series0.setColor(Color.WHITE);
        //Adding the series to the BMI graph
        graph.addSeries(series0);
        //Creating the line/series representing the bottom of the obese region of the BMI graph
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>();
        //Appending the data from the heightArray and from the weightArray to the series
        for (int z = 0; z < weightArray.length; z++) {
            series1.appendData(new DataPoint(heightArray[z], weightArray[z]), true, weightArray.length);
        }
        //Setting the background of the series
        series1.setBackgroundColor(Color.parseColor("#BFFFEB3B"));
        series1.setDrawBackground(true);
        series1.setColor(Color.WHITE);
        //Adding the series to the BMI graph
        graph.addSeries(series1);
        //Double used to to calculate the BMI of an overweight person
        BMI = 25;
        //Creating the values needed to show a line representing the overweight line in the BMI graph
        for (int i = 0; i < heightArray.length; i++) {
            double weightToAdd = (heightArray[i] / 100) * (heightArray[i] / 100) * BMI;
            weightArray[i] = weightToAdd;
        }
        //Creating the line/series representing the overweight region of the BMI graph
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>();
        //Appending the data from the heightArray and from the weightArray to the series
        for (int z = 0; z < weightArray.length; z++) {
            series2.appendData(new DataPoint(heightArray[z], weightArray[z]), true, weightArray.length);
        }
        //Setting the background of the series
        series2.setBackgroundColor(Color.parseColor("#BF8BC34A"));
        series2.setDrawBackground(true);
        series2.setColor(Color.WHITE);
        //Adding the series to the BMI graph
        graph.addSeries(series2);
        //Double used to to calculate the BMI of an average person
        BMI = 18.5;
        //Creating the values needed to show a line representing the average weight line in the BMI graph
        for (int i = 0; i < heightArray.length; i++) {
            double weightToAdd = (heightArray[i] / 100) * (heightArray[i] / 100) * BMI;
            weightArray[i] = weightToAdd;
        }
        //Creating the values needed to show a line representing the average weight line in the BMI graph
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>();
        //Appending the data from the heightArray and from the weightArray to the series
        for (int z = 0; z < weightArray.length; z++) {
            series3.appendData(new DataPoint(heightArray[z], weightArray[z]), true, weightArray.length);
        }
        //Setting the background of the series
        series3.setBackgroundColor(Color.parseColor("#BFFF5722"));
        series3.setDrawBackground(true);
        series3.setColor(Color.WHITE);
        //Adding the series to the BMI graph
        graph.addSeries(series3);
        //Creating a data point used to show the user's BMI in the graph based on the user's weight and height
        PointsGraphSeries<DataPoint> series4 = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(height, weight),
        });
        //Setting the color of the data point
        series4.setColor(Color.BLUE);
        //Adding series to the BMI graph
        graph.addSeries(series4);
        //Setting the BMI graph's labels, the labels' colours as well as the axis title's colours
        graph.getGridLabelRenderer().setVerticalAxisTitle("Weight");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Height");
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
    }

    /**
     * This method is used to show data in the exercises graph of the Progress page
     *
     * @param easyExercises      array used to store the easy exercises of a component
     * @param mediumExercises    array used to store the medium exercises of a component
     * @param difficultExercises array used to store the difficult exercises of a component
     */
    public void GetExercisesForThisWeek(int[] easyExercises, int[] mediumExercises, int[] difficultExercises) {
        //Setting up the graph used to show the exercises data
        GraphView graph = (GraphView) view.findViewById(R.id.graph2);
        //Removing any previous series from cache, as whenever this method would be called the new data would appear on top of the previous data
        graph.removeAllSeries();
        //Creating a line/series to show the easy exercises completed during a week
        LineGraphSeries<DataPoint> easyExercisesSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, easyExercises[0]),
                new DataPoint(2, easyExercises[1]),
                new DataPoint(3, easyExercises[2]),
                new DataPoint(4, easyExercises[3]),
                new DataPoint(5, easyExercises[4]),
                new DataPoint(6, easyExercises[5]),
                new DataPoint(7, easyExercises[6])
        }
        );
        easyExercisesSeries.setDrawDataPoints(true);
        //Easy exercises are represented in green in the graph
        easyExercisesSeries.setColor(Color.GREEN);
        easyExercisesSeries.setDrawDataPoints(true);
        easyExercisesSeries.setThickness(8);
        //Adding the easy exercises line/series to the graph
        graph.addSeries(easyExercisesSeries);
        //Creating a line/series to show the medium exercises completed during a week
        LineGraphSeries<DataPoint> mediumExercisesSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, mediumExercises[0]),
                new DataPoint(2, mediumExercises[1]),
                new DataPoint(3, mediumExercises[2]),
                new DataPoint(4, mediumExercises[3]),
                new DataPoint(5, mediumExercises[4]),
                new DataPoint(6, mediumExercises[5]),
                new DataPoint(7, mediumExercises[6])
        }
        );
        mediumExercisesSeries.setDrawDataPoints(true);
        //Medium difficulty exercises are represented in yellow in the graph
        mediumExercisesSeries.setColor(Color.YELLOW);
        mediumExercisesSeries.setDrawDataPoints(true);
        mediumExercisesSeries.setThickness(8);
        //Adding the medium difficulty exercises line/series to the graph
        graph.addSeries(mediumExercisesSeries);
        //Creating a line/series to show the difficult exercises completed during a week
        LineGraphSeries<DataPoint> difficultExercisesSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, difficultExercises[0]),
                new DataPoint(2, difficultExercises[1]),
                new DataPoint(3, difficultExercises[2]),
                new DataPoint(4, difficultExercises[3]),
                new DataPoint(5, difficultExercises[4]),
                new DataPoint(6, difficultExercises[5]),
                new DataPoint(7, difficultExercises[6])
        }
        );
        difficultExercisesSeries.setDrawDataPoints(true);
        //Difficult exercises are represented in red in the graph
        difficultExercisesSeries.setColor(Color.RED);
        difficultExercisesSeries.setDrawDataPoints(true);
        difficultExercisesSeries.setThickness(8);
        //Adding the difficult exercises line/series to the graph
        graph.addSeries(difficultExercisesSeries);
        //Setting up a static labels formatter for the graph
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        //Setting up the grid label for the vertical axis and also the colour of the grid label
        graph.getGridLabelRenderer().setVerticalAxisTitle("Number of exercises");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        //Setting up the grid label for the horizontal axis and also the colour of the grid label
        staticLabelsFormatter.setHorizontalLabels(new String[]{"M", "T", "W", "Th", "F", "S", "Su"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        //Adding some padding to the grid label renderer
        glr.setPadding(60);
        //Setting the grid colour to white
        glr.setGridColor(Color.WHITE);
    }

    /**
     * This method is used to retrieve the user's personal ID from the device's storage
     *
     * @param view The fragment's view
     * @return It returns the user's personal ID as a String
     */
    public String GetPersonalID(View view) {
        //Try reading the contents of the file containing the user's personal ID
        try {
            //Creating a file input stream, using user_data.txt as the input file
            FileInputStream fileIn = view.getContext().openFileInput("user_data.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            //Setting a character array to store the first 100 characters of the input stream
            char[] inputBuffer = new char[100];
            //String used to store the user's personal ID
            String userPersonalID = "";
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
            userPersonalID = strArray[1];
            //Return the the user's personal ID
            return userPersonalID;
        } catch (Exception e) {
            e.printStackTrace();
            //If the file could not be read, return "String not found"
            return "String not found";
        }
    }
}