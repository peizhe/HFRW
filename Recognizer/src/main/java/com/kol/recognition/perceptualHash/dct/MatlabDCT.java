package com.kol.recognition.perceptualHash.dct;

public final class MatlabDCT implements DCT<double[][]> {
    @Override
    public double[][] dct(double[][] x) {
        final int rows = x.length;
        final int columns = x[0].length;

        final double[] ap = new double[rows];
        ap[0] = 1.0 / Math.sqrt(rows);
        for (int i = 1; i < rows; i++) {
            ap[i] = Math.sqrt(2.0/rows);
        }
        final double[] aq = new double[columns];
        aq[0] = 1.0 / Math.sqrt(columns);
        for (int i = 1; i < columns; i++) {
            aq[i] = Math.sqrt(2.0/columns);
        }

        final double result[][] = new double[rows][columns];

        for (int k1 = 0; k1 < rows; k1++) {
            for (int k2 = 0; k2 < columns; k2++) {
                double sum = 0;
                for (int n1 = 0; n1 < rows; n1++) {
                    for (int n2 = 0; n2 < columns; n2++) {
                        sum += x[n1][n2] *
                                Math.cos((Math.PI * (2*n1 + 1) * k1) / (2.0 * rows)) *
                                Math.cos((Math.PI * (2*n2 + 1) * k2) / (2.0 * columns));
                    }
                }
                result[k1][k2] = ap[k1] * aq[k2] * sum;
            }
        }
        return result;
    }
}
