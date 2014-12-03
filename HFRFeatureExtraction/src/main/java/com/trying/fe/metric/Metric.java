package com.trying.fe.metric;

import Jama.Matrix;

public interface Metric {
    double getDistance(Matrix a, Matrix b);
}