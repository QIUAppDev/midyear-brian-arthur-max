package com.example.brian.subwaytime.UnifiedSystem;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.brian.subwaytime.AppDatabase;
import com.example.brian.subwaytime.derpwork;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseStuff {
    /*
    * the new master class for everything database-related. This class handles:
    * -adding data to Room Database
    * -pushing/pulling from Firebase and integrating with Room
    * -ideally, all asynctasks should be made here
    * */

    final AppDatabase appDatabase;

    private FirebaseDatabase database= FirebaseDatabase.getInstance();
    private DatabaseReference myRef= database.getReference();

    public DatabaseStuff(Context context){
         appDatabase = AppDatabase.getDatabase(context);
    }

    //given a single network, returns a List of networks whose MAC addresses match on the Room DB
    public List<derpwork> search(derpwork network){
        final List<derpwork> output = new ArrayList<>();
        final derpwork network_final = network;
        new AsyncTask<Void,Void,Void>(){
            protected Void doInBackground(Void...params){
                List<derpwork> output_similar = appDatabase.networkDao().station_query_mac_nonLiveData(network_final.getMac());
                for(derpwork net : output_similar){
                    output.add(net);
                }
                return null;
            }
        }.execute();
        return output;
    }

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

    }

}
