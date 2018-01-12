package com.example.brian.subwaytime;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

/**
 * Created by brian on 1/11/18.
 */

@Database(entities = {derpwork.class},version=2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract NetworkDao networkDao();
    public static AppDatabase getDatabase(Context context){ /*implementation of "singleton" db*/
        if(INSTANCE==null){
            static final Migration MIGRATION_1_2 = new Migration(1,2){
                @Override
                public void migrate(SupportSQLiteDatabase database){
                    database.execSQL("");
                }
            };
            INSTANCE= Room.databaseBuilder(context,
                    AppDatabase.class, "network_db").addMigrations(MIGRATION_1_2).build();

        }
        return INSTANCE;
    }
}
