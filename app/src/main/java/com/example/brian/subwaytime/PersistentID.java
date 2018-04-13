package com.example.brian.subwaytime;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;

/*
* generates a persistent ID using Firebase's FirebasePersistentID
* as it turns out, Google Play API's InstanceID is depreciated, hence this
* this apparently survives app restarts, so i guess this works
*
* note: class structure follows AppDatabase
* */
public abstract class PersistentID {
    private static String PERSISTENT_ID;

    public static String get_id(){
        if(PERSISTENT_ID==null){
            PERSISTENT_ID = FirebaseInstanceId.getInstance().getId();
        }
        return PERSISTENT_ID;
    }
}
