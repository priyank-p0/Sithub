package com.example.bottleneck.sithub.database;

import android.provider.BaseColumns;

/**
 * Created by priyankpatel on 10/7/16.
 */
public class SithubContract {




    // To prevent instantiation of the class
    public SithubContract() {}

    /**
     * Inner class defining table for Subscribe List
     */
    public static abstract class WatchListEntry implements BaseColumns {
        public static final String TABLE_NAME_SUBSCRIBE_LIST = "subscribe_list_entry";
        public static final String COLUMN_NAME_IMDB_ID = "imdb_id";
        public static final String COLUMN_NAME_POSTER = "poster";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_AIR_DAY = "air_day";
        public static final String COLUMN_NAME_AIR_TIME = "air_time";
        public static final String COLUMN_NAME_SHOW_NAME = "show_name";
        public static final String COLUMN_NAME_FIRST_AIRED = "first_aired";
    }

}
