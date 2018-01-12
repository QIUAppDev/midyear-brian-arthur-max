package com.example.brian.subwaytime;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by brian on 1/11/18.
 */

@Database(entities = {Network.class},version=1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract NetworkDao networkDao();
    public static AppDatabase getDatabase(Context context){ /*implementation of "singleton" db*/
        if(INSTANCE==null){
            INSTANCE= Room.databaseBuilder(context,
                    AppDatabase.class, "network_db").build();
        }
        return INSTANCE;
    }
}
