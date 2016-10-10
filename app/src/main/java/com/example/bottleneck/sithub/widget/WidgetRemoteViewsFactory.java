package com.example.bottleneck.sithub.widget;

import android.content.Context;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bottleneck.sithub.R;
import com.example.bottleneck.sithub.database.SithubContract;
import com.example.bottleneck.sithub.database.SithubDatabaseHelper;

import java.util.ArrayList;

/**
 * Created by priyankpatel on 10/9/16.
 */
public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context Context;
     static private ArrayList<Show> shows;
        int ctr;
    private SithubDatabaseHelper mDbHelper;
    public WidgetRemoteViewsFactory(Context mContext) {
        this.Context = mContext;
        this.shows = new ArrayList<>();
    }
    public void getData()
    {
        mDbHelper = new SithubDatabaseHelper(Context);
        Cursor c = mDbHelper.getWatchList();
        int n=c.getColumnCount();
        ctr=1;
        if(c!=null){
            c.moveToFirst();
            do{
                Show sh=new Show();
                ctr++;
                sh.setTitle(c.getString(c.getColumnIndex(SithubContract.WatchListEntry.COLUMN_NAME_SHOW_NAME)));
  //              int ctr= Integer.parseInt(SithubContract.WatchListEntry._ID);
                shows.add(sh);
             //   sh=null;
            }while (c.moveToNext());
        }
        c.close();


    }
    @Override
    public void onCreate() {
        getData();

    }

    @Override
    public void onDataSetChanged() {
        getData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return shows.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        int p=position;
        Show show= shows.get(position);
        final RemoteViews remoteViews = new RemoteViews(
                Context.getPackageName(), R.layout.widget_individual);

       // final RemoteViews remoteViews = new RemoteViews(Context.getPackageName(), R.layout.widget_individual);
        remoteViews.setTextViewText(R.id.showTitle, show.getTitle());

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
