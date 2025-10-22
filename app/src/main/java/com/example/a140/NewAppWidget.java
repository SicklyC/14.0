package com.example.a140;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

public class NewAppWidget extends AppWidgetProvider {

    private static final String TAG = "NewAppWidget";
    private static final String ACTION_RESTART_WIDGET = "com.example.a140.ACTION_RESTART_WIDGET";


    @SuppressLint("DefaultLocale")
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int currentMinutes = hour * 60 + minute;

        int[][] busTimes = {
                // 14L
                {6, 2}, {6, 15}, {6, 27}, {6, 38}, {6, 49},
                {7, 0}, {7, 11}, {7, 14}, {7, 33}, {7, 44}, {7, 53},
                {8, 6}, {8, 17}, {8, 26}, {8, 37}, {8, 50},
                {9, 1},
                {13, 48},
                {14, 6}, {14, 33}, {14, 44}, {14, 55},
                {15, 6}, {15, 17}, {15, 39}, {15, 50},
                {16, 1}, {16, 12}, {16, 23}, {16, 34}, {16, 45}, {16, 56},
                {17, 7}, {17, 27}, {17, 38},

                // 14
                {9, 51},
                {10, 19}, {10, 33}, {10, 47},
                {11, 1}, {11, 15}, {11, 29}, {11, 43}, {11, 57},
                {12, 11}, {12, 39},
                {17, 17},
                {18, 12}, {18, 23}, {18, 45},
                {19, 2}, {19, 32}, {19, 48},
                {20, 4}, {20, 20}, {20, 36}, {20, 52},
                {21, 5}, {21, 37}, {21, 57},

                // 14P
                {9, 10}, {9, 23}, {9, 37}, {9, 58},
                {17, 50},
                {18, 30},
                {22, 28},

                // 14M
                {5, 10}, {5, 21}, {5, 32}, {5, 43}, {5, 54},
                {12, 25}, {12, 53},
                {13, 4}, {13, 15}, {13, 26}, {13, 37}, {13, 59},
                {14, 8},
                {15, 25},
                {18, 1},
                {19, 16},
                {21, 16},
                {22, 13}
        };

        List<Integer> diffs = new ArrayList<>();
        List<String> upcomingBuses = new ArrayList<>();

        for (int[] bus : busTimes) {
            int busMinutes = bus[0] * 60 + bus[1];
            int diff = busMinutes - currentMinutes;
            if (diff > 0) {
                diffs.add(diff);
                upcomingBuses.add(String.format("%02d:%02d", bus[0], bus[1]));
            }
        }
        for (int i = 0; i < diffs.size() - 1; i++) {
            for (int j = i + 1; j < diffs.size(); j++) {
                if (diffs.get(j) < diffs.get(i)) {
                    int tempDiff = diffs.get(i);
                    diffs.set(i, diffs.get(j));
                    diffs.set(j, tempDiff);

                    String tempBus = upcomingBuses.get(i);
                    upcomingBuses.set(i, upcomingBuses.get(j));
                    upcomingBuses.set(j, tempBus);
                }
            }
        }

        String widgetText;
        if (diffs.size() >= 2) {
            widgetText = "Următorul autobuz: " + upcomingBuses.get(0) + " în "
                    + diffs.get(0) + " min\n"
                    + "Următorul după acesta: " + upcomingBuses.get(1) + " în "
                    + diffs.get(1) + " min";
        } else if (diffs.size() == 1) {
            widgetText = "Următorul autobuz: " + upcomingBuses.get(0) + " în "
                    + diffs.get(0) + " min";
        } else {
            widgetText = "Nu mai sunt autobuze astăzi!";
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setInt(R.id.appwidget_text, "setTextColor",Color.BLACK);

        Intent intent = new Intent(context, NewAppWidget.class);
        intent.setAction(ACTION_RESTART_WIDGET);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.restart_widget_button3, pendingIntent);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.new_app_widget);
        remoteViews.setInt(R.id.appwidget_text, "setBackgroundColor", Color.BLACK);

        appWidgetManager.updateAppWidget(appWidgetId, views);

        Log.d(TAG, "Updated widget " + appWidgetId + " at " + hour + ":" + minute);

        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateAppWidget(context, appWidgetManager, appWidgetId);
                handler.postDelayed(this, 15000); // reapelează la 15 secunde
            }
        };
        handler.postDelayed(runnable, 15000);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_RESTART_WIDGET.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), NewAppWidget.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(TAG, "Widget enabled -> scheduling updates");
        scheduleNextUpdate(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(TAG, "Widget disabled -> cancelling updates");
        Intent intent = new Intent(context, WidgetUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static void scheduleNextUpdate(Context context) {
        Intent intent = new Intent(context, WidgetUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, 1);

        long triggerAt = calendar.getTimeInMillis();

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent);

        Log.d(TAG, "Next update scheduled at " + calendar.getTime().toString());
    }
}
