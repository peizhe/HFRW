package com.kol.recognition.interfaces;

import Jama.Matrix;

public interface Metric {
    double getDistance(Matrix a, Matrix b);
}