package com.example.bottleneck.sithub.tvshow;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bottleneck.sithub.R;

/**
 * Created by priyankpatel on 10/7/16.
 */
public class ShowListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShowListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] items = new String[ShowListActivity.getNumberOfSeasons() + 1];
        // first menu item is for show info
        items[0] = getString(R.string.activity_title_summary);
        for (int i = 1; i < items.length; i++) {
            items[i] = "Season " + i;
        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState
                    .getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position,
                                long id) {
        super.onListItemClick(listView, view, position, id);
        if (position == 0) {
            if (ShowListActivity.isTwoPane()) {
                ShowDetailFragment fragment = new ShowDetailFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.show_detail_container, fragment).commit();
            } else {
                Intent intent = new Intent(getActivity(), ShowDetailActivity.class);
                getActivity().startActivity(intent);
            }
        } else {
            if (ShowListActivity.isTwoPane()) {
                SeasonEpisodeFragment fragment = new SeasonEpisodeFragment();
                fragment.setSeason(position);
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.show_detail_container, fragment).commit();
            } else {
                Intent intent = new Intent(getActivity(), SeasonEpisodeActivity.class);
                intent.putExtra(SeasonEpisodeActivity.EXTRA_SEASON, position);
                getActivity().startActivity(intent);
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(
                activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
                        : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public void onStart() {
        super.onStart();

        //set first item activated by default, only for two pane mode
        if (ShowListActivity.isTwoPane()) {
            onListItemClick(getListView(), getView(), 0, 0);
        }

    }
}

