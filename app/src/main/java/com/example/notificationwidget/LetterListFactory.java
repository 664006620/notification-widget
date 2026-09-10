package com.example.notificationwidget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

public class LetterListFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<String> letters;

    public LetterListFactory(Context context) {
        this.context = context;
        letters = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            letters.add(String.valueOf(c));
        }
    }

    @Override
    public void onCreate() { }

    @Override
    public void onDataSetChanged() { }

    @Override
    public void onDestroy() { }

    @Override
    public int getCount() {
        return letters.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        String letter = letters.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_letter);
        views.setTextViewText(R.id.letter_button, letter);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(NotificationWidgetProvider.EXTRA_LETTER, letter);
        views.setOnClickFillInIntent(R.id.letter_button, fillInIntent);
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
