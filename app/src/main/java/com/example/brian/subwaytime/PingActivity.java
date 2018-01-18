package com.example.brian.subwaytime;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class PingActivity extends AppCompatActivity {

    //insert hotspot stuff here;
    private String ssid_temp = "G6";
    private String mac_temp = "de:0b:34:c4:ac:e7";
    WifiManager mainwifi;
    List<derpwork> networks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping);


        if (mainwifi.startScan()){
            Log.d("wifistuff", "wifi successfuly started");
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
                networks.add(testDerpwork);
                //this passes the output to the database
                //input[0] will be replaced with the station names eventually


            }
        } else {
            Log.d("wifistuff", "serious err, couldn't start wifi");//TODO PERMISSIONS potentially done rn
        }
        for(derpwork network : networks){
            if(network.getSsid().equals(ssid_temp) && network.getMac().equals(mac_temp)){
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            }
        }
    }
}
