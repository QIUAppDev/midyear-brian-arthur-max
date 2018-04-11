/*
* possible implementations:
* -collapse Network item generation into a single method
* */

package com.example.brian.subwaytime;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ActivityScan extends AppCompatActivity {
    boolean hasStarted = false;
    Button button;
    TextView text;
    final int REQUEST_CODE=1;

final AppDatabase appDatabase = AppDatabase.getDatabase(this);

    WifiManager mainwifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "onCreate: App started.");
        if(!(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)){
            //if perms aren't granted, we ask
            ActivityCompat.requestPermissions(ActivityScan.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE); //TODO whats a request code?

        }
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            Log.d("permissions","permissions granted!");
        }
        else{
            Log.d("permissions","denied, something sent wrong");
        }
        mainwifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        button= findViewById(R.id.button);
        text = findViewById(R.id.textView2);
        /*
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hasStarted){
                    start.setText("true");
                }
            }
        });
        */
    }

    public void buttonClick(View v){
        if(!hasStarted){ //start app here
            button.setText("Stop");
            text.setText("Running");
            
            if (mainwifi.startScan()){
                Log.d("wifistuff", "wifi successfuly started");
                hasStarted = true;
                for (android.net.wifi.ScanResult i : mainwifi.getScanResults() //messy but it should work
                     ) {
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
                    testAdd(input[0],input[0],input[1],input[2],input[3],input[4],input[5],input[6],input[7],input[8]);
                    //this passes the output to the database
                    //input[0] will be replaced with the station names eventually


                }
            } else {
                Log.d("wifistuff", "serious err, couldn't start wifi");//TODO PERMISSIONS potentially done rn
                hasStarted = false;
            }
            
            testAdd("network_c","ss_c","mac_c", "cap_a","level_a","freq_a","tstamp_a","dista","distsd_a","pspnt_a");

        }
        else if(hasStarted){ //stop app here
            button.setText("Start");
            text.setText("Finish");
            hasStarted=false;
            queryDB("network_a"); //searches the database
            //testAdd("network_b","ss_b","mac_b");
        }
        //test code here
        //Log.d("wifistuff", wifiOut());


    }

    /*private String wifiOut(){
        return mainwifi.getScanResults().toString();
    }*/

    public void queryDB(String name){ //does the search query
        final String nameF = name;
        new AsyncTask<Void,Void,Void>(){
            protected Void doInBackground(Void...params){
                List<derpwork> results = appDatabase.networkDao().station_query_nonLiveData(nameF);


                if(results.size()==0){Log.e("search results for " + nameF, "no search results were found,");}
                else{
                    Log.d("search results for " + nameF, "found!");
                    for(derpwork network : results){
                        Log.d(nameF + " search result", " station name: " + network.getName() + " network name: "
                                + network.getSsid() + " station MAC address: " + network.getMac());
                        //outputs any results

                    }
                }
                return null;
            }
        }.execute();
    }

    public void testAdd(String name, String ss, String mac, String cap, String level, String freq,String tstamp,String dist,String distsd,String pspnt ){
        final derpwork testDerpwork = new derpwork();
        testDerpwork.setName(name); //again, name refers to the STATION NAME, not the NETWORK NAME
        testDerpwork.setSsid(ss); //this refers to the NETWORK NOISE
        testDerpwork.setMac(mac);
        //sub-details
        testDerpwork.setCapabilities(cap);
        testDerpwork.setLevel(level);
        testDerpwork.setFrequency(freq);
        testDerpwork.setTimestamp(tstamp);
        testDerpwork.setDistance(dist);
        testDerpwork.setDistanceSD(distsd);
        testDerpwork.setPasspoint(pspnt);

        new AsyncTask<Void,Void,Void>(){
            protected Void doInBackground(Void...params){
                //checks if network doesn't exist, and adds it if it does
                //this was the first problematic section
                if(appDatabase.networkDao().station_query_mac_nonLiveData(testDerpwork.getMac()).size()==0){
                    appDatabase.networkDao().insertAll(testDerpwork);
                }
                else{
                    Log.d("Update","The derpwork " + testDerpwork.getName() + " already exists, so it was not added");
                }

                //Some extra uesful diagnostic info
                Log.d("number of networks",Integer.toString(appDatabase.networkDao().getCount()));
                List<derpwork> derpwork_list = appDatabase.networkDao().getAll_nonLiveData();

                for(derpwork a : derpwork_list){
                    Log.d("derpwork " + a.getName(),"SSID: " + a.getSsid() + ", MAC: " + a.getMac() + ", levels: " + a.getLevel());
                }

                //keep this around
                return null;
            }
        }.execute();
    }

    public void resetDB(){
        new AsyncTask<Void,Void,Void>(){ //deletes all rows as expected. don't run if there are no rows. idk what'll happen
            protected Void doInBackground(Void...params){
                appDatabase.networkDao().deleteAll();
                Log.d("update","all previous networks have been deleted");
                return null;
            }
        }.execute();
    }
}
