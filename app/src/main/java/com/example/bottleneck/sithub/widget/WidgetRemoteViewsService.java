package com.example.bottleneck.sithub.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by priyankpatel on 10/9/16.
 */
public class WidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext()); //returning the new object of remoteviewsfactory
    }
}
