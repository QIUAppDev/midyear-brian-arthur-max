package com.example.brian.subwaytime;

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

    @Query("SELECT * FROM NETWORK")
    List<Network> getAll();

    /*check if the network's already there*/
    @Query("SELECT * FROM NETWORK WHERE network_name LIKE :name OR ssid LIKE :ssid OR mac_address LIKE mac")
    boolean isAdded(String name, String ssid, String mac);

    @Insert
    void insertAll(Network ... networks);

    @Delete
    void delete(Network network);

}
