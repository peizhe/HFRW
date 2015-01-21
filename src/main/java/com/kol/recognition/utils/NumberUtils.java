package com.kol.recognition.utils;

import java.util.Base64;

public final class NumberUtils {

    private NumberUtils(){}

    public static String encode(final int value) {
        return Base64.getEncoder().encodeToString(String.valueOf(value).getBytes());
    }

    public static int decode(final String value) {
        return Integer.valueOf(new String(Base64.getDecoder().decode(value)));
    }
}
