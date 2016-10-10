package com.example.bottleneck.sithub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.example.bottleneck.sithub.traktapi.TraktapiHelper;
import com.example.bottleneck.sithub.tvshow.ShowListActivity;
import com.example.bottleneck.sithub.util.CustomRequest;
import com.example.bottleneck.sithub.util.MyApplication;

import org.joda.time.DateTime;
import org.json.JSONException;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    public static final String EXTRA_LAUNCH_ACTIVITY = "launch_activity";
    public static final int EXTRA_ACTIVITY_MAIN = 1;
    public static final int EXTRA_ACTIVITY_SHOW_DETAIL = 2;
    public static final String EXTRA_SHOW_ID = "show_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = getIntent();
        int activityToLaunch = intent.getIntExtra(EXTRA_LAUNCH_ACTIVITY, 0);
        if (activityToLaunch == 0) {
            if (MyApplication.getInstance().isShowCalendarUpdated()) {
                launchActivity(EXTRA_ACTIVITY_MAIN);
            } else {
                updateShowCalendarAndLaunchActivity(EXTRA_ACTIVITY_MAIN);
            }
        } else {
            updateShowCalendarAndLaunchActivity(activityToLaunch);
        }
    }

    private void launchActivity(int activityToLaunch) {
        if (activityToLaunch == EXTRA_ACTIVITY_MAIN) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (activityToLaunch == EXTRA_ACTIVITY_SHOW_DETAIL) {
            Intent intent = new Intent(SplashActivity.this, ShowListActivity.class);
            intent.putExtra(ShowListActivity.EXTRA_SHOW_ID, (String) getIntent().getExtras().get(EXTRA_SHOW_ID));
            startActivity(intent);
        }
        finish();
    }

    private void updateShowCalendarAndLaunchActivity(final int activityToLaunch) {
        int numDays = 7;
        String query = TraktapiHelper.getShowCalendar(numDays);
        final String requestTag = "update_show_calendar_ids";

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.v(requestTag, "Error: " + volleyError.getMessage());
            }
        };

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String resultString) {
                try {
                    ArrayList<String> ids = TraktapiHelper.buildIdListFromCalendar(resultString);
                    MyApplication.getInstance().setShowCalendarIds(ids);
                    SharedPreferences.Editor editor = getSharedPreferences(MyApplication.SHARED_PREFERENCES_FILE, MODE_PRIVATE).edit();
                    editor.putString(MyApplication.KEY_SHOW_CAL_LAST_UPDATED, new DateTime().toString());
                    editor.apply();
                    launchActivity(activityToLaunch);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        CustomRequest request = new CustomRequest(Request.Method.GET, query, responseListener, errorListener, MyApplication.getInstance().getTraktHeaders());
        MyApplication.getInstance().addToRequestQueue(request, requestTag);
    }

}
