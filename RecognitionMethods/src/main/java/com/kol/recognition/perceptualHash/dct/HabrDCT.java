package com.kol.recognition.perceptualHash.dct;

public final class HabrDCT implements DCT<double[]> {
    @Override
    public double[] dct(double[] x) {
        return new double[0];
    }

    private double[][] c(final int rows, final int cols) {
        final double c[][] = new double[rows][cols];
        for (int n = 0; n < rows; n++) {
            for (int m = 0; m < cols; m++) {
                c[n][m] = Math.sqrt(2.0/rows);
            }
        }
        throw new RuntimeException("Not implemented yet");
    }
}
