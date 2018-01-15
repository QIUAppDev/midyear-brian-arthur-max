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



    @ColumnInfo(name="network_name") //actually station names
    private String name;


    @ColumnInfo(name="ssid")
    private String ssid;


    @ColumnInfo(name="mac_address")
    private String mac;

    @ColumnInfo(name="capabilities")
    private String capabilities;

    @ColumnInfo(name="level")
    private String level;
    @ColumnInfo(name="frequency")
    private String frequency;
    @ColumnInfo(name="timestamp")
    private String timestamp;
    @ColumnInfo(name="distance")
    private String distance;
    @ColumnInfo(name="distanceSD")
    private String distanceSD;
    @ColumnInfo(name="passpoint")
    private String passpoint;

    public int getNetwork_id(){return network_id;}
    public void setNetwork_id(int id){network_id=id;}

    public String getName(){return name;}
    public void setName(String a){name=a;}

    public String getSsid(){return ssid;}
    public void setSsid(String ss){ssid=ss;}

    public String getMac(){return mac;}
    public void setMac(String m){mac=m;}

    public String getCapabilities(){return capabilities;}
    public void setCapabilities(String c){capabilities=c;}

    public String getLevel(){return level;}
    public void setLevel(String l){level=l;}

    public String getFrequency(){return frequency;}
    public void setFrequency(String f){frequency=f;}

    public String getTimestamp(){return timestamp;}
    public void setTimestamp(String t){timestamp=t;}

    public String getDistance(){return distance;}
    public void setDistance(String d){distance=d;}

    public String getDistanceSD(){return distanceSD;}
    public void setDistanceSD(String dsd){distanceSD=dsd;}

    public String getPasspoint(){return passpoint;}
    public void setPasspoint(String p){passpoint=p;}

}
