/*
* insert this code
* private SensorManager mSensorManager;
private Sensor mSensor;
private TriggerEventListener mTriggerEventListener;
...
mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);

mTriggerEventListener = new TriggerEventListener() {
    @Override
    public void onTrigger(TriggerEvent event) {
        // Do work
    }
};

mSensorManager.requestTriggerSensor(mTriggerEventListener, mSensor);


//proposal: add button?
* */


package com.example.brian.subwaytime;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PingActivity extends AppCompatActivity {

    //insert hotspot stuff here;
    private String ssid_temp = "G6"; //left these two just in case shit breaks
    private String mac_temp = "de:0b:34:c4:ac:e7";
    private TextView ssid;
    private TextView mac;
    private TextView output;
    private WifiManager mainwifi;
    private List<derpwork> networks;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TriggerEventListener mTriggerEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping);

        //dada dum du dum! (sakurasou was a really good anime)
        String[] ins = getIntent().getStringArrayExtra("davai hard");
        final String ssidText = ins[0];
        final String macText = ins[1];

        //instantiates and sends ssid to ui
        ssid = findViewById(R.id.textView5);
        ssid.setText("SSID: " + ssidText);

        //instantiates and sends mac address to ui
        mac = findViewById(R.id.textView6);
        mac.setText("MAC: " + macText);

        //instanties and sends msg to user about wifi status
        //this is the initial condition, which will change if a wifi match is detected
        output = findViewById(R.id.textView7);
        output.setText("Out of Range");
        output.setTextColor(Color.RED);


        mainwifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //the wifi networks pinged are collected into a a list of derpwork objects, which we compare with the given ssid + mac
        networks = new ArrayList<derpwork>();


        //the timer
        final Handler handler = new Handler();
        final int delay = 5000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                if (mainwifi.startScan()){
                    Log.d("wifistuff", "wifi successfuly started");
                    for (android.net.wifi.ScanResult i : mainwifi.getScanResults()) {
                        //messy but it should work
                        String[] input = i.toString().split(",", -1);
                    /* you know it's great when you need a multiline comment to explain what you just did
                    input is an array of all the vars that you're gonna need. It will look something like the following array
                    {SSID: ssidNameHere,
                    BSSID: so:me:th:in:g_:in,
                    capabilities: [WPA2-PSK-CCMP][ESS],
                    level: -(this is signal strength, more negative, lower numbers mean stronger signal),
                    frequency: (honestly irrelevant),
                    timestamp: (presumably miliseconds since the code started),
                    distance: (very inconsistent, might be a bad idea to use),
                    distanceSD: (I have no idea),
                    passpoint: (is personal hotspot),
                    etc. you get the point. These will always be in the same order, so just query the int location of data you need. Stuff like at 0, you have SSID, and BSSID is it's mac address

                    oh and also this is a foreach loop, so just use the SSID to order the DB or the time added. Idc, just know there's no int i.

                    */
                        Log.d("shitfuck",i.toString());

                        //instnatiates temporary derpwrok object and adds values to it
                        derpwork testDerpwork = new derpwork();
                        testDerpwork.setName(input[0]); //again, name refers to the STATION NAME, not the NETWORK NAME
                        testDerpwork.setSsid(input[0]); //this refers to the NETWORK NOISE
                        testDerpwork.setMac(input[1]);
                        //sub-details
                        testDerpwork.setCapabilities(input[2]);
                        testDerpwork.setLevel(input[3]);
                        testDerpwork.setFrequency(input[4]);
                        testDerpwork.setTimestamp(input[5]);
                        testDerpwork.setDistance(input[6]);
                        testDerpwork.setDistanceSD(input[7]);
                        testDerpwork.setPasspoint(input[8]);

                        //adds value to list
                        networks.add(testDerpwork);



                    }
                } else {
                    Log.d("wifistuff", "serious err, couldn't start wifi");//TODO PERMISSIONS potentially done rn
                }

                //runs through our list and checks for a match
                if(networks.size()>0){
                    for(derpwork network : networks){
                        if(network.getSsid().equals(ssidText) && network.getMac().equals(macText)){

                            //the ringer
                            //TODO: fix bug
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(),notification);
                            mp.start();

                            //changes condition to valid
                            output.setText("In Range!");
                            output.setTextColor(Color.GREEN);
                        }
                    }
                }
                handler.postDelayed(this, delay);
            }
        }, delay);


        /*
        //starts the sensor manager
        //basically the code reference from android
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);

        mTriggerEventListener = new TriggerEventListener() {
            @Override
            public void onTrigger(TriggerEvent event) {


                //when android detects the shaking, it triggers the scan

                //essentially activityscan's code


                //end of onTrigger method
            }
        };

        //starts the process, I think?
        mSensorManager.requestTriggerSensor(mTriggerEventListener, mSensor);
        */

    }
}
