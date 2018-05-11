package com.tunaemre.audiovisualizer.math;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

public class ArrayScaler {
    private volatile static ArrayScaler instance;
    //https://en.wikipedia.org/wiki/Spline_interpolation
    private static UnivariateInterpolator interpolator = new SplineInterpolator();

    private static UnivariateFunction univariateFunction;

    private static double[] scaled;
    private static double[] indices;

    public static ArrayScaler getInstance(int targetLength) {
        if (instance == null) {
            synchronized (ArrayScaler.class) {
                if (instance == null)
                    instance = new ArrayScaler(targetLength);
            }
        }

        return instance;
    }

    public ArrayScaler(int targetLength) {
        scaled = new double[targetLength];
    }

    public double[] scale(double[] source) {
        if (indices == null || indices.length != source.length)
            indices = new double[source.length];

        for (int i = 0; i < indices.length; i++) {
            indices[i] = ((double) i) / (source.length - 1);
        }

        univariateFunction = interpolator.interpolate(indices, source);

        for (int i = 0; i < scaled.length; i++) {
            scaled[i] = univariateFunction.value(((double) i) / scaled.length);
        }

        return scaled;
    }
}