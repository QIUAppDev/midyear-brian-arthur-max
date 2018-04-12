package com.example.brian.subwaytime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.UUID;

public class StartActivity extends AppCompatActivity {

    Button activityScan;
    Button mainActivity;
    Button firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        activityScan = findViewById(R.id.button2);
        mainActivity = findViewById(R.id.button3);
        firebase = findViewById(R.id.button7);


        //portal to easily switch between apps
        //main activity to be set here

        activityScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityScan.class);
                startActivity(intent);
                //add portal to ActivityScan
                //portal to wifi scan
            }
        });

        mainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                //portal to go to display db
            }
        });

        firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FirebaseTest.class);
                startActivity(intent);

            }
        });

        //TODO: generate CONSTANT user id (UUID) to be persistently stored
        //TODO: add timestamp to each magnetic data option
        //TODO: link magnetism with wifi tracker in some way (perhaps make the switch continuous)
        //TODO: this means the the button shouldn't be the WIFI scanner, but should be MAGNETISM tracker
        String test_id = UUID.randomUUID().toString();
        Log.d("test_id",test_id);
    }



}
