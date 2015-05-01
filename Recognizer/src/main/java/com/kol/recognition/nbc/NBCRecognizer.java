package com.kol.recognition.nbc;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.kol.RGBImage;
import com.kol.Utils;
import com.kol.nbc.NBLA;
import com.kol.nbc.NaiveBayesLearningAlgorithm;
import com.kol.recognition.general.Algorithm;
import com.kol.recognition.general.Image;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class NBCRecognizer implements Algorithm {

    private NaiveBayesLearningAlgorithm<int[]> nbc;
    private Multimap<String, BufferedImage> data;
    private Multimap<String, Image> train;
    private int[] meanMatrix;

    private int width;
    private int height;

    public NBCRecognizer(Multimap<String, BufferedImage> data, Multimap<String, Image> train, int width, int height) {
        this.data = data;
        this.width = width;
        this.train = train;
        this.height = height;
        this.nbc = NBLA.intArray();
        final Multimap<String, RGBImage> rgbImages = Multimaps.transformValues(data, RGBImage::fromBufferedImage);
        this.meanMatrix = countMean(rgbImages.values());
        rgbImages.entries().forEach(e -> nbc.addExample(Utils.diff(e.getValue().vectorContent(), meanMatrix), e.getKey()));
    }

    /*@Override
    protected void init() {
        final List<Matrix> faces = new ArrayList<>(data.values());
        meanMatrix = countMean(faces);
        nbc = NBLA.doubleArray(8);

        final PCA pca = new PCA(data, numberOfComponents, imageAsVectorLength, training);
        final Multimap<String, Matrix> pcaSpace = ArrayListMultimap.create();
        data.entries().forEach(d -> pcaSpace.put(d.getKey(), pca.getW().transpose().times(d.getValue().minus(meanMatrix)).transpose()));
        this.w = pca.w;

        pcaSpace.entries().forEach(e -> nbc.addExample(e.getValue().getArray()[0], e.getKey()));
    }

    @Override
    public String classify(Matrix vector, ClassifySettings data) {
        return nbc.classifier().classify(w.transpose().times(vector.minus(meanMatrix)).transpose().getArray()[0]);
    }*/

    /**
     * The matrix has already been vectorized
     */
    private int[] countMean(final Collection<RGBImage> input) {
        int[] accumulator = new int[width*height];
        for (RGBImage image : input) {
            accumulator = Utils.sum(accumulator, image.vectorContent());
        }
        return Arrays.stream(accumulator).map(v -> v / input.size()).toArray();
    }

    @Override
    public String classify(BufferedImage image) {
        return nbc.classifier().classify(Utils.diff(RGBImage.fromBufferedImage(image).vectorContent(), meanMatrix));
    }

    @Override
    public Multimap<String, Image> getTraining() {
        return train;
    }

    @Override
    public List<BufferedImage> getProcessedImages() {
        return Lists.newArrayList(data.values());
    }
}
