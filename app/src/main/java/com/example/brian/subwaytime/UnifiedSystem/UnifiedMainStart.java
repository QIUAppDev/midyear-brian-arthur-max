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

public class UnifiedMainStart extends AppCompatActivity {
    /** This is the startup method
     *
     */

//    private Button startUnifedMain;
    private String TAG = "UnifiedMainStart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unifed_main_start);

        if(!requestPerms()){
           //user has not granted permissions
           //TODO transfer user to explaination why they can't use the app, offer to request perms again
           requestPerms(); // here we'll just request again to be safe, note that this is a one time thing and won't loop. This is just for testing
        }else{
            Intent intent = new Intent(getApplicationContext(),UnifiedMain.class);
            startActivity(intent);
        };
       /*  this bit shouldn't be needed. why would we need an additional button?
        startUnifedMain = findViewById(R.id.button9);
        startUnifedMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),UnifiedMain.class);
                startActivity(intent);
            }
        });
        */
    }

    /**
     * method requests permissions from the user needed to run the app
     * @return boolean if permissions were granted by user
     */
    private boolean requestPerms(){
        //asks for location/sensor permissions
        int REQUEST_CODE = 1;
        if(!(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)) {
            //if perms aren't granted, we ask
            ActivityCompat.requestPermissions(UnifiedMainStart.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            Log.d(TAG, "permissions requested");
        }
        
        //TODO potential bug here, not sure how synchronous this request process is 
        
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "requestPerms: permissions granted");
            return true;
        }
        Log.d(TAG, "requestPerms: permissions denied");
        return false;
  
    }
}
