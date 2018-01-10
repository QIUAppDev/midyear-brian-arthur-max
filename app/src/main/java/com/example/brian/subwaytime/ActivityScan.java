package com.example.brian.subwaytime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityScan extends AppCompatActivity {
    boolean hasStarted=false;
    Button button;
    TextView text;
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
            hasStarted=true;
        }
        else if(hasStarted){
            button.setText("Start");
            text.setText("Finish");
            hasStarted=false;
        }
    }
}
