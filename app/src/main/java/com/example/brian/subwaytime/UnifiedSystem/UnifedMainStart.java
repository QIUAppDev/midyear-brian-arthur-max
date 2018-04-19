package com.example.brian.subwaytime.UnifiedSystem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.brian.subwaytime.R;

public class UnifedMainStart extends AppCompatActivity {

    private Button startUnifedMain = findViewById(R.id.button9);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        startUnifedMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),UnifedMain.class);
                startActivity(intent);
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unifed_main_start);

        //asks for location/sensor permissions
        int REQUEST_CODE = 1;
        if(!(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)){
            //if perms aren't granted, we ask
            ActivityCompat.requestPermissions(UnifedMainStart.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE);

        }
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            Log.d("permissions","granted");
        }
        else{
            Log.d("permissions","denied, something sent wrong");
        }

        Log.d("async test","it's running");
    }
}
