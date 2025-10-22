package com.example.a140;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new android.content.ComponentName(context, NewAppWidget.class));

            for (int appWidgetId : appWidgetIds) {
                NewAppWidget.updateAppWidget(context, appWidgetManager, appWidgetId);
            }

            // Reschedule updates (dacă vrei periodic)
            NewAppWidget.scheduleNextUpdate(context);
        }
    }
}
