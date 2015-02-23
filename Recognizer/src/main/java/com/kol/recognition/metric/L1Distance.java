package com.kol.recognition.metric;

import Jama.Matrix;
import com.kol.recognition.interfaces.Metric;

public final class L1Distance implements Metric {

    @Override
    public double getDistance(final Matrix a, final Matrix b) {
        double sum = 0;
        for (int i = 0; i < a.getRowDimension(); i++) {
            sum += Math.abs(a.get(i, 0) - b.get(i, 0));
        }
        return sum;
    }
}