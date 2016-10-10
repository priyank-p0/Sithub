package com.example.bottleneck.sithub.tvshow;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.bottleneck.sithub.R;
import com.example.bottleneck.sithub.database.SithubDatabaseHelper;
import com.example.bottleneck.sithub.traktapi.Show;
import com.example.bottleneck.sithub.traktapi.TraktapiHelper;
import com.example.bottleneck.sithub.util.CustomRequest;
import com.example.bottleneck.sithub.util.MyApplication;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;

/**
 * Created by priyankpatel on 10/7/16.
 */
public class ShowDetailFragment extends Fragment {

    private boolean mIsOnWatchList;
    private Show mShow;
    private AdView mAdView;

    private static final String BUNDLE_SHOW_OBJECT = "show";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShowDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_show_detail,
                container, false);

        if ( (savedInstanceState != null) && savedInstanceState.getSerializable(BUNDLE_SHOW_OBJECT) != null) {
            mShow = (Show) savedInstanceState.getSerializable(BUNDLE_SHOW_OBJECT);
            populateUi(rootView);
        } else {
            final String requestTag = "get_show";
            String query = TraktapiHelper.getShowQuery(ShowListActivity.getShowId());

            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getString(R.string.show_retrieving));
            dialog.setCancelable(false);
            dialog.show();

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.d(requestTag, "Error: " + volleyError.getMessage());
                    dialog.dismiss();
                }
            };

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String stringResponse) {
                    try {
                        mShow = TraktapiHelper.getShowFromResult(stringResponse);
                        populateUi(rootView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        dialog.dismiss();
                    }
                }
            };

            CustomRequest request = new CustomRequest(Request.Method.GET, query, responseListener, errorListener, MyApplication.getInstance().getTraktHeaders());
            MyApplication.getInstance().addToRequestQueue(request, requestTag);
        }

        // set action bar title
        getActivity().setTitle("Summary");
        mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        return rootView;
    }
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_SHOW_OBJECT, mShow);
    }

    private void populateUi(View rootView) {

        ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
        NetworkImageView imgPoster = (NetworkImageView) rootView.findViewById(R.id.imgPoster);
        imgPoster.setImageUrl(mShow.getPosterUrl(), imageLoader);

        TextView lblShowName = (TextView) rootView.findViewById(R.id.lblShowName);
        lblShowName.setText(mShow.getTitle());

        TextView lblShowYear = (TextView) rootView.findViewById(R.id.lblShowYear);
        String year = (mShow.getYear() == 0) ? getString(R.string.show_year_unknown) : Integer.toString(mShow.getYear());
        lblShowYear.setText(year);

        TextView lblShowTime = (TextView) rootView.findViewById(R.id.lblShowTime);
        lblShowTime.setText(mShow.makeShowTimeString(getActivity()));

        TextView lblShowNetwork = (TextView) rootView.findViewById(R.id.lblShowNetwork);
        lblShowNetwork.setText(mShow.getNetwork());

        TextView lblShowCountry = (TextView) rootView.findViewById(R.id.lblShowCountry);
        lblShowCountry.setText(mShow.getCountry());

        TextView lblFirstAired = (TextView) rootView.findViewById(R.id.lblFirstAired);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMMM d, yyyy");
        String firstAired = "First aired: ";
        firstAired = firstAired + mShow.getFirstAired().toString(dtf);
        lblFirstAired.setText(firstAired);

        final Button btnWatchList = (Button) rootView.findViewById(R.id.btnWatchList);
        mIsOnWatchList = isOnWatchList(mShow.getImdbId());
        String buttonText = mIsOnWatchList ? getString(R.string.watchlist_remove) : getString(R.string.watchlist_add);
        btnWatchList.setText(buttonText);
        btnWatchList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SithubDatabaseHelper db = new SithubDatabaseHelper(getActivity());
                if (mIsOnWatchList) {
                    String[] ids = { mShow.getImdbId() };
                    db.removeFromWatchList(ids);
                    Toast.makeText(getActivity(), getString(R.string.watchlist_removed), Toast.LENGTH_LONG).show();
                    btnWatchList.setText(getString(R.string.watchlist_add));
                    mIsOnWatchList = false;
                } else {
                    db.addToWatchList(mShow);
                    Toast.makeText(getActivity(), getString(R.string.watchlist_added), Toast.LENGTH_LONG).show();
                    btnWatchList.setText(getString(R.string.watchlist_remove));
                    mIsOnWatchList = true;
                }
            }
        });

        TextView lblOverviewBody = (TextView) rootView.findViewById(R.id.lblOverviewBody);
        lblOverviewBody.setText(mShow.getOverview());
    }

    private boolean isOnWatchList(String showId) {
       SithubDatabaseHelper db = new SithubDatabaseHelper(getActivity());
        return db.isOnWatchList(showId);
    }
}
