package com.example.notificationwidget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class ConfigureActivity extends AppCompatActivity {
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        seekBar = findViewById(R.id.seekBar);
        int alpha = getAlpha(appWidgetId);
        seekBar.setProgress(alpha);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            int newAlpha = seekBar.getProgress();
            saveAlpha(appWidgetId, newAlpha);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            NotificationWidgetProvider provider = new NotificationWidgetProvider();
            provider.onUpdate(this, appWidgetManager, new int[]{appWidgetId});
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        });
    }

    private int getAlpha(int widgetId) {
        SharedPreferences prefs = getSharedPreferences("widget_prefs", MODE_PRIVATE);
        return prefs.getInt("alpha_" + widgetId, 255);
    }

    private void saveAlpha(int widgetId, int alpha) {
        getSharedPreferences("widget_prefs", MODE_PRIVATE).edit().putInt("alpha_" + widgetId, alpha).apply();
    }
}
