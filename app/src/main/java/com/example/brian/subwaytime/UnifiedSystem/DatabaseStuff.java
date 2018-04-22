package com.example.brian.subwaytime.UnifiedSystem;

import android.app.Application;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.util.Log;

import com.example.brian.subwaytime.AppDatabase;
import com.example.brian.subwaytime.PersistentID;
import com.example.brian.subwaytime.derpwork;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseStuff implements OnTaskCompleted{
    /*
    * the new master class for everything database-related. This class handles:
    * -adding data to Room Database
    * -pushing/pulling from Firebase and integrating with Room
    * -ideally, all asynctasks should be made here
    * */

    //Room
    public final AppDatabase appDatabase;
    public final AppDatabase WifiDatabase;

    //Firebase
    private FirebaseDatabase database= FirebaseDatabase.getInstance();
    private DatabaseReference myRef= database.getReference();

    //required for AsyncTask
    Context con;

    //unique app id
    String phone_id = PersistentID.get_id();

    public DatabaseStuff(Context context){
        con = context;
        appDatabase = AppDatabase.getDatabase(context); //inits sql db
        WifiDatabase = AppDatabase.getLocalWifiDatabase(context); //does the wifi thing
    }

    //given a single network, returns a List of networks whose MAC addresses match on the Room DB
    public List<derpwork> search(final derpwork network){

        List<derpwork> output = new ArrayList<>();

        //instantiates async task and directly grabs data
        SearchAsync async_search = new SearchAsync(con, network);
        async_search.setListener(this);
        try{
            output = async_search.execute().get();
        } catch(ExecutionException | InterruptedException e){ //exception handling Android requires
            Log.e("async","failed");
        }

        Log.d("results_direct_size",Integer.toString(output.size()));

        return output;
    }

    //dumps wifi scans into the WiFi Room Persistence DB
    public void addtoRoomWifiDB(final List<derpwork> results){
        new AsyncTask<Void,Void,Void>(){
            public Void doInBackground(Void...params){
                for(derpwork network:results){
                    WifiDatabase.networkDao().insertAll(network);
                }
                Log.d("size_of_wifi",Integer.toString(WifiDatabase.networkDao().getCount()));
                return null;
            }
        }.execute();
    }

    //this method is invoked when the AsyncTask finishes
    //NOTE: DEPRECIATED
    public void onTaskCompleted(List<derpwork> results){ }

    //prints all entries in the Room database
    public void printRoomDB(){
        new AsyncTask<Void,Void,Void>(){
            protected Void doInBackground(Void...params){
                List<derpwork> all_networks = appDatabase.networkDao().getAll_nonLiveData();
                for(derpwork network : all_networks){
                    Log.d("printDB","SSID: " + network.getSsid() + ", MAC: " + network.getMac());
                }
                Log.d("printDB_size",Integer.toString(appDatabase.networkDao().getCount()));
                return null;
            }
        }.execute();
    }

    //adds wifi networks with picked station names to db
    public void addRoomDB(final ArrayList<derpwork> add_to_db, final String station_name){
        new AsyncTask<Void,Void,Void>(){
            protected Void doInBackground(Void...params){
                for(derpwork network : add_to_db){
                    network.setName(station_name);
                    appDatabase.networkDao().insertAll(network);
                }
                return null;
            }
        }.execute();
    }

    //pulls latest wifi networks from Firebase, appends it to local DB, and pushes entire thing back to Firebase
    //also pushes local DB to user profile on Firebase
    public void firebasePullPush(){

        //connects to the net wifi section on Firebase
        myRef = database.getReference("message");
        userPushFirebase();
        pushFirebase();

        pullFirebase();

    }

    //Firebase helper method that pulls from Firebase
    private void pullFirebase(){
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                //pulls and directly de-serializes fresh network objects
                for(DataSnapshot network : dataSnapshot.getChildren()){

                    //de-serializes object and reconstructs it to strip the autogenerated id
                    derpwork input_network = network.getValue(derpwork.class);
                    final derpwork testwork = new derpwork();
                    testwork.setTimestamp(input_network.getTimestamp());
                    testwork.setPasspoint(input_network.getPasspoint());
                    testwork.setMac(input_network.getMac());
                    testwork.setLevel(input_network.getLevel());
                    testwork.setDistanceSD(input_network.getDistanceSD());
                    testwork.setCapabilities(input_network.getCapabilities());
                    testwork.setSsid(input_network.getSsid());
                    testwork.setName(input_network.getName());
                    testwork.setDistance(input_network.getDistance());
                    testwork.setFrequency(input_network.getFrequency());

                    //if the network isn't already added, it is added to the local database
                    new AsyncTask<Void,Void,Void>(){
                        public Void doInBackground(Void... params){
                            Log.d("testwork name", testwork.getName());
                            if(appDatabase.networkDao().station_query_mac_nonLiveData(testwork.getMac()).size()==0){
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
                Log.w("Firebase_ERRIR", "Failed to read value.", error.toException());
            }
        });
    }

    //pulls existing wifi networks from Firebase, appends to
    public void pushFirebase(){

        Log.d("testData","progress got here");


        //pulls the networks accumulated in the local Room database, and pushes it onto Firebase
        new AsyncTask<Void,Void,Void>(){
            protected Void doInBackground(Void... params){

                //gets locally accumulated data
                List<derpwork> all_networks = appDatabase.networkDao().getAll_nonLiveData();
                final HashMap<String, derpwork> wifi_push = new HashMap<>();
                for(derpwork network : all_networks){
                    wifi_push.put(network.getMac(), network);
                }

                //downloads existing data, appends local DB, and pushes
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("debugstep","stepping here");
                        for(DataSnapshot network : dataSnapshot.getChildren()){
                            derpwork input_network = network.getValue(derpwork.class);
                            wifi_push.put(input_network.getMac(),input_network);
                        }
                        myRef.setValue(wifi_push);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Database_ERROR", "Failed to read value.", databaseError.toException());
                    }
                });


                return null;
            }
        }.execute();

        Log.d("testaa","we get here");
    }

    //pushes room DB to user profile on Firebase
    //by being run first, the Room DB at this point already represents the new wifi networks
    public void userPushFirebase(){
        new AsyncTask<Void,Void,Void>(){
            protected Void doInBackground(Void...params){
                myRef = database.getReference("users");
                HashMap<String, derpwork> user_wifi_push = new HashMap<>();

                for(derpwork network : appDatabase.networkDao().getAll_nonLiveData()){
                    user_wifi_push.put(network.getMac(),network);
                }
                myRef.child(phone_id).child("wifi_networks").setValue(user_wifi_push);
                return null;
            }
        }.execute();
    }

}

//interface that facilitates callback of search results
interface OnTaskCompleted{
    public void onTaskCompleted(List<derpwork> search_results);
}

//separate AsyncTask to handle search results from search()
class SearchAsync extends AsyncTask<List<derpwork>,Void,List<derpwork>>{

    //carries over listener from OnTaskCompleted
    private OnTaskCompleted listener;

    //Room persistence DB
    private final AppDatabase appDatabase;

    //prospective network that's being searched
    private derpwork network;

    public SearchAsync(Context context, derpwork net){
        appDatabase = AppDatabase.getDatabase(context);
        network=net;
    }

    //instantiates listener
    public void setListener(OnTaskCompleted listen){
        listener=listen;
    }

    //this method runs in the background and does the Room DB search, returning similarities between the network passed in and the user's local Room DB
    @Override
    protected List<derpwork> doInBackground(List<derpwork>...params){
        List<derpwork> output_similar = appDatabase.networkDao().station_query_mac_nonLiveData(network.getMac());
        return output_similar;
    }

    //this method runs when doInBackground finishes, and passes the search results back to DatabaseStuff
    @Override
    protected void onPostExecute(List<derpwork> list){
        listener.onTaskCompleted(list);
        //Log.d("output_final_size",Integer.toString(output.size()));
    }
}