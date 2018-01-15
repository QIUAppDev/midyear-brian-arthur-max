package com.example.brian.subwaytime;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by brian on 1/11/18.
 */

@Dao
public interface NetworkDao {

    @Query("SELECT * FROM derpwork")
    LiveData<List<derpwork>> getAll(); //List<derpwork> was the original. LiveData ensured the front end works iwth backend.
    //changes were accordingly made on the ActivityScan side, so things should still work

    /*check if the network's already there*/
    @Query("SELECT * FROM derpwork WHERE network_name LIKE :name OR ssid LIKE :ssid OR mac_address LIKE :mac")
    List<derpwork> isAdded(String name, String ssid, String mac);

    @Query("SELECT * FROM derpwork WHERE network_name LIKE :name")
    List<derpwork> station_query(String name);

    @Query("SELECT COUNT(*) FROM derpwork")
    int getCount();

    @Query("DELETE FROM derpwork")
    void deleteAll();

    @Insert
    void insertAll(derpwork... derpworks);

    @Delete
    void delete(derpwork derpwork);

}
