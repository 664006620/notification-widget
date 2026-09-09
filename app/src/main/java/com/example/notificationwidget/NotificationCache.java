package com.example.notificationwidget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NotificationCache {
    private static final Map<String, NotificationItem> notifications = new LinkedHashMap<>();

    public static synchronized void add(NotificationItem item) {
        notifications.put(item.key, item);
    }

    public static synchronized void remove(String key) {
        notifications.remove(key);
    }

    public static synchronized List<NotificationItem> getAll() {
        return new ArrayList<>(notifications.values());
    }
}
