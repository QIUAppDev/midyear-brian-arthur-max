package com.example.brian.subwaytime;

import android.provider.BaseColumns;

/** Contract class that defines the schema of the sql table we'll be using
 * https://developer.android.com/training/data-storage/sqlite.html#DefineContract
 * Created by vru11 on 12/9/2017.
 */

public final class Contract {
    // To prevent someone from accidentally instantiating the Contract class,
    // make the constructor private.
    private Contract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

}
