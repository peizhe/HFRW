package com.kol.recognition.components.interfaces;

import Jama.Matrix;

public interface Metric {
    double getDistance(Matrix a, Matrix b);
}