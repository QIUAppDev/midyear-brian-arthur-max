package com.example.brian.subwaytime;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;

public class MagneticData extends AppCompatActivity implements SensorEventListener {

    public ArrayList<float[]> TODOBRIANFIXTHIS = new ArrayList<>();
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private String TAG = "MagneticData";

//    https://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-accel
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //just a little something spliced into it for testing. Probably can stay
        int REQUEST_CODE = 1;
        if(!(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)){
            //if perms aren't granted, we ask
            ActivityCompat.requestPermissions(MagneticData.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE); //TODO whats a request code?

        }
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            Log.d("permissions","permissions granted!");
        }
        else{
            Log.d("permissions","denied, something sent wrong");
        }

        //initialize the references
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);



        //check if the user is actually running magnetometer
        String out = mSensorManager.getSensorList(TYPE_MAGNETIC_FIELD).toString();
        Log.d(TAG, "onCreate: type of magnetometer if at all:"+out);
        if(out != ""){
            //user has no magnetometer. This is an issue. TODO
        }




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_get);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+TODOBRIANFIXTHIS.get(0)[0]);
                Log.d(TAG, "onClick: "+ Arrays.deepToString(TODOBRIANFIXTHIS.toArray()));
                Snackbar.make(view, "Started magnetic stuff", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });
    }
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSensorChanged(SensorEvent event){
        //Log.d(TAG, "onSensorChanged: 0="+event.values[0]+" 1="+event.values[1]+" 2="+event.values[2]);
        float[] temp = {event.values[0],event.values[1],event.values[2]};
        TODOBRIANFIXTHIS.add(temp);// lord knows why I can't do this inline
    }
    public void onAccuracyChanged(Sensor event, int accuracy){
        Log.d(TAG, "onAccuracyChanged: "+accuracy);

    }


}
