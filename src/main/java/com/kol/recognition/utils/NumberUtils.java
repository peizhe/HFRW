package com.kol.recognition.utils;

import java.util.Base64;

public final class NumberUtils {

    private NumberUtils(){}

    public static String encode(final long value) {
        return Base64.getEncoder().encodeToString(String.valueOf(value).getBytes());
    }

    public static long decode(final String value) {
        return Long.valueOf(new String(Base64.getDecoder().decode(value)));
    }

    public static synchronized String generateId() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException ignored) {}
        return encode(System.nanoTime());
    }
}
