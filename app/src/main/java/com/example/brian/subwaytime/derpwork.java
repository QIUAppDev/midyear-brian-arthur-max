package com.example.brian.subwaytime;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by brian on 1/11/18.
 */

@Entity
public class derpwork {

    public derpwork(){

    }

    @PrimaryKey(autoGenerate = true)
    public int network_id;



    @ColumnInfo(name="network_name")
    private String name;



    @ColumnInfo(name="ssid")
    private String ssid;



    @ColumnInfo(name="mac_address")
    private String mac;

    public int getNetwork_id(){return network_id;}
    public void setNetwork_id(int id){network_id=id;}

    public String getName(){return name;}
    public void setName(String a){name=a;}

    public String getSsid(){return ssid;}
    public void setSsid(String ss){ssid=ss;}

    public String getMac(){return mac;}
    public void setMac(String m){mac=m;}


}
