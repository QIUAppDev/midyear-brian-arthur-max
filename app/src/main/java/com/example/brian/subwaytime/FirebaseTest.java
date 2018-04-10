package com.example.brian.subwaytime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class FirebaseTest extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    private static final String TAG = "aaaaaaaaaa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_test);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                //pulls and directly de-serializes fresh network objects
                for(DataSnapshot network : dataSnapshot.getChildren()){
                    derpwork testwork = network.getValue(derpwork.class);
                    Log.d("testwork name", testwork.getName());
                }

                //TODO: write code that adds new objects to local db

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        pushNewData();
    }

    public void pushNewData(){ //uses the derpwork class STRUCTURE to add to firebase
        //proposed feature: since our DB is literally just one table, a simple method can read off incoming JSON files and write into db
        //advantages: boilerplate recyclerview code remains untouched

        Log.d("testData","progress got here");

        //generates a test object and appends to the wifi network
        //ideally, on the subway run, Room persistence captures the wifi networks, and Firebase bundles them up and pushes them
        derpwork test_network = new derpwork();
        String MAC="mac_1";
        test_network.setName("station_1"); //again, to store the actual station name
        test_network.setSsid("test_network_1");
        test_network.setCapabilities("capabilities_1");
        test_network.setDistance("distance_1");
        test_network.setDistanceSD("distanceSD_1");
        test_network.setLevel("level_1");
        test_network.setMac(MAC);
        test_network.setPasspoint("passpoint_1");
        test_network.setTimestamp("timestamp 1");

        HashMap<String, derpwork> wifi_push = new HashMap<>();
        wifi_push.put("station_1",test_network); //station_1 corresponds to the key and will be replaced by something better as a key
        myRef.setValue(wifi_push);
        //myRef.child("station_1").setValue(test_network);





    }


}
