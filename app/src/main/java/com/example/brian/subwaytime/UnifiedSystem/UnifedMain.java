package com.example.brian.subwaytime.UnifiedSystem;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.brian.subwaytime.MagneticData;
import com.example.brian.subwaytime.PersistentID;
import com.example.brian.subwaytime.R;
import com.example.brian.subwaytime.derpwork;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;

/*
* a class that unifies magnetism and wifi into one activity
* */
public class UnifedMain extends AppCompatActivity implements SensorEventListener {

    //for consistency, all declarations were made in the header
    //Exceptions: the Magnetism Sensors and the Wifi Sensors

    //magnetism data structures
    public List<float[]> TODOBRIANFIXTHIS = new ArrayList(); //maintained for legacy support
    public List<Long> timestamps = new ArrayList();
    public HashMap<String,List<Float>> data_meshed = new HashMap<>(); //format that is pushed to Firebase

    //magnetic sensors
    //NOTE: Android gave me an error when I tried moving its instantiation here, so it remains in onCreate
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private String TAG = "UnifedMain";

    //wifi sensors
    WifiManager mainwifi;

    //Firebase database (only exception to DatabaseStuff)
    private FirebaseDatabase database= FirebaseDatabase.getInstance();
    private DatabaseReference myRef= database.getReference();

    //DatabaseStuff
    DatabaseStuff control = new DatabaseStuff(this);

    //set true if wifi networks have been pulled and are being labeled by the user
    //during this time, no pushes/wifi scans are performed while the user is making choices
    private boolean menuOpen = false;


    //    https://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-accel
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //asks for location/sensor permissions
        int REQUEST_CODE = 1;
        if(!(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)){
            //if perms aren't granted, we ask
            ActivityCompat.requestPermissions(UnifedMain.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE); //TODO whats a request code?

        }
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            Log.d("permissions","permissions granted!");
        }
        else{
            Log.d("permissions","denied, something sent wrong");
        }

        //initialize the Wifi Sensor
        mainwifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //initialize the Magnetism Sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //check if the user is actually running magnetometer
        String out = mSensorManager.getSensorList(TYPE_MAGNETIC_FIELD).toString();
        Log.d(TAG, "onCreate: type of magnetometer if at all:"+out);
        if(out != ""){
            //user has no magnetometer. This is an issue. TODO
        }

        //inits activity layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unifed_main);

        /*

        old code for the bottom toolbar that was displaced for the new UI
        TODOBRIANTHIS is kept so this code remains usable

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
        });*/


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

        //control.printDB();

        //checks if a) timestamps size is at least 2, b)if an actual change of timestamp has occured, c) if it's 10 seconds
        //TODO: integrate wifi and magnetism, such that 10 second buffer itself triggers wifi scan, and prompts users to select station
        //TODO: 1) move old activites to separate folder and ensure functinoality works. [DONE]
        //TODO 2) create new activity and ensure magnetism buffer works/magnetism db works. [DONE]
        // TODO: 3) integrate wifi and ensure wifi networks detected. [DONE]
        // TODO: 4) pull up prompt and disable 10 second buffer so long as prompt is put up [DONE]
        // TODO: 5) display list of stations to select from/wifi networks?
        //TODO: 6) log new wifi networks (WITH STATION NAME) into Room [DONE, PENDING TESTING]
        //TODO: 7) pull and push to Firebase: a) to net wifi system and b) to user profile
        //TODO: 8) write up comment listing entire procedure
        //TODO: 9) write up POC for Arthur to access raw magnetic data

        //IMMEDIATE TODO: a) past added wifi networks still register, b) firebase
        if(timestamps.size()>1){
            if(!timestamps.get(timestamps.size()-1).equals(timestamps.get(timestamps.size()-2)) && timestamps.get(timestamps.size()-1)%10==0 && menuOpen==false){
                menuOpen = true;

                //pushes magnetic data to db
                String phone_id = PersistentID.get_id();
                myRef.child("users").child(phone_id).child("phone_id").setValue(phone_id);
                myRef.child("users").child(phone_id).child("all_data").setValue(data_meshed);
                Log.d("push_to_firebase","10 seconds");

                ArrayList<derpwork> fresh_wifi = pullWifi();

                if(fresh_wifi.size()!=0){
                    Log.d("update","new networks!");
                    promptStations();

                    /*String station_name = "test_station_name"; //temporary station name that will be replaced by user choice from promptStations()
                    control.addRoomDB(fresh_wifi,station_name);
                    control.printRoomDB();
                    control.firebasePullPush();*/


                    //TODO: extract user choice of station name from prompt
                    //TODO: make station name that selected choice
                    //TODO: push wifi networks to database
                    //TODO: sync, merge, and push to firebase

                }

            }
        }

        //Log.d("changed sensor","yes");
    }
    public void onAccuracyChanged(Sensor event, int accuracy){
        Log.d(TAG, "onAccuracyChanged: "+accuracy);

    }

    //runs a wifi scan and returns an array of local networks NOT indexed in database
    public ArrayList<derpwork> pullWifi(){
        ArrayList<derpwork> fresh_wifi = new ArrayList<>();
        if(mainwifi.startScan()){
            for (android.net.wifi.ScanResult i : mainwifi.getScanResults()) {
                String[] input = i.toString().split(",", -1);

                //initiates and appends to wifi network
                derpwork testDerpwork = new derpwork();
                testDerpwork.setName(input[0]); //STATION name, not NETWORK NAME (will be fixed upon user selection)
                testDerpwork.setSsid(input[0]); //this refers to the NETWORK NAME
                testDerpwork.setMac(input[1]);
                //sub-details
                testDerpwork.setCapabilities(input[2]);
                testDerpwork.setLevel(input[3]);
                testDerpwork.setFrequency(input[4]);
                testDerpwork.setTimestamp(input[5]);
                testDerpwork.setDistance(input[6]);
                testDerpwork.setDistanceSD(input[7]);
                testDerpwork.setPasspoint(input[8]);

                //appends fresh network to list if not in database
                if(control.search(testDerpwork).size()==0){
                    fresh_wifi.add(testDerpwork);
                }
            }
        }

        return fresh_wifi;

    }

    //When called, a prompt is summoned that requires the user to select a subway station
    //during this time, menuOpen is set as TRUE, ensuring no additional networks are requested while the user making up his mind
    public void promptStations(){
        DialogFragment dialog = new StationFragment();
        dialog.show(getFragmentManager(), "test");
    }




}