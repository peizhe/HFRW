package com.kol.recognition.components.metric;

import Jama.Matrix;

public final class CosineDissimilarity implements Metric {

    @Override
    public double getDistance(final Matrix a, final Matrix b) {
        assert a.getRowDimension() == b.getRowDimension();
        final int size = a.getRowDimension();

        // get s * e
        double se = 0;
        for (int i = 0; i < size; i++) {
            se += a.get(i, 0) * b.get(i, 0);
        }

        // get s norm
        double sNorm = 0;
        for (int i = 0; i < size; i++) {
            sNorm += Math.pow(a.get(i, 0), 2);
        }
        sNorm = Math.sqrt(sNorm);

        // get e norm
        double eNorm = 0;
        for (int i = 0; i < size; i++) {
            eNorm += Math.pow(b.get(i, 0), 2);
        }
        eNorm = Math.sqrt(eNorm);

        final double cosine = Math.abs(se) / (eNorm * sNorm);
        // transform cosine similarity into dissimilarity such that this is
        // unified with EuclideanDistance and L1Distance
        return cosine == 0.0 ? Double.MAX_VALUE : 1 / cosine;
    }
}