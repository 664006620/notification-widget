package com.example.notificationwidget;

import android.app.Notification;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NLService extends NotificationListenerService {

    @Override
    public void onCreate() {
        super.onCreate();
        StatusBarNotification[] activeNotifs = getActiveNotifications();
        for (StatusBarNotification sbn : activeNotifs) {
            addToCache(sbn);
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        addToCache(sbn);
        updateWidgets();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        NotificationCache.remove(sbn.getKey());
        updateWidgets();
    }

    private void addToCache(StatusBarNotification sbn) {
        String key = sbn.getKey();
        String packageName = sbn.getPackageName();
        Notification notification = sbn.getNotification();
        Bundle extras = notification.extras;
        String title = extras.getString(Notification.EXTRA_TITLE, "");
        String text = extras.getString(Notification.EXTRA_TEXT, "");
        String appName = getAppName(packageName);
        long when = notification.when;
        NotificationItem item = new NotificationItem(key, packageName, appName, title, text, when);
        NotificationCache.add(item);
    }

    private void updateWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName thisWidget = new ComponentName(this, NotificationWidgetProvider.class);
        int[] ids = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int id : ids) {
            appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.notification_list);
        }
    }

    private String getAppName(String packageName) {
        PackageManager pm = getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
            return pm.getApplicationLabel(info).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return packageName;
        }
    }
}
