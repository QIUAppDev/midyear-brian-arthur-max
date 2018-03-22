package com.example.brian.subwaytime;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;

public class dataGetActivity extends AppCompatActivity {

    public ArrayList<String> TODOBRIANFIXTHIS;
    private SensorManager mSensorManager;
    private String TAG = "dataGetActivity";

//    https://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-accel
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //check if the user is actually running magnetometer
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        String out = mSensorManager.getSensorList(TYPE_MAGNETIC_FIELD).toString();
        Log.d(TAG, "onCreate: type of magnetometer if at all:"+out);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_get);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Started magnetic stuff", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });
    }

}
