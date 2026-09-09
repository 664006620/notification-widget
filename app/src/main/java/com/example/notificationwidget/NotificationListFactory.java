package com.example.notificationwidget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

public class NotificationListFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private int appWidgetId;
    private List<NotificationItem> items;

    public NotificationListFactory(Context context, int appWidgetId) {
        this.context = context;
        this.appWidgetId = appWidgetId;
        items = new ArrayList<>();
    }

    @Override
    public void onCreate() { }

    @Override
    public void onDataSetChanged() {
        items.clear();
        String currentLetter = NotificationWidgetProvider.getCurrentLetter(context, appWidgetId);
        List<NotificationItem> all = NotificationCache.getAll();
        for (NotificationItem item : all) {
            if (currentLetter == null) {
                items.add(item);
            } else {
                String firstLetter = PinyinUtil.getFirstLetter(item.appName);
                if (currentLetter.equalsIgnoreCase(firstLetter)) {
                    items.add(item);
                }
            }
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        NotificationItem item = items.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_notification);
        views.setTextViewText(R.id.app_name, item.appName);
        views.setTextViewText(R.id.title, item.title);
        views.setTextViewText(R.id.text, item.text);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(NotificationWidgetProvider.EXTRA_PACKAGE, item.packageName);
        views.setOnClickFillInIntent(R.id.notification_item, fillInIntent);
        return views;
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
