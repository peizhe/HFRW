package com.kol.recognition.nbc;

import Jama.Matrix;
import com.google.common.collect.Multimap;
import com.kol.RGBImage;
import com.kol.nbc.NBLA;
import com.kol.nbc.NaiveBayesLearningAlgorithm;
import com.kol.recognition.general.Algorithm;
import com.kol.recognition.general.Image;
import com.kol.recognition.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

public class NBCRecognizer implements Algorithm {

    private NaiveBayesLearningAlgorithm<double[]> nbc;
    private Multimap<String, BufferedImage> data;
    private Multimap<String, Image> train;
    private Matrix meanMatrix;
    private List<RGBImage> rgbImages;

    private int width;
    private int height;

    public NBCRecognizer(Multimap<String, BufferedImage> data, Multimap<String, Image> train, int width, int height) {
        this.data = data;
        this.width = width;
        this.train = train;
        this.height = height;
        this.nbc = NBLA.doubleArray(8);
        this.rgbImages = data.values().stream().map(RGBImage::fromBufferedImage).collect(Collectors.toList());
        this.meanMatrix = countMean(rgbImages);
//        this.data.entries().forEach(e -> nbc.addExample(e.getValue().minus(meanMatrix).getArray()[0], e.getKey()));
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
     * @param input
     */
    private Matrix countMean(final List<RGBImage> input) {
//        final int length = input.size();
//        input.get(0).vectorContent()
//        input.forEach(all::plusEquals);
//        return all.times(1.0 / length);
        return null;
    }

    @Override
    public String classify(BufferedImage image) {
        return nbc.classifier().classify(ImageUtils.toVector(image).minus(meanMatrix).getArray()[0]);
    }

    @Override
    public Multimap<String, Image> getTraining() {
        return train;
    }

    @Override
    public List<BufferedImage> getProcessedImages() {
//        final List<Matrix> values = new ArrayList<>(data.values());
//        final Matrix matrix = new Matrix(width*height, values.size());
//        for (int j = 0; j < values.size(); j++) {
//            for (int i = 0; i < values.get(j).getRowDimension(); i++) {
//                matrix.set(i, j, values.get(j).get(i, 0));
//            }
//        }
        return ImageUtils.convertMatricesToImage(null, height, width);
    }
}
