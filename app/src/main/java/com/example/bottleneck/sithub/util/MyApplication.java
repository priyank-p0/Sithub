package com.example.bottleneck.sithub.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.bottleneck.sithub.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by priyankpatel on 10/7/16.
 */
public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();

    public static final String SHARED_PREFERENCES_FILE = "mytvtracker_shared_prefs";
    public static final String KEY_SHOW_CAL_LAST_UPDATED = "show_cal_last_updated";

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private ArrayList<String> mShowCalendarIds;

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new BItmapcache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public HashMap<String, String> getTraktHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("trakt-api-version", "2");
        headers.put("trakt-api-key", getString(R.string.trakt_client_id));
        return headers;
    }

    public ArrayList<String> getShowCalendarIds() {
        if (mShowCalendarIds == null) {
            mShowCalendarIds = new ArrayList<>();
        }
        return mShowCalendarIds;
    }

    public void setShowCalendarIds(ArrayList<String> ids) {
        mShowCalendarIds = ids;
    }

    public boolean isShowCalendarUpdated() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        String lastUpdatedString = preferences.getString(KEY_SHOW_CAL_LAST_UPDATED, null);
        if (lastUpdatedString == null) {
            return false;
        } else {
            DateTime now = new DateTime();
            DateTime lastUpdated = new DateTime(lastUpdatedString);
            Period period = new Period(lastUpdated, now);
            return !(period.getDays() > 1);
        }
    }

}
