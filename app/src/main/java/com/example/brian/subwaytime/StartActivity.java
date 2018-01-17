package com.example.brian.subwaytime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    Button activityScan;
    Button mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        activityScan = findViewById(R.id.button2);
        mainActivity = findViewById(R.id.button3);


        //portal to easily switch between apps
        //main activity to be set here

        activityScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add portal to ActivityScan
            }
        });

        mainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add portal to MainActivity
            }
        });
    }



}
