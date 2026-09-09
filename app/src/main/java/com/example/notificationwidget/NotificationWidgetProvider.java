package com.example.notificationwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.RemoteViews;

public class NotificationWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_LETTER_CLICK = "com.example.notificationwidget.LETTER_CLICK";
    public static final String ACTION_OPEN_APP = "com.example.notificationwidget.OPEN_APP";
    public static final String EXTRA_LETTER = "letter";
    public static final String EXTRA_PACKAGE = "package";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            Intent notificationIntent = new Intent(context, WidgetRemoteViewsService.class);
            notificationIntent.setAction(WidgetRemoteViewsService.ACTION_NOTIFICATION_LIST);
            notificationIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            views.setRemoteAdapter(R.id.notification_list, notificationIntent);

            Intent letterIntent = new Intent(context, WidgetRemoteViewsService.class);
            letterIntent.setAction(WidgetRemoteViewsService.ACTION_LETTER_LIST);
            letterIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            views.setRemoteAdapter(R.id.letter_grid, letterIntent);

            Intent letterClickIntent = new Intent(context, NotificationWidgetProvider.class);
            letterClickIntent.setAction(ACTION_LETTER_CLICK);
            letterClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent letterPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, letterClickIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            views.setPendingIntentTemplate(R.id.letter_grid, letterPendingIntent);

            Intent openAppIntent = new Intent(context, NotificationWidgetProvider.class);
            openAppIntent.setAction(ACTION_OPEN_APP);
            PendingIntent openAppPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            views.setPendingIntentTemplate(R.id.notification_list, openAppPendingIntent);

            int alpha = getAlpha(context, appWidgetId);
            views.setInt(R.id.widget_background, "setBackgroundColor", Color.argb(alpha, 0, 0, 0));

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_LETTER_CLICK.equals(intent.getAction())) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            String letter = intent.getStringExtra(EXTRA_LETTER);
            if (appWidgetId != -1 && letter != null) {
                saveCurrentLetter(context, appWidgetId, letter);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.notification_list);
            }
        } else if (ACTION_OPEN_APP.equals(intent.getAction())) {
            String packageName = intent.getStringExtra(EXTRA_PACKAGE);
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(launchIntent);
            }
        }
    }

    private void saveCurrentLetter(Context context, int widgetId, String letter) {
        SharedPreferences prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE);
        prefs.edit().putString("letter_" + widgetId, letter).apply();
    }

    public static String getCurrentLetter(Context context, int widgetId) {
        SharedPreferences prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE);
        return prefs.getString("letter_" + widgetId, null);
    }

    private int getAlpha(Context context, int widgetId) {
        SharedPreferences prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE);
        return prefs.getInt("alpha_" + widgetId, 255);
    }
}
