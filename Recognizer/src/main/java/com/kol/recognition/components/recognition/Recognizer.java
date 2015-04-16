package com.kol.recognition.components.recognition;

import Jama.Matrix;
import com.google.common.collect.Multimap;
import com.kol.recognition.components.beans.AnalysisSettings;
import com.kol.recognition.components.beans.Mix;
import com.kol.recognition.components.beans.ProjectedTrainingMatrix;
import com.kol.recognition.general.Algorithm;
import com.kol.recognition.general.Image;
import com.kol.recognition.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public abstract class Recognizer implements Algorithm {

    protected Multimap<String, Matrix> data;
    protected Multimap<String, Image> training;

    protected Matrix meanMatrix;
    protected int numberOfComponents;

    protected Matrix w;
    protected List<ProjectedTrainingMatrix> projectedTrainingSet;

    protected final int imageAsVectorLength;

    protected AnalysisSettings settings;

    protected Recognizer(Multimap<String, Matrix> data, int components, int vecLength, Multimap<String, Image> training, AnalysisSettings settings){
        if (components >= data.size()) {
            throw new RuntimeException("the expected dimensions could not be achieved!");
        }
        this.data = data;
        this.training = training;
        this.settings = settings;
        this.numberOfComponents = components;
        this.imageAsVectorLength = vecLength;

        init();
    }

    /**
     * The matrix has already been vectorized
     */
    protected Matrix countMean(final List<Matrix> input) {
        final int rows = input.get(0).getRowDimension();
        final int length = input.size();
        final Matrix all = new Matrix(rows, 1);
        input.forEach(all::plusEquals);
        return all.times(1.0 / length);
    }

    protected int[] getIndexesOfKEigenvalues(final double[] d, final int k) {
        final Mix[] mixes = new Mix[d.length];
        for (int i = 0; i < d.length; i++) {
            mixes[i] = new Mix(i, d[i]);
        }
        Arrays.sort(mixes);
        final int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = mixes[i].getIndex();
        }
        return result;
    }

    /**
     * extract features, namely w
     */
    protected abstract void init();

    protected Matrix getMeanMatrix() {
        return meanMatrix;
    }

    protected List<ProjectedTrainingMatrix> getProjectedTrainingSet() {
        return projectedTrainingSet;
    }

    protected Matrix getW() {
        return w;
    }

    public Multimap<String, Image> getTraining() {
        return training;
    }

    public abstract String classify(Matrix vector);

    @Override
    public String classify(BufferedImage image) {
        return classify(ImageUtils.toVector(ImageUtils.toMatrix(image)));
    }

    @Override
    public List<BufferedImage> getProcessedImages() {
        return ImageUtils.convertMatricesToImage(getW(), settings.getHeight(), settings.getWidth());
    }
}