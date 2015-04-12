package com.kol.recognition.perceptualHash.dct;

public final class ScalaDCT implements DCT<double[][]> {
    @Override
    public double[][] dct(double[][] x) {
        return com.kol.dct.DCT.dct(x);
    }
}