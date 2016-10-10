package com.example.bottleneck.sithub.widget;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.bottleneck.sithub.R;

/**
 * Created by priyankpatel on 10/9/16.
 */
public class WidgetProvider extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int widgetId : appWidgetIds) {
            Intent remoteViewServiceIntent = new Intent(context, WidgetRemoteViewsService.class);
            remoteViewServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            remoteViewServiceIntent.setData(Uri.parse(remoteViewServiceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_individual);
            remoteViews.setRemoteAdapter(widgetId, R.id.widget_listview, remoteViewServiceIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }
    }
}