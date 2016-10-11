package com.example.bottleneck.sithub;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.bottleneck.sithub.database.SithubDatabaseHelper;

import java.util.HashMap;

/**
 * Created by priyankpatel on 10/11/16.
 */
public class ShowProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.provider.Show";
    static final String URL = "content://" + PROVIDER_NAME + "/episode";
    static final Uri CONTENT_URI = Uri.parse(URL);
    public static final String _ID = "_id";
    public static final String TABLE_NAME_SUBSCRIBE_LIST = "subscribe_list_entry";
    public static final String COLUMN_NAME_IMDB_ID = "imdb_id";
    public static final String COLUMN_NAME_POSTER = "poster";
    public static final String COLUMN_NAME_STATUS = "status";
    public static final String COLUMN_NAME_AIR_DAY = "air_day";
    public static final String COLUMN_NAME_AIR_TIME = "air_time";
    public static final String COLUMN_NAME_SHOW_NAME = "show_name";
    public static final String COLUMN_NAME_FIRST_AIRED = "first_aired";

    private static HashMap<String, String> SHOWS_PROJECTION_MAP;
    private SithubDatabaseHelper db=null;
    static final int SHOWS = 1;
    static final int SHOW_ID = 2;
    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "shows", SHOWS);
        uriMatcher.addURI(PROVIDER_NAME, "shows/#", SHOW_ID);
    }
    @Override
    public boolean onCreate( ) {
        Context context=getContext();
        db=new SithubDatabaseHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String id = null;

        return db.getWatchList();
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == SHOW_ID) {
            //Delete is for one single image. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }

         db.removeFromWatchList(new String[]{id});
        return 1;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
