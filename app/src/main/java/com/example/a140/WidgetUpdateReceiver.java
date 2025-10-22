package com.example.a140;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WidgetUpdateReceiver extends BroadcastReceiver {
    private static final String TAG = "WidgetUpdateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received update intent -> refreshing widget");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, NewAppWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int appWidgetId : appWidgetIds) {
            NewAppWidget.updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        // Reschedule next update
        NewAppWidget.scheduleNextUpdate(context);
    }
}
