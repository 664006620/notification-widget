package com.example.notificationwidget;

public class NotificationItem {
    public String key;
    public String packageName;
    public String appName;
    public String title;
    public String text;
    public long timestamp;

    public NotificationItem(String key, String packageName, String appName, String title, String text, long timestamp) {
        this.key = key;
        this.packageName = packageName;
        this.appName = appName;
        this.title = title;
        this.text = text;
        this.timestamp = timestamp;
    }
}
