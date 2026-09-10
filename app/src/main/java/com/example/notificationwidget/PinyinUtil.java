package com.example.notificationwidget;

import android.icu.text.Transliterator;

public class PinyinUtil {
    public static String getFirstLetter(String input) {
        if (input == null || input.isEmpty()) return "#";
        char firstChar = input.charAt(0);
        if (Character.isLetter(firstChar)) {
            if (String.valueOf(firstChar).matches("[a-zA-Z]")) {
                return String.valueOf(Character.toUpperCase(firstChar));
            } else {
                try {
                    Transliterator transliterator = Transliterator.getInstance("Han-Latin");
                    String pinyin = transliterator.transform(String.valueOf(firstChar));
                    if (pinyin != null && !pinyin.isEmpty()) {
                        return String.valueOf(Character.toUpperCase(pinyin.charAt(0)));
                    }
                } catch (Exception e) {
                    // 忽略转换错误
                }
                return "#";
            }
        }
        return "#";
    }
}
