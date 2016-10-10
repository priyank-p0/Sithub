package com.example.bottleneck.sithub.tvshow;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.bottleneck.sithub.R;

public class ShowDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ShowDetailFragment fragment = new ShowDetailFragment();
        fragment.setArguments(savedInstanceState);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.show_detail_container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent intent = new Intent(this, ShowListActivity.class);
            intent.putExtra(ShowListActivity.EXTRA_SHOW_ID, ShowListActivity.getShowId());
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
