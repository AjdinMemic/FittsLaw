package com.example.fittslaw_tablet2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class InputActivity extends AppCompatActivity {

    private static final String[] EXPERIMENT = {"choose Experiment", "2D_Calibration", "2D_Fitts"};
    //{"choose Experiment", "1D_Calibration", "2D_Calibration", "1D_Fitts", "2D_Fitts"};
    private static final String[] DEVICE_MODE = {"choose Finger size", "own width", "25mm", "30mm", "35mm"};


    private Spinner spinE;
    private Spinner spinM;
    private TextView idView;
    private TextView blockView;
   // private CheckBox btnFullwidth;
  //  private CheckBox btnIgnoreFinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        idView = findViewById(R.id.number);
        idView.setText("0");
        blockView = findViewById(R.id.blockNumber);
        //btnFullwidth = findViewById(R.id.btnWidth);
        //btnFullwidth.setVisibility(View.INVISIBLE);
        //btnIgnoreFinger = findViewById(R.id.ignoreFingerSize);

        spinE = findViewById(R.id.experiment);
        ArrayAdapter<String> adapterE = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, EXPERIMENT);
        adapterE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinE.setAdapter(adapterE);

        spinM = findViewById(R.id.modus);
        ArrayAdapter<String> adapterM = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DEVICE_MODE);
        adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinM.setAdapter(adapterM);


        spinE.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinE.getSelectedItem().toString().equals("2D_Fitts")){
                    //btnFullwidth.setVisibility(View.VISIBLE);
                    blockView.setText("3"); //3 = 6 reps (because each block has 2 reps (up / down)
                }else{
                    //btnFullwidth.setVisibility(View.INVISIBLE);
                    blockView.setText("2");
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                //btnFullwidth.setVisibility(View.INVISIBLE);
                blockView.setText("");
            }
        });

        final Button next= findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

    }

    private void next(){
        //NEXT
        String id = idView.getText().toString();
        String e = spinE.getSelectedItem().toString();
        String b = blockView.getText().toString();
        String m = spinM.getSelectedItem().toString();


        String width = "thumb";
        /*if(btnFullwidth.isChecked()){
            width = "full";
        }else{
            width = "thumb";
        }*/

        final Intent i = new Intent(InputActivity.this, InbetweenActivity.class);

        i.putExtra("id", id);
        i.putExtra("experiment", e);
        i.putExtra("blocks", b);
        if(m.equals("own width")){
            i.putExtra("mode", "20mm");
        }else {
            i.putExtra("mode", m);
        }
        i.putExtra("btn", "Next");
        i.putExtra("txt", "Next up Calibration");
        i.putExtra("caller", "Input");
        i.putExtra("ignoreFinger", "true"); //so we use target size from ex2
       /* if(btnIgnoreFinger.isChecked()){
            i.putExtra("ignoreFinger", "true");
        }else{
            i.putExtra("ignoreFinger", "false");
        } */
        i.putExtra("btn_width", width);


        if(id.equals("") || b.equals("") || e.contains("choose") || m.contains("choose") ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Not everything is filled out correct.");
            builder.setCancelable(false);

            builder.setPositiveButton(
                    "Okay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }else if(id.equals("0")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure this is a Test?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(i);
                            dialog.cancel();
                            finish();

                        }
                    });

            builder.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();

        }else {
            startActivity(i);
            finish();
        }

    }

}
