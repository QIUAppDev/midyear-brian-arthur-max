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
import java.util.concurrent.ExecutionException;

public class DatabaseStuff implements OnTaskCompleted{
    /*
    * the new master class for everything database-related. This class handles:
    * -adding data to Room Database
    * -pushing/pulling from Firebase and integrating with Room
    * -ideally, all asynctasks should be made here
    * */

    private final AppDatabase appDatabase;

    private FirebaseDatabase database= FirebaseDatabase.getInstance();
    private DatabaseReference myRef= database.getReference();

    Context con;

    public DatabaseStuff(Context context){
        con = context;
        appDatabase = AppDatabase.getDatabase(context);
    }

    //given a single network, returns a List of networks whose MAC addresses match on the Room DB
    public List<derpwork> search(final derpwork network){

        List<derpwork> output = new ArrayList<>();

        SearchAsync async_search = new SearchAsync(con, network);
        async_search.setListener(this);
        try{
            output = async_search.execute().get();
        } catch(ExecutionException | InterruptedException e){
            Log.e("async","failed");
        }

        Log.d("results_direct_size",Integer.toString(output.size()));

        return output;
        /*

        replaced by SearchAsync class to facilitate Callback for getting output

        final List<derpwork> output = new ArrayList<>();
        new AsyncTask<List<derpwork>,Void,List<derpwork>>(){

            private OnTaskCompleted listener;
            @Override
            protected List<derpwork> doInBackground(List<derpwork>...params){
                List<derpwork> output_similar = appDatabase.networkDao().station_query_mac_nonLiveData(network.getMac());

                for(derpwork net : output_similar){
                    output.add(net);
                    Log.d("output",Integer.toString(output.size()));
                }
                return output;
            }
            @Override
            protected void onPostExecute(List<derpwork> list){
                Log.d("output_final_size",Integer.toString(output.size()));

            }
        }.execute();
        */
    }

    //when the AsyncTask is completed, this method runs and assigns wifi_search_results the latest results
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

    }

}

//interface that facilitates callback of search results
interface OnTaskCompleted{
    public void onTaskCompleted(List<derpwork> search_results);
}

class SearchAsync extends AsyncTask<List<derpwork>,Void,List<derpwork>>{

    private OnTaskCompleted listener;

    private final AppDatabase appDatabase;

    private derpwork network;

    public SearchAsync(Context context, derpwork net){
        appDatabase = AppDatabase.getDatabase(context);
        network=net;
    }

    public void setListener(OnTaskCompleted listen){
        listener=listen;
    }
    @Override
    protected List<derpwork> doInBackground(List<derpwork>...params){
        List<derpwork> output_similar = appDatabase.networkDao().station_query_mac_nonLiveData(network.getMac());
        return output_similar;

        /*for(derpwork net : output_similar){
            output.add(net);
            Log.d("output",Integer.toString(output.size()));
        }
        return output;*/
    }
    @Override
    protected void onPostExecute(List<derpwork> list){
        listener.onTaskCompleted(list);
        //Log.d("output_final_size",Integer.toString(output.size()));
    }
}