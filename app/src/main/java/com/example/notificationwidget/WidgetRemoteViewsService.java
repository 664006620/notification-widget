package com.example.notificationwidget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetRemoteViewsService extends RemoteViewsService {
    public static final String ACTION_NOTIFICATION_LIST = "notification_list";
    public static final String ACTION_LETTER_LIST = "letter_list";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        String action = intent.getAction();
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        if (ACTION_NOTIFICATION_LIST.equals(action)) {
            return new NotificationListFactory(this.getApplicationContext(), appWidgetId);
        } else if (ACTION_LETTER_LIST.equals(action)) {
            return new LetterListFactory(this.getApplicationContext());
        }
        return null;
    }
}
