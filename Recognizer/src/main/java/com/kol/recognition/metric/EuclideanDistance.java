package com.kol.recognition.metric;

import Jama.Matrix;
import com.kol.recognition.interfaces.Metric;

public final class EuclideanDistance implements Metric {

    @Override
    public double getDistance(final Matrix a, final Matrix b) {
        double sum = 0;
        for (int i = 0; i < a.getRowDimension(); i++) {
            sum += Math.pow(a.get(i, 0) - b.get(i, 0), 2);
        }
        return Math.sqrt(sum);
    }
}