package com.example.brian.subwaytime;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;

public class FirebaseTest extends AppCompatActivity {

    //FIREBASE stuff
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //Room Persistence DB
    final AppDatabase appDatabase = AppDatabase.getDatabase(this);

    private Button push_to_firebase;
    private Button pull_from_firebase;
    private Button back_to_menu;

    private static final String TAG = "aaaaaaaaaa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_test);

        //init the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        //init the buttons and the UI
        push_to_firebase = findViewById(R.id.button4);
        pull_from_firebase = findViewById(R.id.button5);
        back_to_menu = findViewById(R.id.button6);

        push_to_firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushFirebase();
            }
        });

        pull_from_firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pullFirebase();
            }
        });

        back_to_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: add back to menu
            }
        });

    }

    public void pullFirebase(){
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                //pulls and directly de-serializes fresh network objects
                for(DataSnapshot network : dataSnapshot.getChildren()){
                    final derpwork testwork = network.getValue(derpwork.class);

                    new AsyncTask<Void,Void,Void>(){
                        public Void doInBackground(Void... params){
                            Log.d("testwork name", testwork.getName());
                            if(appDatabase.networkDao().isAdded_nonLiveData(testwork.getName(),testwork.getSsid(),testwork.getMac()).size()==0){
                                appDatabase.networkDao().insertAll(testwork);
                            }
                            else{
                                Log.e("network already here","The network called " + testwork.getSsid() + "is already in the db, so it wasn't added");
                            }

                            return null;
                        }
                    }.execute();

                }

                //TODO: write code that adds new objects to local db

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void pushFirebase(){ //uses the derpwork class STRUCTURE to add to firebase
        //proposed feature: since our DB is literally just one table, a simple method can read off incoming JSON files and write into db
        //advantages: boilerplate recyclerview code remains untouched

        //generates a test object and appends to the wifi network
        //ideally, on the subway run, Room persistence captures the wifi networks, and Firebase bundles them up and pushes them

        Log.d("testData","progress got here");

        /*
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
        */


        //pulls the networks accumulated in the local Room database, and pushes it onto Firebase
        new AsyncTask<Void,Void,Void>(){
            protected Void doInBackground(Void... params){
                List<derpwork> all_networks = appDatabase.networkDao().getAll_nonLiveData();
                HashMap<String, derpwork> wifi_push = new HashMap<>();
                for(derpwork network : all_networks){
                    wifi_push.put(network.getSsid(), network);
                }
                myRef.setValue(wifi_push);

                return null;
            }
        }.execute();


        //wifi_push.put("station_1",test_network);

        //myRef.child("station_1").setValue(test_network);





    }


}
