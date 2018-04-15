package com.example.brian.subwaytime.UnifiedSystem;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.example.brian.subwaytime.AppDatabase;
import com.example.brian.subwaytime.derpwork;

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

    public DatabaseStuff(Context context){
         appDatabase = AppDatabase.getDatabase(context);
    }

    //given a single network, returns a List of networks whose MAC addresses match
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


}
