package com.example.fittslaw_tablet2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME_1D = "1D_Fitts#3_";
    private static final String FILE_NAME_2D = "2D_Fitts#3_";

    private static String[] HEADER_Log = {"ID", "Finger size", "Experiment", "Trial", "Counter","Axis_Major", "Axis_Minor", "A_mm", "W_mm", "A_px", "W_px"};

    private static String[] HEADER = {"ID", "Experiment", "Finger size", "Trial", "Block", "successfulTrialInBlock",
            "Block_Group", "Task_Direction", "Button_Width",
            "A_mm", "W_mm", "A_px", "W_px", "Time_Down", "Time_Up",  "X-Start-Finger-Down",
            "Y-Start-Finger-Down", "X-Start-Finger-Up", "Y-Start-Finger-Up",
            "X-End-Finger-Down", "Y-End-Finger-Down", "X-End-Finger-Up", "Y-End-Finger-Up",
            "X-Start", "Y-Start", "X-Target", "Y-Target", "X-Target-Center", "Y-TLarget-Center", "X-Distance-Down",
            "Y-Distance-Down", "X-Distance-Up", "Y-Distance-Up", "Hit", "Outlier", "Outlier_px_center", "Outlier_mm_center", "Outlier_px_boarder", "Outlier_mm_boarder",
            "Axis_Major_Down",  "Axis_Minor_Down",  "Axis_Major_Up",  "Axis_Minor_Up", "Avg_Axis_Major", "Avg_Axis_Minor"};


    private static int INDEX_OF_ID = 0;
    private static int INDEX_OF_Experiment = 1;
    private static int INDEX_OF_Mode = 2;


    private static int INDEX_OF_Trail = 3;
    private static int INDEX_OF_Block_Number = 4;
    private static int INDEX_OF_Trail_In_Block = 5;
    private static int INDEX_OF_Group = 6;

    private static int INDEX_OF_BtnOrder = 7;
    private static int INDEX_OF_BtnWidth = 8;

    private static int INDEX_OF_A = 9;
    private static int INDEX_OF_W = 10;

    private static int INDEX_OF_A_px = 11;
    private static int INDEX_OF_W_px = 12;

    private static int INDEX_OF_Time = 13;
    private static int INDEX_OF_Time_up = 14;


    //finger at Start
    private static int INDEX_OF_X_Start_Down = 15;
    private static int INDEX_OF_Y_Start_Down = 16;
    private static int INDEX_OF_X_Start_Up = 17;
    private static int INDEX_OF_Y_Start_Up = 18;
    //finger at Target
    private static int INDEX_OF_X_End_Down = 19;
    private static int INDEX_OF_Y_End_Down = 20;
    private static int INDEX_OF_X_End_Up = 21;
    private static int INDEX_OF_Y_End_Up = 22;

    private static int INDEX_OF_Start_X = 23;
    private static int INDEX_OF_Start_Y = 24;

    private static int INDEX_OF_Target_X = 25;
    private static int INDEX_OF_Target_Y = 26;

    private static int INDEX_OF_TARGET_CENTER_X = 27;
    private static int INDEX_OF_TARGET_CENTER_Y = 28;
    private static int INDEX_OF_DISTANCE_X_DOWN = 29;
    private static int INDEX_OF_DISTANCE_Y_DOWN = 30;
    private static int INDEX_OF_DISTANCE_X_UP = 31;
    private static int INDEX_OF_DISTANCE_Y_UP = 32;
    private static int INDEX_OF_Hit = 33;
    private static int INDEX_OF_OUTLIER = 34;
    private static int INDEX_OF_OUTLIER_PX_center = 35;
    private static int INDEX_OF_OUTLIER_MM_center = 36;
    private static int INDEX_OF_OUTLIER_PX_boarder = 37;
    private static int INDEX_OF_OUTLIER_MM_boarder = 38;
    private static int INDEX_OF_AXIS_L_Down = 39;
    private static int INDEX_OF_AXIS_S_Down = 40;
    private static int INDEX_OF_AXIS_L_Up = 41;
    private static int INDEX_OF_AXIS_S_Up = 42;
    private static int INDEX_OF_AXIS_AVG_L = 43;
    private static int INDEX_OF_AXIS_AVG_S = 44;
    private static int INDEX_WH = 0;

    //Settings ---------------------------------------------------
    //private static final int START_BAR_WIDE = 6; //in mm
    int START_BAR_WIDE = 0;  //is set depending on fingerSize

    private static final int OUTLIER_DISTANCE_Boarder = 15; //in mm
    private float OUTLIER_DISTANCE_PX_Boarder;

    //todo EDIT here
    private int NUMBER_OF_GROUPS = 3;
    //Number of combinations Sets (combination set = one for each A & W & side & order)
    //private static final int NUMBER_OF_COMBINATION_SET_PER_BLOCK = 1; // 1x (3*3*2*2) = 36 trails per block)

    private static int THUMB_WIDTH = 12; //mm
    //right and left of the screen
    private static int BOARDER = 5; //mm
    //todo difference?!
    private static final int PADDING = 5; //mm

    //A Levels
    // Distance from the center of start to the center of the target
    private static final int A2 = 50;
    //private static final int A3 = 80;
    private static final int A4 = 110;
    private static final int A5 = 150;
    private static final int[] A = {A2, A4, A5};

    //W Levels
    // diameter of the target circle

    //width
    private static final  float W1 = 2.4f;
    private static final  float W2 = 5.0f;
    private static final  float W3 = 8.0f;
    private static final  float W4 = 7.2f;
    private static final  float W5 = 5.0f;
    private static final  float W6 = 10.0f;
    private static final  float W7 = 10.0f;
    private static final  float W8 = 7.2f;
    private static final  float W9 = 15.0f;

    //heigh
    private static final float H1 = 8.0f;
    private static final float H2 = 2.4f;
    private static final float H3 = 5.0f;
    private static final float H4 = 7.2f;
    private static final float H5 = 5.0f;
    private static final float H6 = 2.4f;
    private static final float H7 = 7.2f;
    private static final float H8 = 5.0f;
    private static final float H9 = 4.8f;

    private static final float[] W = {W1, W2, W3, W4, W5, W6, W7,W8,W9};
    private static final float[] H = {H1, H2, H3, H4, H5, H6, H7,H8,H9};
    private static float[][] WH = new float[W.length][2];

    /*float[] W0 = new float[]{2.4f, 4.8f, 7.2f, 10.0f, 15.0f, 20.0f, 25.0f};
    float[] W20 = new float[]{3.0f, 6.0f, 9.0f, 12.5f, 18.75f, 25.0f, 31.25f};
    float[] W25 = new float[]{3.75f, 7.5f, 11.25f, 15.63f, 23.44f, 31.25f, 39.06f};
    float[] W30 = new float[]{4.5f, 9.0f, 13.5f, 18.75f, 28.13f, 37.5f, 46.88f};
    float[] W35 = new float[]{5.25f, 10.5f, 15.75f, 21.88f, 32.81f, 43.75f, 54.69f};*/



    //Colors
    private static final int DEFAULT_COLOR_START = Color.parseColor("#999999"); //GREY #999999
    private static final int SUCCESS_COLOR_START = Color.parseColor("#0066FF");  //BLUE #0066FF

    private static final int DEFAULT_COLOR_TARGET = Color.parseColor("#FFFF00"); //YELLOW #FFFF00
    private static final int SUCCESS_COLOR_TARGET = Color.parseColor("#00CC00"); //GREEN #00CC00
    private static final int FAIL_COLOR_TARGET = Color.parseColor("#FF0000"); //RED #FF0000

    //---------------------------------------------------------------------------------------------

    private static final int PERMISSION_CONSTANT = 2;


    private String id;
    private String number_of_groups;
    private String m;
    private String e;
    private String f;
    private String startWidth;
    private Boolean oneD;

   // private float[] W = new float[7];

   private Button startBar;
   private Button targetBar;
   private Button targetCircle;
   private float target_center_x;
   private float target_center_y;
   private Boolean outlier_down;
   private float currentW;

   private Boolean started;
   private long startTime;
   private long timeNeeded;

    private View myView;
    private TextView scoreView;
    private Integer score;
    private double hitPercentage;
    private Integer trailCounter;
    private Integer blockCounter;
    private Integer groupCounter;
    private Integer successful_trailsPerBlockCounter;
    private Integer successful_trailsPerGroupCounter;
    private Integer maxTrailsPerBlock;
    private Integer maxTrailsPerGroup;
    private boolean endOfBlock;
    private boolean endOfGroup;
    private Vector blockVector;
    private int totalBlocksDone;
    private List<Vector> blockVectors;

    private List<float[]> axisValues;
    private int axisCounter;

    private Integer trailIndex;
    private Integer blockIndex;

   // private ScrollView breakView;
    private Button  endBreak;
    private TextView breakText;
    private float defaultBtnPositionY;
    private float defaultBtnPositionX;

    private String[] data;
    private String[] log;

    private MediaPlayer hitPlayer;
    private MediaPlayer missPlayer;
    private FileOutputStream dataStream;
    private FileOutputStream logStream;
    private File file;


    @Override
    public void onBackPressed() {
        //do nothing
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createWH();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        id = i.getStringExtra("id");
        number_of_groups = i.getStringExtra("blocks");
        m = i.getStringExtra("mode");
        e = i.getStringExtra("experiment");
        f = i.getStringExtra("ignoreFinger");
        startWidth = i.getStringExtra("btn_width");

        switch (m){
            case "20mm":
               // W = W20;
                START_BAR_WIDE = 7; //7.5
                THUMB_WIDTH = 20; //mm
                break;
            case "25mm":
              // W = W25;
                START_BAR_WIDE = 9; //9.4
                THUMB_WIDTH = 25; //mm
                break;
            case "30mm":
               // W = W30;
                START_BAR_WIDE = 11; //11
                THUMB_WIDTH = 30; //mm
                break;
            case "35mm":
               // W = W35;
                START_BAR_WIDE = 13; //13
                THUMB_WIDTH = 35; //mm
                break;
            default:
                System.out.println("Error finger size.");
        }
        //If we ignore finger size all the targets will be same as for finger W20
       /* if(f.equals("true")){
            W = W0;
        } */


        if(e.contains("1D")){
            oneD=true;
        }else{
            oneD=false;
        }

        NUMBER_OF_GROUPS = Integer.valueOf(number_of_groups);
        OUTLIER_DISTANCE_PX_Boarder = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, OUTLIER_DISTANCE_Boarder,
                getResources().getDisplayMetrics());

        //MAKE DATA FILE
        getPermission(); //to write Data
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = "";
        if(oneD) {
            path = baseDir + File.separator + FILE_NAME_1D + id + ".txt"; //saved in storage on the phone
        }else {
            path = baseDir + File.separator + FILE_NAME_2D + id + ".txt"; //saved in storage on the phone
        }
        file = new File(path);
        Boolean isNew = !file.exists();


        try {
            dataStream = new FileOutputStream(file, true);
        }catch (IOException e) {
            e.printStackTrace();
        }

        if(isNew) {
            writeData(HEADER);
        }

        //MAKE LOG FILE
        if(oneD) {
            path = baseDir + File.separator + FILE_NAME_1D + "Log.txt"; //saved in storage on the phone
        }else {
            path = baseDir + File.separator + FILE_NAME_2D + "Log.txt"; //saved in storage on the phone
        }
        file = new File(path);
        isNew = !file.exists();

        try {
            logStream = new FileOutputStream(file, true);
        }catch (IOException e) {
            e.printStackTrace();
        }

        if(isNew) {
            writeLog(HEADER_Log);
        }


        myView = findViewById(R.id.layout);
        myView.setBackgroundColor(Color.BLACK); //black


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthWindow = displayMetrics.widthPixels;
        scoreView = findViewById(R.id.scoreView);
        scoreView.setText("0/0");
        scoreView.setTextColor(Color.parseColor("#FFFFFF")); //white

        started = false;
        score = 0;
        trailCounter = 0;
        totalBlocksDone = 0;
        //NEW - Group (= 4 (-rightLeft x topDown) Blocks)
        endOfGroup = true; //so new blockVectors are generated
        groupCounter = 0;
        successful_trailsPerGroupCounter = 0;
        maxTrailsPerGroup = (A.length*W.length*2); //in one group all combinations direction

        //Block (= 45 (-AxW) Trials)
        endOfBlock = true; //so new blockVector is generated
        blockCounter = 0;
        successful_trailsPerBlockCounter = 0;
        maxTrailsPerBlock = (A.length*W.length);

        outlier_down = false;
        startBar = findViewById(R.id.startBtn);
        targetBar = findViewById(R.id.targetBtn);
        targetCircle = findViewById(R.id.target_0);
        if(oneD){
            targetCircle.setVisibility(View.INVISIBLE);
        }else{
            targetBar.setVisibility(View.INVISIBLE);
        }

        endBreak = findViewById(R.id.endBreak1D);
        endBreak.setVisibility(View.INVISIBLE);

        int heightWindow = displayMetrics.heightPixels;
        defaultBtnPositionX = (widthWindow/2);
        defaultBtnPositionY = (heightWindow/3)*2;

        System.out.println("******************************************DISPLAY HEIGHT PX: " + heightWindow);

        breakText = findViewById(R.id.breakTxt1D);
        breakText.setPadding(20, 10, 20, 10);
        breakText.setVisibility(View.INVISIBLE);



        hitPlayer = MediaPlayer.create(MainActivity.this, R.raw.success);
        missPlayer = MediaPlayer.create(MainActivity.this, R.raw.error);

        data = new String[HEADER.length];
        data[INDEX_OF_ID] = id;
        data[INDEX_OF_Mode] = m;
        if(m.equals("20mm")){
            data[INDEX_OF_Mode] = "own width";
        }
        data[INDEX_OF_Experiment] = e;


        log = new String[HEADER_Log.length];
        log[0] = id;
        log[1] = e;
        log[2] = m;

        makeTask();

        endBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               endBreak();
            }
        });

        //Start hit
        startBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!started) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        data[INDEX_OF_X_Start_Down] = String.valueOf((int) event.getRawX());
                        data[INDEX_OF_Y_Start_Down] = String.valueOf((int) event.getRawY());
                        data[INDEX_OF_Start_X] = String.valueOf((int) startBar.getX());
                        data[INDEX_OF_Start_Y] = String.valueOf((int) startBar.getY());

                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int touchX = (int) event.getRawX();
                        int touchY = (int) event.getRawY();
                        boolean inButtonX = true;
                        float thumbWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, THUMB_WIDTH,
                                getResources().getDisplayMetrics());
                        if(startWidth.equals("thumb")) {
                            inButtonX = (startBar.getX() <= touchX && ((startBar.getX() + thumbWidth) >= touchX));
                        }
                        float bar_height_pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, START_BAR_WIDE,
                                getResources().getDisplayMetrics());
                        boolean inButtonY = startBar.getY() <= touchY && (startBar.getY() + bar_height_pixel) >= touchY;
                        if(inButtonX && inButtonY) {
                            startBar.setBackgroundColor(SUCCESS_COLOR_START);
                            startTime = System.currentTimeMillis();
                            started = true;
                            data[INDEX_OF_X_Start_Up] = String.valueOf(touchX);
                            data[INDEX_OF_Y_Start_Up] = String.valueOf(touchY);
                        }
                    }
                }
                return true;
            }

        });

        //Hit Somewhere "wrong"
       myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (started) {
                    targetTouch(event, false);
                }
                return true;
            }
        });

        //Target (Bar) Hit
        targetBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (started) {
                    targetTouch(event, true);
                }
                return true;
            }

        });

        //Target (Circle) Hit
        targetCircle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (started) {
                    targetTouch(event, true);
                }
                return true;
            }

        });

    }

    private void createWH() {
        for(int i=0;i<WH.length;i++){
                WH[i][0]=W[i];
                WH[i][1]=H[i];
        }
    }


    public void targetTouch(MotionEvent event, Boolean hit){

        // ------- TOUCH DOWN ----------
       if (event.getAction() == MotionEvent.ACTION_DOWN) {
           //RECORD TOUCH AXIS
           axisValues = new ArrayList<float[]>();
           axisCounter = 0;
           float[] tuple = new float[2];
           tuple[0] = event.getTouchMajor();
           tuple[1] = event.getTouchMinor();
           axisValues.add(tuple);
           makeLogEntry(event);
           data[INDEX_OF_AXIS_L_Down] = String.valueOf(event.getTouchMajor()).replace('.', ',');
           data[INDEX_OF_AXIS_S_Down] = String.valueOf(event.getTouchMinor()).replace('.', ',');

           //STOP TIME
           timeNeeded = System.currentTimeMillis() - startTime;
           float distance_y;
           float distance_x;
           float OUTLIER_DISTANCE = OUTLIER_DISTANCE_Boarder + currentW/2;
           float OUTLIER_DISTANCE_PX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, OUTLIER_DISTANCE,
                   getResources().getDisplayMetrics());

           //** 1D
           if(oneD) {
               //CHANGE COLOR
               if(hit){ targetBar.setBackgroundColor(SUCCESS_COLOR_TARGET);}
               else{ targetBar.setBackgroundColor(FAIL_COLOR_TARGET);}

               //CALCULATE DISTANCE
               distance_y = target_center_y - event.getRawY();
               data[INDEX_OF_DISTANCE_Y_DOWN] = String.valueOf((int) distance_y);
               if(Math.abs(distance_y) >= OUTLIER_DISTANCE_PX){
                   outlier_down = true;
               }


           //** 2D
           }else{
               //CHANGE COLOR
               targetCircle.getBackground().getCurrent();
               if(hit){ targetCircle.setBackgroundColor(SUCCESS_COLOR_TARGET);}
               else{ targetCircle.setBackgroundColor(FAIL_COLOR_TARGET);}

               //CALCULATE DISTANCE
               distance_y = target_center_y - event.getRawY();
               distance_x = target_center_x - event.getRawX();
               data[INDEX_OF_DISTANCE_Y_DOWN] = String.valueOf((int) distance_y);
               data[INDEX_OF_DISTANCE_X_DOWN] = String.valueOf((int) distance_x);
               if(Math.abs(distance_y) >= OUTLIER_DISTANCE_PX || Math.abs(distance_x) >= OUTLIER_DISTANCE_PX){
                   outlier_down = true;
               }
           }

           //PLAY SOUND
           if(hit){
               if(hitPlayer.isPlaying()) {
                   hitPlayer.stop();
               }
               hitPlayer = MediaPlayer.create(MainActivity.this, R.raw.success);
               hitPlayer.start();
               //increase score
               score++;
           }else {
               if (missPlayer.isPlaying()) {
                   missPlayer.stop();
               }
               missPlayer = MediaPlayer.create(MainActivity.this, R.raw.error);
               missPlayer.start();
           }

           Integer temp = successful_trailsPerBlockCounter+1;
           scoreView.setText(score.toString() + "/" + temp.toString());

           data[INDEX_OF_Time] = String.valueOf(timeNeeded);
           data[INDEX_OF_Trail] = String.valueOf(trailCounter+1);
           if(hit){ data[INDEX_OF_Hit] = "1";}
           else{ data[INDEX_OF_Hit] = "0";}
           data[INDEX_OF_X_End_Down] = String.valueOf((int) event.getRawX());
           data[INDEX_OF_Y_End_Down] = String.valueOf((int) event.getRawY());

       }

       // ------- TOUCH MOVE ----------
       if(event.getAction() == MotionEvent.ACTION_MOVE){
           //RECORD TOUCH AXIS
           float axis_L = event.getTouchMajor();
           float axis_S = event.getTouchMinor();
           float[] tuple = new float[2];
           tuple[0] = axis_L;
           tuple[1] = axis_S;
           axisValues.add(tuple);
           makeLogEntry(event);
       }

       // ------- TOUCH UP ----------
       if (event.getAction() == MotionEvent.ACTION_UP) {
           float distance_y;
           float distance_x;
           boolean outlier_up = false;
           float OUTLIER_DISTANCE = OUTLIER_DISTANCE_Boarder + currentW/2;
           float OUTLIER_DISTANCE_PX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, OUTLIER_DISTANCE,
                   getResources().getDisplayMetrics());

           started = false;

           //STOP TIME #2
           timeNeeded = System.currentTimeMillis() - startTime;

           if(oneD) {
               //CALCULATE DISTANCE
               data[INDEX_OF_Target_X] = String.valueOf((int) targetBar.getX());
               data[INDEX_OF_Target_Y] = String.valueOf((int) targetBar.getY());
               distance_y = target_center_y - event.getRawY();
               data[INDEX_OF_DISTANCE_Y_UP] = String.valueOf((int) distance_y);
               if (Math.abs(distance_y) >= OUTLIER_DISTANCE_PX) {
                   outlier_up = true;
               }


           }else{
               //CALCULATE DISTANCE
               data[INDEX_OF_Target_X] = String.valueOf((int) targetCircle.getX());
               data[INDEX_OF_Target_Y] = String.valueOf((int) targetCircle.getY());
               distance_y = target_center_y - event.getRawY();
               distance_x = target_center_x - event.getRawX();
               data[INDEX_OF_DISTANCE_Y_UP] = String.valueOf((int) distance_y);
               data[INDEX_OF_DISTANCE_X_UP] = String.valueOf((int) distance_x);
               if(Math.abs(distance_y) >= OUTLIER_DISTANCE_PX || Math.abs(distance_x) >= OUTLIER_DISTANCE_PX){
                   outlier_up = true;
               }
           }

           //WRITE DATA
           data[INDEX_OF_Time_up] = String.valueOf(timeNeeded);
           data[INDEX_OF_X_End_Up] = String.valueOf((int) event.getRawX());
           data[INDEX_OF_Y_End_Up] = String.valueOf((int) event.getRawY());
           data[INDEX_OF_TARGET_CENTER_X] = String.valueOf((int) target_center_x);
           data[INDEX_OF_TARGET_CENTER_Y] = String.valueOf((int) target_center_y);
           data[INDEX_OF_Block_Number] = String.valueOf(blockCounter);
           data[INDEX_OF_Trail_In_Block] = String.valueOf(successful_trailsPerBlockCounter+1);
           data[INDEX_OF_Group] = String.valueOf((int) groupCounter);
           data[INDEX_OF_OUTLIER_MM_center] = String.valueOf((int) OUTLIER_DISTANCE);
           data[INDEX_OF_OUTLIER_PX_center] = String.valueOf((int) OUTLIER_DISTANCE_PX);
           data[INDEX_OF_OUTLIER_MM_boarder] = String.valueOf((int) OUTLIER_DISTANCE_Boarder);
           data[INDEX_OF_OUTLIER_PX_boarder] = String.valueOf((int) OUTLIER_DISTANCE_PX_Boarder);

           // AXIS Values
           float[] tuple = new float[2];
           tuple[0] = event.getTouchMajor();
           tuple[1] = event.getTouchMinor();
           axisValues.add(tuple);
           makeLogEntry(event);
           data[INDEX_OF_AXIS_L_Up] = String.valueOf(event.getTouchMajor()).replace('.', ',');
           data[INDEX_OF_AXIS_S_Up] = String.valueOf(event.getTouchMinor()).replace('.', ',');
           setAvgAxisVals();
           axisValues.clear();


           //CHECK FOR OUTLIER
           if(outlier_up || outlier_down){
               data[INDEX_OF_OUTLIER] = String.valueOf((int) 1); //1 = true
               writeData(data);
               trailCounter++;
               outlier_down = false; //reset to false
               //no removing of the index - will return (at random place in block)
               makeTask();

           }else {
               data[INDEX_OF_OUTLIER] = String.valueOf((int) 0); //false
               writeData(data);

               //UPDATE COUNTERS
               blockVector.removeElementAt(trailIndex); //todo like this!!
               //blockVector.remove(trailIndex); //here was the error

               successful_trailsPerBlockCounter++;
               successful_trailsPerGroupCounter++;
               trailCounter++;

               //CHECK FOR END
               if (groupCounter == NUMBER_OF_GROUPS && successful_trailsPerGroupCounter.equals(maxTrailsPerGroup)) {
                   try {
                       dataStream.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

                   Intent i = new Intent(MainActivity.this, InbetweenActivity.class);
                   //should not be needed any more
                   i.putExtra("id", id);
                   i.putExtra("blocks", number_of_groups);
                   i.putExtra("mode", m);
                   i.putExtra("ignoreFinger", f);
                       /* i.putExtra("btn", "Done");
                        i.putExtra("txt", "Thanks for participate");*/
                   i.putExtra("caller", "Main");
                   i.putExtra("experiment", e);

                   startActivity(i);

                   finish();

               } else {
                   //CHECK FOR BREAK
                   if (successful_trailsPerBlockCounter.equals(maxTrailsPerBlock)) {
                       if (successful_trailsPerGroupCounter.equals(maxTrailsPerGroup)) {
                           setGroup_BreakView();
                       } else {
                           setBlock_BreakView();
                       }
                   } else {
                       makeTask();
                   }

               }
           }
       }


   }


    public void setGroup_BreakView(){
        endOfGroup = true;
        endOfBlock = true;
        blockCounter = 0;

        //breakView.setVisibility(View.VISIBLE);
        myView.setBackgroundColor(Color.WHITE);
        endBreak.setVisibility(View.VISIBLE);
        breakText.setVisibility(View.VISIBLE);
        hitPercentage = (((double) score)/((double) successful_trailsPerBlockCounter))*100;
        String txt = "Your hit-percentage in the last block: " + String.valueOf((int) hitPercentage);

        int numberOfBlocks = (maxTrailsPerGroup/maxTrailsPerBlock)*NUMBER_OF_GROUPS;
        breakText.setText(txt + "% \n \nTake a BREAK! \n" + totalBlocksDone + " blocks out of " + numberOfBlocks + " blocks finished.");

        breakText.setTextColor(Color.WHITE);
        // breakView.setBackgroundColor(SUCCESS_COLOR_START); //Yellow
        myView.setBackgroundColor(Color.MAGENTA);
        endBreak.setX(0);
        endBreak.setY(0);
        endBreak.setText("");


        targetCircle.setVisibility(View.INVISIBLE);
        targetBar.setVisibility(View.INVISIBLE);
        startBar.setVisibility(View.INVISIBLE);

    }

    public void setBlock_BreakView(){
        endOfBlock = true;
        blockVectors.remove(blockVectors.get(blockIndex)); //todo doesn't work, how?!

        //breakView.setVisibility(View.VISIBLE);
        myView.setBackgroundColor(Color.WHITE);
        endBreak.setVisibility(View.VISIBLE);
        breakText.setVisibility(View.VISIBLE);
        hitPercentage = (((double) score)/((double) successful_trailsPerBlockCounter))*100;
        String txt = "Your hit-percentage in the last block: " + String.valueOf((int) hitPercentage);

        int numberOfBlocks = (maxTrailsPerGroup/maxTrailsPerBlock)*NUMBER_OF_GROUPS;
        if(totalBlocksDone > 1){
            breakText.setText(txt + "% \n \n" + totalBlocksDone + " blocks out of " + numberOfBlocks + " blocks finished.");
        }else {
            breakText.setText(txt + "% \n \n" + totalBlocksDone + " block out of " + numberOfBlocks + " blocks finished.");
        }
        breakText.setTextColor(Color.BLACK);

        endBreak.setX(defaultBtnPositionX - (endBreak.getWidth()/2));
        endBreak.setY(defaultBtnPositionY);
        endBreak.setText("Continue");
       // breakView.setBackgroundColor(DEFAULT_COLOR_START);


        targetCircle.setVisibility(View.INVISIBLE);
        targetBar.setVisibility(View.INVISIBLE);
        startBar.setVisibility(View.INVISIBLE);

    }

    public void endBreak(){
        makeTask();
        scoreView.setText(score.toString() + "/" + successful_trailsPerBlockCounter.toString());
       // breakView.setVisibility(View.INVISIBLE);
        myView.setBackgroundColor(Color.BLACK);
        endBreak.setVisibility(View.INVISIBLE);
        breakText.setVisibility(View.INVISIBLE);
    }

    public void getPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CONSTANT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CONSTANT:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {
                        dataStream = new FileOutputStream(file, true);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    getPermission();
                }
                break;
        }
    }



    private void makeTask(){
        //NEW GROUP (A x W x direction)
        if(endOfGroup){
            blockVectors = makeBlockVectors();
            groupCounter++;
            successful_trailsPerGroupCounter = 0;
            endOfGroup = false;
        }

        //NEW BLOCK (A x W)
       if(endOfBlock){
           System.out.println("---------------- Block end --------------------");
           //get random vector form blockVector
           int max = blockVectors.size(); //no -1 because exclusive
           blockIndex = ThreadLocalRandom.current().nextInt(max);
           blockVector = blockVectors.get(blockIndex); //todo bug
           //todo problem: never remove blockVector from blockVectors !! -> right/left not equal
           endOfBlock = false;
           blockCounter++;
           totalBlocksDone++;
           successful_trailsPerBlockCounter = 0;
           score = 0;
       }

        Random r = new Random();
        trailIndex = r.nextInt(blockVector.size());
        System.out.println("+++++++++++++++++++++++++++++++++  Size : " + blockVector.size() + "  +++++++++++++++++++++++++++");
        System.out.println("********   Vector: " + ((float[])blockVector.elementAt(trailIndex))[0] + ", " + ((float[])blockVector.elementAt(trailIndex))[1] + ", " + ((float[])blockVector.elementAt(trailIndex))[2]);

        int myA = (int)((float[])blockVector.elementAt(trailIndex))[0];

        int randomInt=0;
        boolean notDuplicate=false;

        while(notDuplicate==false) {

            if(randomChosen.size()==W.length){
                randomChosen.clear();
            }

            int len = WH.length;
            Random random = new Random();
            randomInt = random.nextInt(len);

            if(containtsDup(randomInt)){
                notDuplicate=false;
            }else{notDuplicate=true;}

            randomChosen.add(randomInt);
        }

        float myW = WH[randomInt][0];
        float myH = WH[randomInt][1];

        String myStartPosition;
        if(((float[])blockVector.elementAt(trailIndex))[2] == 0){
            myStartPosition = "top";
        }else{
            myStartPosition = "bottom";
        }

        //save A & W options
        data[INDEX_OF_A] = String.valueOf(myA);
        log[7] = String.valueOf(myA);

        //to get , for float not .
        String myW_String = String.valueOf(myW);
        data[INDEX_OF_W] = myW_String.replace('.', ',');
        currentW = myW;
        log[8] = myW_String.replace('.', ',');


        setButtonParameters(myA, myW,myH, myStartPosition);

        started = false;

    }

    private static LinkedList<Integer> randomChosen=new LinkedList();

    private boolean containtsDup(int randomInt) {
        boolean retVal=false;
        for(int i:randomChosen){
            if(randomInt==i){retVal= true;}
        }

        return retVal;
    }

    private List<Vector> makeBlockVectors(){

        List<List<float[]>> listOptionlistforAWCombi = new ArrayList<>();
        for (int distance : A) {
           for(int i=0;i<W.length;i++){
                List<float[]> optionsforAWCombi = new ArrayList<>();
                //to make direction equal
                for (int k = 0; k < 2; k++) {
                    float[] combi = {distance, W[i],H[i], k};
                    optionsforAWCombi.add(combi);
                }
                listOptionlistforAWCombi.add(optionsforAWCombi);
            }
        }

        List<Vector> blockCombinationVectors = new ArrayList<>();

        int blocksPerGroup = maxTrailsPerGroup/maxTrailsPerBlock;
        for (int x = 0; x < blocksPerGroup; x++) {
            Vector blockCombinationVector = new Vector();
            //in each block i want one option combi of each list (optionsForAW) of listOptions...
            for (int j = 0; j < listOptionlistforAWCombi.size(); j++) {
                List<float[]> AW_Combi = listOptionlistforAWCombi.get(j);
                //random index
                int max = AW_Combi.size(); //no -1 because exclusive
                int i = ThreadLocalRandom.current().nextInt(max);

                // add chosen to vector
                blockCombinationVector.add(AW_Combi.get(i));

                //remove chosen from options
                AW_Combi.remove(i);
            }
            blockCombinationVectors.add(blockCombinationVector);
        }
        return blockCombinationVectors;
    }



    private void setButtonParameters(int a, float w,float h,String startP){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int widthWindow = displayMetrics.widthPixels;
        int heightWindow = displayMetrics.heightPixels;

        // (convert_From, convert_Value, convert_into) here: mm -> dip / pixel
        float bar_height_pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, START_BAR_WIDE,
                getResources().getDisplayMetrics());

        float padding_pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, PADDING,
                getResources().getDisplayMetrics());

        float target_width_pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, w,
                getResources().getDisplayMetrics());
        data[INDEX_OF_W_px]= String.valueOf((int)target_width_pixel);
        log[10] = String.valueOf((int)target_width_pixel);

        float target_height_pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, h,getResources().getDisplayMetrics());

        float distance_pixel =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, a,
                getResources().getDisplayMetrics());
        data[INDEX_OF_A_px]= String.valueOf((int)distance_pixel);
        log[9] = String.valueOf((int)distance_pixel);

        //PROPERTIES UNRELATED TO MODE
        startBar.setVisibility(View.VISIBLE);
        startBar.setBackgroundColor(DEFAULT_COLOR_START);
        data[INDEX_OF_BtnWidth] = "full_width";


        //PORTRAIT MODE

        float startPositionY;
        float targetPositionY;
        float distanceTop;
        float usableScreenHeight = heightWindow - (2 * padding_pixel) - (target_width_pixel / 2) - distance_pixel - (bar_height_pixel / 2); // - padding_pixel

        //Start -> Target
        if(startP.equals("top")) {
            distanceTop = padding_pixel;
            Random r = new Random();
            startPositionY = r.nextInt((int) usableScreenHeight) + distanceTop;
            targetPositionY = ((startPositionY + (bar_height_pixel / 2)) + distance_pixel) - (target_width_pixel / 2);
            data[INDEX_OF_BtnOrder] = "down";

        //Target <- Start
        }else{
            distanceTop = padding_pixel + (target_width_pixel / 2) + distance_pixel - (bar_height_pixel / 2);
            Random r = new Random();
            startPositionY = r.nextInt((int) usableScreenHeight) + distanceTop;
            targetPositionY = (startPositionY + (bar_height_pixel/2)) - distance_pixel - (target_width_pixel/2) ;
            data[INDEX_OF_BtnOrder] = "up";
        }

        //START BAR
        startBar.setX(0);
        startBar.setY(startPositionY);
        startBar.setWidth(widthWindow);
        startBar.setHeight((int) bar_height_pixel);

        //TARGET BUTTON
        // 1D - Bar
        if(oneD) {
            targetBar.setVisibility(View.VISIBLE);
            targetBar.setBackgroundColor(DEFAULT_COLOR_TARGET);

            targetBar.setX(0);
            targetBar.setY(targetPositionY);
            targetBar.setWidth(widthWindow);
            targetBar.setHeight((int) target_width_pixel);

            target_center_x = widthWindow / 2;
            target_center_y = targetPositionY + (target_width_pixel / 2);

        // 2D - Circle
        }else{
            targetCircle.setVisibility(View.VISIBLE);
            float thumbWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, THUMB_WIDTH,
                    getResources().getDisplayMetrics());
            int boarder_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, BOARDER,
                    getResources().getDisplayMetrics());
            float targetXPosition_Circle;

            //Calculate X position of circle
            Random r = new Random();

            targetXPosition_Circle = r.nextInt(widthWindow - 2 * boarder_px
                    - Math.max((int) target_width_pixel, (int) thumbWidth))
                    + boarder_px + (Math.max(0, ((int) thumbWidth-(int) target_width_pixel)/2));


           targetCircle.setBackgroundColor(DEFAULT_COLOR_TARGET);
           targetCircle.setWidth((int) target_width_pixel);
           targetCircle.setHeight((int) target_width_pixel);

            targetCircle.setWidth((int) target_width_pixel);
            targetCircle.setHeight((int) target_height_pixel);
            targetCircle.setX(targetXPosition_Circle);
            targetCircle.setY(targetPositionY);

            target_center_x = targetXPosition_Circle + (target_width_pixel / 2);
            target_center_y = targetPositionY + (target_width_pixel / 2);

            //ADAPT START IF WIDTH == thumb
            if(startWidth.equals("thumb")) {
                //new Width
                startBar.setWidth((int) thumbWidth);

                // new X
                float centroidCircle = targetXPosition_Circle + (target_width_pixel / 2);
                float newX = centroidCircle - (thumbWidth / 2);
                startBar.setX(newX);

                data[INDEX_OF_BtnWidth] = "thumb_width";
            }

        }

    }

    private void setAvgAxisVals(){
        float avgAxis_L = 0;
        float avgAxis_S = 0;
        //StringBuilder vals = new StringBuilder();
        for(float[] val: axisValues){
            avgAxis_L = avgAxis_L+val[0];
            avgAxis_S = avgAxis_S+val[1];
            //vals.append(String.valueOf(val));
            //vals.append(", ");
        }
        avgAxis_L = avgAxis_L/axisValues.size();
        avgAxis_S = avgAxis_S/axisValues.size();

        data[INDEX_OF_AXIS_AVG_L] = String.valueOf(avgAxis_L).replace('.', ',');
        data[INDEX_OF_AXIS_AVG_S] = String.valueOf(avgAxis_S).replace('.', ',');
    }


    private void makeLogEntry(MotionEvent event){
        axisCounter++;

        log[3] = String.valueOf(trailCounter);
        log[4] = String.valueOf(axisCounter);
        log[5] = String.valueOf(event.getTouchMajor()).replace('.', ',');
        log[6] = String.valueOf(event.getTouchMinor()).replace('.', ',');

        writeLog(log);

    }

    private void writeLog(String[] log){
        try {
            String text = "";

            //make txt from array
            for(int i=0; i<log.length; i++){
                text = text + log[i] + ";";
            }
            text = text + " \n";

            logStream.write(text.getBytes());

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeData(String[] data){
        try {
            String text = "";

            //make txt from array
            for(int i=0; i<data.length; i++){
                text = text + data[i] + ";";
            }
            text = text + " \n";

            dataStream.write(text.getBytes());

        }catch (IOException e) {
            e.printStackTrace();
        }

    }



}
