package com.kol.recognition.perceptualHash.distance;

import org.apache.commons.lang3.StringUtils;

public final class JaroWinklerDistance implements StringDistance {
    @Override
    public double getDistance(final String first, final String second) {
        return StringUtils.getJaroWinklerDistance(first, second);
    }
}
