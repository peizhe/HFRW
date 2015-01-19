package com.kol.recognition.web.utils;

public class Utils {
    public static String leadingZeros(final String str, final int length) {
        if (str.length() >= length) {
            return str;
        } else {
            return String.format("%0" + (length- str.length()) + "d%s", 0, str);
        }
    }

    public static String leadingZeros(final int src, final int length) {
        return leadingZeros(String.valueOf(src), length);
    }
}
