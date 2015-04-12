package com.kol.recognition.utils;

public final class Utils {

    private Utils(){}

    public static String leadingZeros(final String str, final int length) {
        if (str.length() >= length) {
            return str;
        } else {
            return String.format("%0" + (length - str.length()) + "d%s", 0, str);
        }
    }
}
