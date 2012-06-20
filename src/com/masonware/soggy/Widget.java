package com.masonware.soggy;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {
    @Override
    public void onUpdate( Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds )
    {
        RemoteViews remoteViews = new RemoteViews( context.getPackageName(), R.layout.main );
        ComponentName watchWidget = new ComponentName( context, Widget.class );
        appWidgetManager.updateAppWidget( watchWidget, remoteViews );
    }

}
