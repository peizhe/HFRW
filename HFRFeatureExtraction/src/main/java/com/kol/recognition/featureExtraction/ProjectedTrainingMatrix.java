package com.kol.recognition.featureExtraction;

import Jama.Matrix;

public final class ProjectedTrainingMatrix {

    private String label;
    private Matrix matrix;
    private double distance = 0;

    public ProjectedTrainingMatrix(String label, Matrix matrix) {
        this.label = label;
        this.matrix = matrix;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}