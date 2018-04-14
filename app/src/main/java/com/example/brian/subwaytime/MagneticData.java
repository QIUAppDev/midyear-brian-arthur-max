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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;

public class MagneticData extends AppCompatActivity implements SensorEventListener {

    //magnetic data (and corresponding) timestamps that get posted by user ID to firebase
    //pushes to firebase every 10 seconds (i.e. when the unix timestamp is divisible by 10)
    public List<float[]> TODOBRIANFIXTHIS = new ArrayList(); //legacy holder
    public List<Long> timestamps = new ArrayList();
    public HashMap<String,List<Float>> data_meshed = new HashMap<>(); //format that is pushed to Firebase

    //magnetic stuff
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private String TAG = "MagneticData";

    //FIREBASE stuff
    //a different point of reference will be accessed than FirebaseTest to post users
    private FirebaseDatabase database;
    private DatabaseReference myRef;


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

                Log.d(TAG,"onClick timestampes: " + timestamps.get(0)); //timestamp stuff
                Log.d(TAG,"onClick timestampes"+Arrays.deepToString(timestamps.toArray()));

                Snackbar.make(view, "Started magnetic stuff", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });

        //init the firebase
        //getReference changes the "database mode" such that the "users" tree is edited, not the wifi networks
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
    }
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSensorChanged(SensorEvent event){

        //Log.d(TAG, "onSensorChanged: 0="+event.values[0]+" 1="+event.values[1]+" 2="+event.values[2]);
        //OLD SYSTEM
        float[] temp = {event.values[0],event.values[1],event.values[2]}; //kept to keep button working, will be removed later
        TODOBRIANFIXTHIS.add(temp);// lord knows why I can't do this inline
        timestamps.add(System.currentTimeMillis()/1000); //kept to ensure 10 second buffer works


        //NEW SYSTEM: assemble raw magnetic values into List, then append List into HashMap (paired with corresponding timestamp) for pushing to Firebase
        List<Float> temp_list = new ArrayList(); //temp except as a List (for pushing to firebase)
        temp_list.add(event.values[0]);
        temp_list.add(event.values[1]);
        temp_list.add(event.values[2]);

        data_meshed.put(timestamps.get(timestamps.size()-1).toString(),temp_list); //note: timestamp is converted to string to comply with firebase standards



        //checks if a) timestamps size is at least 2, b)if an actual change of timestamp has occured, c) if it's 10 seconds
        //TODO: integrate wifi and magnetism, such that 10 second buffer itself triggers wifi scan, and prompts users to select station
        //TODO: 1) move old activites to separate folder and ensure functinoality works. 2) create new activity and ensure magnetism buffer works/magnetism db works. 3) integrate wifi and ensure wifi
        //todo: networks detected. 4) pull up prompt and disable 10 second buffer so long as prompt is put up 5) handle database pushes
        if(timestamps.size()>1){
            if(!timestamps.get(timestamps.size()-1).equals(timestamps.get(timestamps.size()-2)) && timestamps.get(timestamps.size()-1)%10==0){
                String phone_id = PersistentID.get_id();
                myRef.child(phone_id).child("phone_id").setValue(phone_id);
                myRef.child(phone_id).child("all_data").setValue(data_meshed);
                Log.d("push_to_firebase","10 seconds");
            }
        }

        //Log.d("changed sensor","yes");
    }
    public void onAccuracyChanged(Sensor event, int accuracy){
        Log.d(TAG, "onAccuracyChanged: "+accuracy);

    }


}