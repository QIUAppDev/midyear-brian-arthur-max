package com.example.brian.subwaytime;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityScan extends AppCompatActivity {
    boolean hasStarted = false;
    Button button;
    TextView text;

    WifiManager mainwifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

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
            if (mainwifi.startScan()){
                Log.d("wifistuff", "wifi successfuly started");
                hasStarted = true;
            } else {
                Log.d("wifistuff", "serious err, couldn't start wifi");//TODO PERMISSIONS
                hasStarted = false;
            }
        }
        else if(hasStarted){
            button.setText("Start");
            text.setText("Finish");
            hasStarted=false;
        }
        //test code here
        Log.d("wifistuff", wifiOut());


    }

    private String wifiOut(){
        return mainwifi.getScanResults().toString();
    }
}
