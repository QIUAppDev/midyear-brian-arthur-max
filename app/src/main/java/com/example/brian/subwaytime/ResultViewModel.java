package com.example.brian.subwaytime;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by brian on 1/15/18.
 */

public class ResultViewModel extends AndroidViewModel {
    private final LiveData<List<derpwork>> output_list;

    private AppDatabase appDatabase; //will migrate query methods later

    public ResultViewModel(Application application){
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
        output_list = (LiveData<List<derpwork>>)appDatabase.networkDao().getAll();
    }

    public LiveData<List<derpwork>> getOutput_list(){
        return output_list;
    }
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
}
