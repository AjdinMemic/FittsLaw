package com.example.fittslaw_tablet2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InbetweenActivity extends AppCompatActivity {

    private Button actionBtn;
    private TextView textView;

    private String caller;
    private String id;
    private String m;
    private String e;
    private String b;
    private String w;
    private String f;
    private String text1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);


        actionBtn = findViewById(R.id.nextBtn);
        textView = findViewById(R.id.textView);

        Intent i = getIntent();
       /* actionBtn.setText(i.getStringExtra("btn"));
        textView.setText(i.getStringExtra("txt"));*/

        caller = i.getStringExtra("caller");
        id = i.getStringExtra("id");
        b = i.getStringExtra("blocks");
        m = i.getStringExtra("mode");
        e = i.getStringExtra("experiment");
        w = i.getStringExtra("btn_width");
        f = i.getStringExtra("ignoreFinger");

        text1 = "";
        if( caller.equals("Input") && e.contains("Calibration")){
            text1 = "Next up Calibration";

        }else if(caller.equals("Input") && e.contains("Fitts")) {
            text1 = "Next up Fitts' Task";
        }



        if(caller.equals("Input")){
            textView.setText(text1);
            actionBtn.setVisibility(View.VISIBLE);
            actionBtn.setText("Next");

        }else{
            textView.setText("Thanks for your participation !");
            actionBtn.setText("Done");
        }

        actionBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               nextAction();
            }
        });

    }



    private void nextAction(){


        if( caller.equals("Input") && e.contains("Calibration")){
            Intent intent;
            intent = new Intent(getApplicationContext(), CalibrationActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("mode", m);
            intent.putExtra("ignoreFinger", f);
            intent.putExtra("experiment", e);
            intent.putExtra("blocks", b);
            startActivity(intent);

        }else if(caller.equals("Input") && e.contains("Fitts")){
            Intent intent;
            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("mode", m);
            intent.putExtra("ignoreFinger", f);
            intent.putExtra("experiment", e);
            intent.putExtra("blocks", b);
            intent.putExtra("btn_width", w);
            startActivity(intent);

        }

        finish();



    }


}
