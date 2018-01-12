package com.example.brian.subwaytime;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by brian on 1/11/18.
 */

@Database(entities = {Network.class},version=1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NetworkDao networkDao();
}
