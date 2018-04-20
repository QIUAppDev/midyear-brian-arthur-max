package com.example.brian.subwaytime;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

/**
 * Created by brian on 1/15/18.
 */

public class ResultViewModel extends AndroidViewModel {
    private MutableLiveData<List<derpwork>> output_list, wifi_list;

    private AppDatabase appDatabase; //instantiates database
    private AppDatabase WifiDatabase;

    public ResultViewModel(Application application){
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
        WifiDatabase = AppDatabase.getLocalWifiDatabase(this.getApplication());
        wifi_list = new MutableLiveData<List<derpwork>>();
        wifi_list.setValue(WifiDatabase.networkDao().getAll_nonLiveData());
        output_list = new MutableLiveData<List<derpwork>>();
        output_list.setValue(appDatabase.networkDao().getAll_nonLiveData()); //grabs dataset
    }


    //getter method for dataset
    public MutableLiveData<List<derpwork>> getOutput_list(){
        return output_list;
    }
    public MutableLiveData<List<derpwork>> getWifi_list(){
        return output_list;
    }

    /**
    //delete item from db
    public void deleteItem(derpwork network){
        new deleteAsyncTask(appDatabase).execute(network);
    }
    **/
    private static class deleteAsyncTask extends AsyncTask<derpwork, Void, Void>{
        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase){
            db=appDatabase;
        }

        @Override
        protected Void doInBackground(final derpwork... params){
            db.networkDao().delete(params[0]);
            return null;
        }
    }

    //does the actual querying-the-db-part
    public void query_for_search_result(String search_query){
        List<derpwork> search_results = appDatabase.networkDao().station_query_nonLiveData(search_query);
        List<derpwork> full_results = appDatabase.networkDao().getAll_nonLiveData();
        output_list.setValue(search_results);

        Log.d("search query",search_query);
        Log.d("search swapped","yes");
        Log.d("size of result",Integer.toString(search_results.size()));
        Log.d("full db size",Integer.toString(full_results.size()));
        //if(search_results==null){Log.e("does the object exist","no");}
        //else{Log.d("does the object exist","yes");}

        //for now, some demo code that Logs the search results
        //will eventually trigger changes
        //List<derpwork> results_log = (List<derpwork>)search_results;

        //return search_results;
    }
}
