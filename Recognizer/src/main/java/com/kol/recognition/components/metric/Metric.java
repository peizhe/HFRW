package com.kol.recognition.components.metric;

import Jama.Matrix;

public interface Metric {
    double getDistance(Matrix a, Matrix b);
}