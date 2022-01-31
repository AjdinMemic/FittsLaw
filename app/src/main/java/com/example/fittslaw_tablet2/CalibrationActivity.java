package com.example.fittslaw_tablet2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class CalibrationActivity extends AppCompatActivity {

    private static final String FILE_NAME_1D = "1D_Calibration#3_";
    private static final String FILE_NAME_2D = "2D_Calibration#3_";

    private static String[] HEADER_Log = {"ID", "Finger size", "Experiment", "Trial", "Counter","Axis_Major", "Axis_Minor", "W_mm", "W_px"};

    private static String[] HEADER = {"ID", "Experiment", "Finger size", "Trial", "Block", "TrialInBlock", "Circle_Position", "W_mm", "W_px",
            "X-Finger-Down", "Y-Finger-Down", "X-Finger-Up", "Y-Finger-Up", "X-Target", "Y-Target",  "X-Target-Center", "Y-Target-Center",
            "X-Distance-Down", "Y-Distance-Down", "X-Distance-Up", "Y-Distance-Up", "Hit", "Outlier", "Outlier_px_center", "Outlier_mm_center", "Outlier_px_boarder", "Outlier_mm_boarder",
            "Axis_Major_Down",  "Axis_Minor_Down",  "Axis_Major_Up",  "Axis_Minor_Up", "Avg_Axis_Major", "Avg_Axis_Minor"};
    private static int INDEX_OF_ID = 0;
    private static int INDEX_OF_Experiment = 1;
    private static int INDEX_OF_Mode = 2;


    private static int INDEX_OF_Trail = 3;
    private static int INDEX_OF_Block_Number = 4;
    private static int INDEX_OF_Trail_In_Block = 5;

    private static int INDEX_OF_CirclePosition = 6 ;
    private static int INDEX_OF_W_mm = 7;
    private static int INDEX_OF_W_px = 8;


    //finger at Target
    private static int INDEX_OF_X_Down = 9;
    private static int INDEX_OF_Y_Down = 10;
    private static int INDEX_OF_X_Up = 11;
    private static int INDEX_OF_Y_Up = 12;

    private static int INDEX_OF_Target_X = 13;
    private static int INDEX_OF_Target_Y = 14;

    private static int INDEX_OF_TARGET_CENTER_X = 15;
    private static int INDEX_OF_TARGET_CENTER_Y = 16;
    private static int INDEX_OF_DISTANCE_X_DOWN = 17;
    private static int INDEX_OF_DISTANCE_Y_DOWN = 18;
    private static int INDEX_OF_DISTANCE_X_UP = 19;
    private static int INDEX_OF_DISTANCE_Y_UP = 20;

    private static int INDEX_OF_Hit = 21;

    private static int INDEX_OF_OUTLIER = 22;
    private static int INDEX_OF_OUTLIER_PX_center = 23;
    private static int INDEX_OF_OUTLIER_MM_center = 24;
    private static int INDEX_OF_OUTLIER_PX_boarder = 25;
    private static int INDEX_OF_OUTLIER_MM_boarder = 26;

    private static int INDEX_OF_AXIS_L_Down = 27;
    private static int INDEX_OF_AXIS_S_Down = 28;
    private static int INDEX_OF_AXIS_L_Up = 29;
    private static int INDEX_OF_AXIS_S_Up = 30;
    private static int INDEX_OF_AXIS_AVG_L = 31;
    private static int INDEX_OF_AXIS_AVG_S = 32;



    private static float BAR_WIDE = 2.4f;

    //todo EDIT here
    private static final int NUMBER_OF_TASKS_PER_BLOCK = 8;
    private int NUMBER_OF_BLOCKS = 2; //default 2
    //Break time in between blocks
    private static final long START_TIME_IN_MILLIS = 1000;

    private static final int PADDING = 5; //mm
    private static final int OUTLIER_DISTANCE_Boarder = 15; //in mm
    private float OUTLIER_DISTANCE_PX_Boarder;

    //Colors
    private static final int DEFAULT_COLOR_TARGET = Color.parseColor("#FFFF00"); //YELLOW #FFFF00
    private static final int SUCCESS_COLOR_TARGET = Color.parseColor("#00CC00"); //GREEN #00CC00
    private static final int FAIL_COLOR_TARGET = Color.parseColor("#FF0000"); //RED #FF0000

    //just random number
    private static final int PERMISSION_CONSTANT = 2;

    //---------------------------------------------------------------------------------------------

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
  //  private TextView countDownTextView;

    private Button targetBar;
    private Button targetCircle;

    private TextView scoreView;
    private Integer score;
    private int trailCounter;
    private int counterPerBlock;
    private int blockCounter;

    private float target_center_x;
    private float target_center_y;
    private Boolean outlier_down;
    private int randomIndex;

    private View myView;
    private ScrollView breakView;
    private Button  endBreak;
    private TextView breakText;

    private List<float[]> axisValues;
    private int axisCounter;

    private String[] data;
    private String[] log;

    private String id;
    private String b;
    private String m;
    private String f;
    private String e;
    private Boolean oneD;
    private Boolean endOfBlock;
    private Vector blockVector;

    private MediaPlayer mp;
    private FileOutputStream stream;
    private FileOutputStream logStream;
    private File file;
    //private Button circle; //for debug
    //private ConstraintLayout layout;
    @Override
    public void onBackPressed() {
        //do nothing
    }


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();

        id = i.getStringExtra("id");
        b = i.getStringExtra("blocks");
        m = i.getStringExtra("mode");
        e = i.getStringExtra("experiment");
        f = i.getStringExtra("ignoreFinger");

        switch (m){
            case "20mm":
                BAR_WIDE = 3.00f;
                break;
            case "25mm":
                BAR_WIDE = 3.75f;
                break;
            case "30mm":
                BAR_WIDE = 4.50f;
                break;
            case "35mm":
                BAR_WIDE = 5.26f;
                break;
            default:
                System.out.println("Error finger size.");
        }

        //If we ignore finger size all the targets will be same as for finger W20
        if(f.equals("true")){
            BAR_WIDE = 2.4f;
        }

        NUMBER_OF_BLOCKS = Integer.valueOf(b);
        OUTLIER_DISTANCE_PX_Boarder = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, OUTLIER_DISTANCE_Boarder,
                getResources().getDisplayMetrics());

        if(e.contains("1D")){
            oneD=true;
        }else{
            oneD=false;
        }

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
            stream = new FileOutputStream(file, true);
        }catch (IOException e) {
            e.printStackTrace();
        }
        if(isNew){
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

        trailCounter = 0;
        blockCounter = 1;
        counterPerBlock = 0;
        score = 0;
        endOfBlock = true;
        outlier_down = false;

        myView = findViewById(R.id.viewC);
        myView.setBackgroundColor(Color.parseColor("#000000")); //black
        scoreView = findViewById(R.id.scoreCal);
        scoreView.setText(score.toString() + "/" + String.valueOf(counterPerBlock));


        breakView = findViewById(R.id.breakView);
        breakView.setVisibility(View.INVISIBLE);

        endBreak = findViewById(R.id.endBreak);
        endBreak.setVisibility(View.INVISIBLE);

        breakText = findViewById(R.id.breakTxt);
        breakText.setVisibility(View.INVISIBLE);


        targetBar = findViewById(R.id.target_0);
        targetCircle = findViewById(R.id.round_Target);


        if(oneD){
            targetCircle.setVisibility(View.INVISIBLE);
        }else{
            targetBar.setVisibility(View.INVISIBLE);
        }

        mp = MediaPlayer.create(CalibrationActivity.this, R.raw.success);

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

        //Width stays same for all the trails in Calibration
        String myW_String = String.valueOf(BAR_WIDE);
        data[INDEX_OF_W_mm] = myW_String.replace('.', ',');
        log[7] =  myW_String.replace('.', ',');
        float target_width_pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, BAR_WIDE,
                getResources().getDisplayMetrics());
        data[INDEX_OF_W_px]= String.valueOf((int)target_width_pixel);
        log[8] = String.valueOf((int)target_width_pixel);


        displayTarget();


        endBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                myView.setBackgroundColor(Color.BLACK);
                scoreView.setVisibility(View.VISIBLE);
                endBreak.setVisibility(View.INVISIBLE);
                breakText.setVisibility(View.INVISIBLE);
            }
        });

        //Hit Somewhere "wrong"
        myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchAction(event, false);
                return true;
            }
        });

        targetCircle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchAction(event, true);
                return true;
            }
        });

        targetBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchAction(event, true);
                return true;
            }
        });
    }

    private void touchAction(MotionEvent event, Boolean hit){
        if (event.getAction() == MotionEvent.ACTION_DOWN && !mTimerRunning) {
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

            // timeNeeded = System.currentTimeMillis() - startTime;
            if(hit){
                //MUSIC
                if (mp.isPlaying()) {
                    mp.stop();
                }
                mp = MediaPlayer.create(CalibrationActivity.this, R.raw.success);
                mp.start();
                //COLOR
                if(oneD) {
                    targetBar.setBackgroundColor(SUCCESS_COLOR_TARGET);
                }else{
                    GradientDrawable circle = (GradientDrawable) targetCircle.getBackground().getCurrent();
                    circle.setColor(SUCCESS_COLOR_TARGET);
                }
                //DATA
                score++;
                data[INDEX_OF_Hit] = "1";


            }else {
                //MUSIC
                if (mp.isPlaying()) {
                    mp.stop();
                }
                mp = MediaPlayer.create(CalibrationActivity.this, R.raw.error);
                mp.start();
                //COLOR
                if(oneD) {
                    targetBar.setBackgroundColor(FAIL_COLOR_TARGET);
                }else{
                    GradientDrawable circle = (GradientDrawable) targetCircle.getBackground().getCurrent();
                    circle.setColor(FAIL_COLOR_TARGET);
                }
                //DATA
                data[INDEX_OF_Hit] = "0";

            }
            scoreView.setText(score.toString() + "/" + String.valueOf(counterPerBlock+1));

            float distance_y;
            float distance_x;
            float OUTLIER_DISTANCE = OUTLIER_DISTANCE_Boarder + BAR_WIDE/2;
            float OUTLIER_DISTANCE_PX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, OUTLIER_DISTANCE,
                    getResources().getDisplayMetrics());

            //If you want the coordinates relative to the top left corner of the device screen, then use the raw values.
            data[INDEX_OF_X_Down] = String.valueOf((int) event.getRawX());
            data[INDEX_OF_Y_Down] = String.valueOf((int) event.getRawY());
            data[INDEX_OF_Trail] = String.valueOf(trailCounter+1);
            data[INDEX_OF_Trail_In_Block] = String.valueOf(counterPerBlock+1);
            data[INDEX_OF_Block_Number] = String.valueOf(blockCounter);
            if(oneD) {
                data[INDEX_OF_Target_X] = String.valueOf((int)targetBar.getX());
                data[INDEX_OF_Target_Y] = String.valueOf((int)targetBar.getY());
                //CALCULATE DISTANCE
                distance_y = target_center_y - event.getRawY();
                data[INDEX_OF_DISTANCE_Y_DOWN] = String.valueOf((int) distance_y);
                if(Math.abs(distance_y) >= OUTLIER_DISTANCE_PX){
                    outlier_down = true;
                }



            }else{
                data[INDEX_OF_Target_X] = String.valueOf((int)targetCircle.getX());
                data[INDEX_OF_Target_Y] = String.valueOf((int)targetCircle.getY());

                //CALCULATE DISTANCE
                distance_y = target_center_y - event.getRawY();
                distance_x = target_center_x - event.getRawX();
                data[INDEX_OF_DISTANCE_Y_DOWN] = String.valueOf((int) distance_y);
                data[INDEX_OF_DISTANCE_X_DOWN] = String.valueOf((int) distance_x);
                if(Math.abs(distance_y) >= OUTLIER_DISTANCE_PX || Math.abs(distance_x) >= OUTLIER_DISTANCE_PX){
                    outlier_down = true;
                }
            }

        } // ------- TOUCH MOVE ----------
        else if(event.getAction() == MotionEvent.ACTION_MOVE){
            //RECORD TOUCH AXIS
            float axis_L = event.getTouchMajor();
            float axis_S = event.getTouchMinor();
            float[] tuple = new float[2];
            tuple[0] = axis_L;
            tuple[1] = axis_S;
            axisValues.add(tuple);
            makeLogEntry(event);

        }
        else if(event.getAction() == MotionEvent.ACTION_UP && !mTimerRunning){
            float distance_y;
            float distance_x;
            boolean outlier_up = false;
            float OUTLIER_DISTANCE = OUTLIER_DISTANCE_Boarder + BAR_WIDE/2;
            float OUTLIER_DISTANCE_PX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, OUTLIER_DISTANCE,
                    getResources().getDisplayMetrics());

            float[] tuple = new float[2];
            tuple[0] = event.getTouchMajor();
            tuple[1] = event.getTouchMinor();
            axisValues.add(tuple);
            makeLogEntry(event);
            data[INDEX_OF_AXIS_L_Up] = String.valueOf(event.getTouchMajor()).replace('.', ',');
            data[INDEX_OF_AXIS_S_Up] = String.valueOf(event.getTouchMinor()).replace('.', ',');
            setAvgAxisVals();

            if(oneD) {
                //CALCULATE DISTANCE
                data[INDEX_OF_Target_X] = String.valueOf((int) targetBar.getX());
                data[INDEX_OF_Target_Y] = String.valueOf((int) targetBar.getY());
                //CALCULATE DISTANCE
                distance_y = target_center_y - event.getRawY();
                data[INDEX_OF_DISTANCE_Y_UP] = String.valueOf((int) distance_y);
                if(Math.abs(distance_y) >= OUTLIER_DISTANCE_PX){
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

            data[INDEX_OF_X_Up] = String.valueOf((int) event.getRawX());
            data[INDEX_OF_Y_Up] = String.valueOf((int) event.getRawY());
            data[INDEX_OF_TARGET_CENTER_X] = String.valueOf((int) target_center_x);
            data[INDEX_OF_TARGET_CENTER_Y] = String.valueOf((int) target_center_y);
            data[INDEX_OF_OUTLIER_MM_center] = String.valueOf((int) OUTLIER_DISTANCE);
            data[INDEX_OF_OUTLIER_PX_center] = String.valueOf((int) OUTLIER_DISTANCE_PX);
            data[INDEX_OF_OUTLIER_MM_boarder] = String.valueOf((int) OUTLIER_DISTANCE_Boarder);
            data[INDEX_OF_OUTLIER_PX_boarder] = String.valueOf((int) OUTLIER_DISTANCE_PX_Boarder);


            //CHECK FOR OUTLIER
            if(outlier_up || outlier_down) {
                data[INDEX_OF_OUTLIER] = String.valueOf((int) 1); //1 = true
                writeData(data);
                trailCounter++;
                outlier_down = false;

                startTimer();

            }else {
                data[INDEX_OF_OUTLIER] = String.valueOf((int) 0); //0 = false
                writeData(data);
                trailCounter++;
                counterPerBlock++;
                if(!oneD) {
                    //this makes sure that the sides are equally split
                    blockVector.remove(randomIndex);
                }

                if (blockCounter == NUMBER_OF_BLOCKS && counterPerBlock == NUMBER_OF_TASKS_PER_BLOCK) {
                    //NEXT
                    Intent intent = new Intent(getApplicationContext(), InbetweenActivity.class);

                    intent.putExtra("id", id);
                    intent.putExtra("blocks", b);
                    intent.putExtra("mode", m);
                    intent.putExtra("ignoreFinger", f);
                       /* intent.putExtra("btn", "Next");
                        intent.putExtra("txt", "Next up Main Task");*/
                    intent.putExtra("caller", "Calibration");
                    intent.putExtra("experiment", e);

                    startActivity(intent);

                    finish();
                } else {
                    if (counterPerBlock == NUMBER_OF_TASKS_PER_BLOCK) {
                        pause();
                    } else {
                        startTimer();
                    }

                }
            }
        }
    }

    private void pause(){
        myView.setBackgroundColor(Color.WHITE);
        targetBar.setVisibility(View.INVISIBLE);
        targetCircle.setVisibility(View.INVISIBLE);
        scoreView.setVisibility(View.INVISIBLE);
        endBreak.setVisibility(View.VISIBLE);
        breakText.setVisibility(View.VISIBLE);

        double percentage = (((double) score)/((double) counterPerBlock))*100;
        breakText.setText("Your hit-percentage of the last block: " + String.valueOf((int) percentage) + "% \n \n" + blockCounter + " Blocks out of " + NUMBER_OF_BLOCKS + " Blocks finished.");

        //new Block
        endOfBlock = true;
        counterPerBlock = 0;
        score = 0;
        blockCounter++;
        scoreView.setText(score.toString() + "/" + String.valueOf(counterPerBlock));
    }


    private void displayTarget(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int widthWindow = displayMetrics.widthPixels;
        int heightWindow = displayMetrics.heightPixels;

        //calculate pixels from mm
        float bar_height_pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, BAR_WIDE,
                getResources().getDisplayMetrics());

        float padding_pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, PADDING,
                getResources().getDisplayMetrics());

        Random r = new Random();
        float targetPosition;
        targetPosition = r.nextInt(heightWindow - (int)bar_height_pixel - 2*(int)padding_pixel) + padding_pixel;


        if(oneD) {

            targetBar.setVisibility(View.VISIBLE);
            targetBar.setX(0);
            targetBar.setY(targetPosition);
            targetBar.setWidth(widthWindow);
            targetBar.setHeight((int) bar_height_pixel);
            targetBar.setBackgroundColor(DEFAULT_COLOR_TARGET);

            target_center_x = widthWindow / 2;
            target_center_y = targetPosition + (bar_height_pixel / 2);


        }else{
            targetCircle.setVisibility(View.VISIBLE);
            if(endOfBlock){
                blockVector = getNewBlockCobinationVector();
                endOfBlock = false;
            }

            randomIndex = r.nextInt(blockVector.size());
            String side = (String) blockVector.elementAt(randomIndex);

            float targetXPosition_Circle;

            targetXPosition_Circle = r.nextInt(widthWindow / 2 - (int) bar_height_pixel);
            if (side.equals("right")) {
                targetXPosition_Circle = targetXPosition_Circle + widthWindow / 2 - (int) bar_height_pixel;
            }

            data[INDEX_OF_CirclePosition] = side;

            GradientDrawable d = (GradientDrawable) targetCircle.getBackground().getCurrent();
            d.setColor(DEFAULT_COLOR_TARGET);
            d.setSize((int) bar_height_pixel, (int) bar_height_pixel);


            targetCircle.setX(targetXPosition_Circle);
            targetCircle.setY(targetPosition);
            targetCircle.setWidth((int) bar_height_pixel);
            targetCircle.setHeight((int) bar_height_pixel);

            target_center_x = targetXPosition_Circle + (bar_height_pixel / 2);
            target_center_y = targetPosition + (bar_height_pixel / 2);

        }

    }


    private Vector getNewBlockCobinationVector() {
        //Vector with equal right and left
        Vector blockCombinationVector = new Vector();
        for (int x = 0; x < NUMBER_OF_TASKS_PER_BLOCK; x++){
            if(x<NUMBER_OF_TASKS_PER_BLOCK/2) {
                blockCombinationVector.add("left");
            }else{
                blockCombinationVector.add("right");
            }
        }

        return blockCombinationVector;
    }

    private void writeData(String[] data){
        String text = "";

        //make txt from array
        for(int i=0; i<data.length; i++){
            text = text + data[i] + ";";
        }
        text = text + " \n";

        try {
            stream.write(text.getBytes());

        }catch (IOException e) {
            e.printStackTrace();
        }
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
                        stream = new FileOutputStream(file, true);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                }else{
                    getPermission();
                }
                break;
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


    private void startTimer(){
       // countDownTextView.setVisibility(View.VISIBLE);
       // targetBar.setVisibility(View.INVISIBLE);

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                //updateCountDownText();
            }

            @Override
            public void onFinish() {
                displayTarget();
                resetTimer();
            }
        }.start();

        mTimerRunning = true;

    }

    private void resetTimer(){
       // countDownTextView.setVisibility(View.INVISIBLE);
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
       //updateCountDownText();
    }

   /* private void updateCountDownText(){
        //int minutes = (int) (mTimeLeftInMillis/1000)/60;
        int seconds = (int) (mTimeLeftInMillis/1000)%60;
        String timeLeft = String.format(Locale.getDefault(),"%02d",seconds);
        countDownTextView.setText(timeLeft);
    }*/
}
