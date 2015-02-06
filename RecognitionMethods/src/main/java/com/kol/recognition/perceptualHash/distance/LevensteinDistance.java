package com.kol.recognition.perceptualHash.distance;

import org.apache.commons.lang3.StringUtils;

public final class LevensteinDistance implements StringDistance {
    @Override
    public double getDistance(final String first, final String second) {
        return StringUtils.getLevenshteinDistance(first, second);
    }
}
