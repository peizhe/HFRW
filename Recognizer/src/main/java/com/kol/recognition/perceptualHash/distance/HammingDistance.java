package com.kol.recognition.perceptualHash.distance;

import com.kol.recognition.utils.Utils;

public final class HammingDistance implements StringDistance {

    /**
     * Compute the Hamming distance between the two strings <code>s1</code> and
     * <code>s2</code>. The two strings to be computed must be of equal length
     * and the Hamming distance is defined to be the number of positions where
     * the characters are different.
     *
     * @return The Hamming distance
     */
    @Override
    public double getDistance(final String first, final String second) {
        if (null == first || null == second) {
            throw new IllegalArgumentException();
        }
        final String f, s;
        if(first.length() != second.length()) {
            final int max = Math.max(first.length(), second.length());
            f = Utils.leadingZeros(first, max);
            s = Utils.leadingZeros(second, max);
        } else {
            f = first;
            s = second;
        }
        int distance = 0;
        for (int i = 0; i < f.length(); i++) {
            if (f.charAt(i) != s.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }
}
