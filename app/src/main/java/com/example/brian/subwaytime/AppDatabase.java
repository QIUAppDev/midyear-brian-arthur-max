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

@Database(entities = {derpwork.class},version=9)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract NetworkDao networkDao();
    public static AppDatabase getDatabase(Context context){ /*implementation of "singleton" db*/
        if(INSTANCE==null){

            final Migration MIGRATION_1_2 = new Migration(1,2){
                @Override
                public void migrate(SupportSQLiteDatabase database){
                    database.execSQL("ALTER TABLE `network RENAME TO derpwork");
                }
            };
            final Migration MIGRATION_2_3 = new Migration(2,3){
                @Override
                public void migrate(SupportSQLiteDatabase database){
                    database.execSQL("ALTER TABLE derpwork ADD COLUMN capabilities TEXT");
                }
            };
            final Migration MIGRATION_3_4 = new Migration(3,4){
                @Override
                public void migrate(SupportSQLiteDatabase database){
                    database.execSQL("ALTER TABLE derpwork ADD COLUMN level TEXT");
                }
            };
            final Migration MIGRATION_4_5 = new Migration(4,5){
                @Override
                public void migrate(SupportSQLiteDatabase database){
                    database.execSQL("ALTER TABLE derpwork ADD COLUMN frequency TEXT" );
                }
            };
            final Migration MIGRATION_5_6 = new Migration(5,6){
                @Override
                public void migrate(SupportSQLiteDatabase database){
                    database.execSQL("ALTER TABLE derpwork ADD COLUMN timestamp TEXT" );
                }
            };
            final Migration MIGRATION_6_7 = new Migration(6,7){
                @Override
                public void migrate(SupportSQLiteDatabase database){
                    database.execSQL("ALTER TABLE derpwork ADD COLUMN distance TEXT" );
                }
            };
            final Migration MIGRATION_7_8 = new Migration(7,8){
                @Override
                public void migrate(SupportSQLiteDatabase database){
                    database.execSQL("ALTER TABLE derpwork ADD COLUMN distanceSD TEXT");
                }
            };
            final Migration MIGRATION_8_9 = new Migration(8,9){
                @Override
                public void migrate(SupportSQLiteDatabase database){
                    database.execSQL("ALTER TABLE derpwork ADD COLUMN passpoint TEXT");
                }
            };
            INSTANCE= Room.databaseBuilder(context,
                    AppDatabase.class, "network_db").addMigrations(MIGRATION_1_2, MIGRATION_2_3,MIGRATION_3_4
            ,MIGRATION_4_5,MIGRATION_5_6,MIGRATION_6_7,MIGRATION_7_8,MIGRATION_8_9).build();

        }
        return INSTANCE;
    }
}
