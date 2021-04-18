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
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseDatabase database;
    View view;
    int[] cardioEasyExercises = {0,0,0,0,0,0,0};
    int[] cardioMediumExercises = {0,0,0,0,0,0,0};
    int[] cardioDifficultExercises = {0,0,0,0,0,0,0};
    int[] weightTrainingEasyExercises = {0,0,0,0,0,0,0};
    int[] weightTrainingMediumExercises = {0,0,0,0,0,0,0};
    int[] weightTrainingDifficultExercises = {0,0,0,0,0,0,0};
    int[] coreEasyExercises = {0,0,0,0,0,0,0};
    int[] coreMediumExercises = {0,0,0,0,0,0,0};
    int[] coreDifficultExercises = {0,0,0,0,0,0,0};
    int[] stretchingEasyExercises = {0,0,0,0,0,0,0};
    int[] stretchingMediumExercises = {0,0,0,0,0,0,0};
    int[] stretchingDifficultExercises = {0,0,0,0,0,0,0};
    Button cardioButton;
    Button weightTrainingButton;
    Button coreButton;
    Button stretchingButton;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int weight = 0;
    int height = 0;

    public ProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InsightsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressFragment newInstance(String param1, String param2) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_progress, container, false);
        //Getting the firebase's instance
        database = FirebaseDatabase.getInstance();
        cardioButton = (Button) view.findViewById(R.id.cardioButton);
        weightTrainingButton = (Button) view.findViewById(R.id.weightTrainingButton);
        coreButton = (Button) view.findViewById(R.id.coreButton);
        stretchingButton = (Button) view.findViewById(R.id.stretchingButton);
        cardioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the user's clicks the button a new activity opens, using the openNewActivity method
                GetExercisesForThisWeek(cardioEasyExercises,cardioMediumExercises,cardioDifficultExercises);
            }
        });
        weightTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the user's clicks the button a new activity opens, using the openNewActivity method
                GetExercisesForThisWeek(weightTrainingEasyExercises,weightTrainingMediumExercises,weightTrainingDifficultExercises);
            }
        });
        coreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the user's clicks the button a new activity opens, using the openNewActivity method
                GetExercisesForThisWeek(coreEasyExercises,coreMediumExercises,coreDifficultExercises);
            }
        });
        stretchingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the user's clicks the button a new activity opens, using the openNewActivity method
                GetExercisesForThisWeek(stretchingEasyExercises,stretchingMediumExercises,stretchingDifficultExercises);
            }
        });
        //Getting the user's personal ID using the GetPersonalID method
        String ID = GetPersonalID(view);
        //Getting the database's reference, using the user's personal ID
        DatabaseReference databaseReference = database.getReference("users").child(ID);

         databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
        String weekOfMonth = String.valueOf(Calendar.getInstance().get(Calendar.WEEK_OF_MONTH));
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
        String[] days = {"Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday","Sunday"};
        weight = Integer.parseInt(snapshot.child("weight").getValue(String.class));
        height = Integer.parseInt(snapshot.child("height").getValue(String.class));
        for(int i=0; i<days.length; i++)
        {
            String day = days[i];
            if(snapshot.child("workouts").child(year).child(month).child(weekOfMonth).child(days[i]).child("cardio_easy_exercises").getValue(String.class) != null) {
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
                if(cardioEasyExercisesString!=null){
                    cardioEasyExercises[i] = Integer.parseInt(cardioEasyExercisesString);
                }
                if(cardioMediumExercisesString!=null) {
                    cardioMediumExercises[i] = Integer.parseInt(cardioMediumExercisesString);
                }
                if(cardioDifficultExercisesString!=null) {
                    cardioDifficultExercises[i] = Integer.parseInt(cardioDifficultExercisesString);
                }
                if(weightTrainingEasyExercisesString!=null) {
                    weightTrainingEasyExercises[i] = Integer.parseInt(weightTrainingEasyExercisesString);
                }
                if(weightTrainingMediumExercisesString!=null) {

                    weightTrainingMediumExercises[i] = Integer.parseInt(weightTrainingMediumExercisesString);
                }
                if(weightTrainingDifficultExercisesString!=null) {
                    weightTrainingDifficultExercises[i] = Integer.parseInt(weightTrainingDifficultExercisesString);
                }
                if(coreEasyExercisesString!=null) {
                    coreEasyExercises[i] = Integer.parseInt(coreEasyExercisesString);
                }
                if(coreMediumExercisesString!=null) {
                    coreMediumExercises[i] = Integer.parseInt(coreMediumExercisesString);
                }
                if(coreDifficultExercisesString!=null) {
                    coreDifficultExercises[i] = Integer.parseInt(coreDifficultExercisesString);
                }
                if(stretchingEasyExercisesString!=null) {
                    stretchingEasyExercises[i] = Integer.parseInt(stretchingEasyExercisesString);
                }
                if(stretchingMediumExercisesString!=null) {
                    stretchingMediumExercises[i] = Integer.parseInt(stretchingMediumExercisesString);
                }
                if(stretchingDifficultExercisesString!=null) {
                    stretchingDifficultExercises[i] = Integer.parseInt(stretchingDifficultExercisesString);
                }
                }
            }
                GetExercisesForThisWeek(cardioEasyExercises,cardioMediumExercises,cardioDifficultExercises);
                BMICalculator();
            System.out.println(cardioEasyExercises[0] + " " + cardioEasyExercises[1]);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
        });


        return view;
    }

    public void BMICalculator()
    {
        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        double[] heightArray = new double[100];//{142,147,152,157,163,168,173,178,183,188,193,198,203,208};
        for(int i = 140; i<(heightArray.length + 140);i++)
        {
            heightArray[i-140] = i;
        }
        double[] weightArray = new double[heightArray.length];
        double BMI = 30;
        for(int i = 0; i < heightArray.length; i++)
        {
            double weightToAdd = (heightArray[i]/100)*(heightArray[i]/100)*BMI;
            weightArray[i] = weightToAdd;
        }
        LineGraphSeries<DataPoint> series0 = new LineGraphSeries<DataPoint>();
        for(int z =0; z< heightArray.length; z++)
        {
            series0.appendData(new DataPoint(heightArray[z],200),true, weightArray.length);
        };
        series0.setBackgroundColor(Color.parseColor("#BFff0059"));
        series0.setDrawBackground(true);
        series0.setColor(Color.WHITE);
        graph.addSeries(series0);
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>();
        for(int z =0; z< weightArray.length; z++)
        {
            series1.appendData(new DataPoint(heightArray[z],weightArray[z]),true, weightArray.length);
        };
        series1.setBackgroundColor(Color.parseColor("#BFFFEB3B"));
        series1.setDrawBackground(true);
        series1.setColor(Color.WHITE);
        graph.addSeries(series1);
        BMI = 25;
        for(int i = 0; i < heightArray.length; i++)
        {
            double weightToAdd = (heightArray[i]/100)*(heightArray[i]/100)*BMI;
            weightArray[i] = weightToAdd;
        }
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>();
        for(int z =0; z< weightArray.length; z++)
        {
            series2.appendData(new DataPoint(heightArray[z],weightArray[z]),true, weightArray.length);
        };
        series2.setBackgroundColor(Color.parseColor("#BF8BC34A"));
        series2.setDrawBackground(true);
        series2.setColor(Color.WHITE);
        graph.addSeries(series2);
        BMI = 18.5;
        for(int i = 0; i < heightArray.length; i++)
        {
            double weightToAdd = (heightArray[i]/100)*(heightArray[i]/100)*BMI;
            weightArray[i] = weightToAdd;
        }
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>();
        for(int z =0; z< weightArray.length; z++)
        {
            series3.appendData(new DataPoint(heightArray[z],weightArray[z]),true, weightArray.length);
        };
        series3.setBackgroundColor(Color.parseColor("#BFFF5722"));
        series3.setDrawBackground(true);
        series3.setColor(Color.WHITE);
        //graph.setBackgroundColor(Color.RED);
        graph.addSeries(series3);
        PointsGraphSeries<DataPoint> series4 = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(height, weight),
        });
        series4.setColor(Color.BLUE);
        graph.addSeries(series4);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Weight");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Height");
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
    }

    public void GetExercisesForThisWeek(int[] easyExercises, int[] mediumExercises, int[] difficultExercises)
    {
        GraphView graph = (GraphView) view.findViewById(R.id.graph2);
        graph.removeAllSeries();
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
        easyExercisesSeries.setColor(Color.GREEN);
        easyExercisesSeries.setDrawDataPoints(true);
        easyExercisesSeries.setThickness(8);
        graph.addSeries(easyExercisesSeries);

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
        mediumExercisesSeries.setColor(Color.YELLOW);
        mediumExercisesSeries.setDrawDataPoints(true);
        mediumExercisesSeries.setThickness(8);
        graph.addSeries(mediumExercisesSeries);

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
        difficultExercisesSeries.setColor(Color.RED);
        difficultExercisesSeries.setDrawDataPoints(true);
        difficultExercisesSeries.setThickness(8);
        graph.addSeries(difficultExercisesSeries);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Number of exercises");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"M", "T", "W", "Th", "F", "S", "Su"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setPadding(60);
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