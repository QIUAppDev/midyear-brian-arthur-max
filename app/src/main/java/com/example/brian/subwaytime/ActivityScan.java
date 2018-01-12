package com.example.brian.subwaytime;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ActivityScan extends AppCompatActivity {
    boolean hasStarted = false;
    Button button;
    TextView text;

    final AppDatabase appDatabase = AppDatabase.getDatabase(this);

    /*WifiManager mainwifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        button= (Button)findViewById(R.id.button);
        text = (TextView)findViewById(R.id.textView2);
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
        if(!hasStarted){
            button.setText("Stop");
            text.setText("Running");
            /*
            if (mainwifi.startScan()){
                Log.d("wifistuff", "wifi successfuly started");
                hasStarted = true;
            } else {
                Log.d("wifistuff", "serious err, couldn't start wifi");//TODO PERMISSIONS
                hasStarted = false;
            }
            */
            hasStarted=true;
            testAdd("network_a","ss_a","mac_a");
        }
        else if(hasStarted){
            button.setText("Start");
            text.setText("Finish");
            hasStarted=false;
            testAdd("network_b","ss_b","mac_b");
        }
        //test code here
        //Log.d("wifistuff", wifiOut());


    }

    /*private String wifiOut(){
        return mainwifi.getScanResults().toString();
    }*/

    public void testAdd(String name, String ss, String mac){
        final Network testNetwork = new Network();
        testNetwork.setName(name);
        testNetwork.setSsid(ss);
        testNetwork.setMac(mac);

        final Network testNetwork_final = testNetwork;
        new AsyncTask<Void,Void,Void>(){
            protected Void doInBackground(Void...params){
                appDatabase.networkDao().insertAll(testNetwork);
                Log.d("number of networks",Integer.toString(appDatabase.networkDao().getCount()));
                List<Network> network_list= appDatabase.networkDao().getAll();
                for(Network a : network_list){
                    Log.d("Network " + a.getName(),"SSID: " + a.getSsid() + ", MAC: " + a.getMac());
                }
                return null;
            }
        }.execute();
    }
}
