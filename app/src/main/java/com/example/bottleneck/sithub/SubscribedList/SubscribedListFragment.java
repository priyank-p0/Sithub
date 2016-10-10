package com.example.bottleneck.sithub.SubscribedList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.example.bottleneck.sithub.MainActivity;
import com.example.bottleneck.sithub.R;
import com.example.bottleneck.sithub.SplashActivity;
import com.example.bottleneck.sithub.database.SithubContract;
import com.example.bottleneck.sithub.database.SithubDatabaseHelper;
import com.example.bottleneck.sithub.search.SearchFragment;
import com.example.bottleneck.sithub.traktapi.Show;
import com.example.bottleneck.sithub.tvshow.ShowListActivity;
import com.example.bottleneck.sithub.util.MyApplication;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeMap;

/**
 * Created by priyankpatel on 10/7/16.
 */
public class SubscribedListFragment extends Fragment {

        private Adapter mWatchListAdapter;
        private ArrayList<SubscribedListGroup> mWatchListItems;
        private ExpandableListView mExpandableList;

        /**
         * Database helper object
         */
        private SithubDatabaseHelper mDbHelper;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (!MyApplication.getInstance().isShowCalendarUpdated()) {
                Intent intent = new Intent(getActivity(), SplashActivity.class);
                intent.putExtra(SplashActivity.EXTRA_LAUNCH_ACTIVITY, SplashActivity.EXTRA_ACTIVITY_MAIN);
                startActivity(intent);
                getActivity().finish();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            mDbHelper = new SithubDatabaseHelper(getActivity());

            Cursor c = mDbHelper.getWatchList();

            if (c.getCount() > 0) {
                View inflatedView = inflater.inflate(R.layout.fragment_subscribed_list,
                        container, false);
                mExpandableList = (ExpandableListView) inflatedView
                        .findViewById(R.id.subscribe_list_expandable);

                mWatchListItems = createWatchListItems(c);

                setupExpandableList();

                return inflatedView;
            } else {
                View inflatedView = null;
                inflatedView = inflater.inflate(R.layout.fragment_subscribed_list_empty,
                        container, false);
                Button btnAddShow = (Button) inflatedView.findViewById(R.id.btnAdd);
                btnAddShow.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                       // ((ActionBar Activity)getActivity()).getSupportActionBar().setTitle(getString(R.string.search_activity_title));

                       // getActivity().getActionBar().setTitle(getString(R.string.search_activity_title));
                        MainActivity.mDrawerList.setItemChecked(
                                MainActivity.SCREEN_SEARCH, true);
                        SearchFragment fragment = new SearchFragment();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.content_frame, fragment)
                                .commit();
                    }
                });
                return inflatedView;
            }
        }

        private ArrayList<SubscribedListGroup> createWatchListItems(Cursor c) {
            ArrayList<SubscribedListGroup> list = new ArrayList<SubscribedListGroup>();

            TreeMap<String, SubscribedListGroup> groups = new TreeMap<String, SubscribedListGroup>();

            while(c.moveToNext()) {
                Show show = new Show();
                // get the info from the row
                show.setTitle(c.getString(c.getColumnIndex(SithubContract.WatchListEntry.COLUMN_NAME_SHOW_NAME)));

                String firstLetter = show.getTitle().substring(0, 1).toUpperCase(Locale.ENGLISH);	// used for expandable list headers

                show.setPosterUrl(c.getString(c.getColumnIndex(SithubContract.WatchListEntry.COLUMN_NAME_POSTER)));
                show.setStatus(c.getString(c.getColumnIndex(SithubContract.WatchListEntry.COLUMN_NAME_STATUS)));
                show.setAirTime(c.getString(c.getColumnIndex(SithubContract.WatchListEntry.COLUMN_NAME_AIR_TIME)));
                show.setAirDay(c.getString(c.getColumnIndex(SithubContract.WatchListEntry.COLUMN_NAME_AIR_DAY)));
                show.setImdbId(c.getString(c.getColumnIndex(SithubContract.WatchListEntry.COLUMN_NAME_IMDB_ID)));
                show.setFirstAired(new DateTime(c.getString(c.getColumnIndex(SithubContract.WatchListEntry.COLUMN_NAME_FIRST_AIRED))));
                show.setOnAir(MyApplication.getInstance().getShowCalendarIds().contains(show.getImdbId()));

                Listchild child = new Listchild();
                child.setApiId(show.getImdbId());
                child.setImage(show.getPosterUrl());
                child.setName(show.getTitle());
                child.setShowTime(show.makeShowTimeString(getActivity()));

                SubscribedListGroup SubscribedListGroup;

                if (groups.containsKey(firstLetter)) {
                    SubscribedListGroup = groups.get(firstLetter);

                    ArrayList<Listchild> watchListItems = SubscribedListGroup
                            .getItems();
                    watchListItems.add(child);

                    SubscribedListGroup.setItems(watchListItems);
                    groups.put(firstLetter, SubscribedListGroup);
                    list.set(list.indexOf(SubscribedListGroup), SubscribedListGroup);
                } else {
                    SubscribedListGroup = new SubscribedListGroup();
                    SubscribedListGroup.setName(firstLetter);

                    ArrayList<Listchild> watchListItems = new ArrayList<Listchild>();
                    watchListItems.add(child);

                    SubscribedListGroup.setItems(watchListItems);

                    groups.put(firstLetter, SubscribedListGroup);
                    list.add(SubscribedListGroup);
                }
            }
            return list;
        }

    private void setupExpandableList() {
        mWatchListAdapter = new Adapter(getActivity(),
                mWatchListItems);
        mExpandableList.setAdapter(mWatchListAdapter);
        mExpandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                SubscribedListGroup group = mWatchListItems.get(groupPosition);
                ArrayList<Listchild> child = group.getItems();
                Listchild show = child.get(childPosition);

                Intent intent = new Intent(getActivity(), ShowListActivity.class);
                intent.putExtra(ShowListActivity.EXTRA_SHOW_ID, show.getApiId());
                startActivity(intent);

                return false;
            }
        });
    }
}
