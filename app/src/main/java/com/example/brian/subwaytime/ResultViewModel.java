package com.example.brian.subwaytime;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

/**
 * Created by brian on 1/15/18.
 */

public class ResultViewModel extends AndroidViewModel {
    private final LiveData<List<derpwork>> output_list;

    private AppDatabase appDatabase; //instantiates database

    public ResultViewModel(Application application){
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
        output_list = appDatabase.networkDao().getAll(); //grabs dataset
    }


    //getter method for dataset
    public LiveData<List<derpwork>> getOutput_list(){
        return output_list;
    }

    //delete item from db
    public void deleteItem(derpwork network){
        new deleteAsyncTask(appDatabase).execute(network);
    }
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
        LiveData<List<derpwork>> search_results = appDatabase.networkDao().station_query(search_query);
        //if(search_results==null){Log.e("does the object exist","no");}
        //else{Log.d("does the object exist","yes");}

        //for now, some demo code that Logs the search results
        //will eventually trigger changes
        //List<derpwork> results_log = (List<derpwork>)search_results;
    }
}
