package com.memesAndPunkRock.fakeInst.ml.impl;

import android.app.Activity;

import com.memesAndPunkRock.fakeInst.ml.Classifier;

import java.io.IOException;

public class ClassifierFloatNet extends Classifier {

    /**
     * An array to hold inference results, to be feed into Tensorflow Lite as outputs. This isn't part
     * of the super class, because we need a primitive array here.
     */
    private float[][] labelProbArray = null;

    /**
     * Initializes a {@code ClassifierFloatMobileNet}.
     *
     * @param activity
     */
    public ClassifierFloatNet(Activity activity, int numThreads)
            throws IOException {
        super(activity,numThreads);
        labelProbArray = new float[1][getNumLabels()];
    }

    @Override
    protected String getModelPath() {
        return "model.tflite";
    }

    @Override
    protected String getLabelPath() {
        return "labels.txt";
    }

    @Override
    protected float getProbability(int labelIndex) {
        return labelProbArray[0][labelIndex];
    }

    @Override
    protected void setProbability(int labelIndex, Number value) {
        labelProbArray[0][labelIndex] = value.floatValue();
    }

    @Override
    protected float getNormalizedProbability(int labelIndex) {
        return labelProbArray[0][labelIndex];
    }

    @Override
    protected void runInference(float[] input) {
        float[][] input_1 = {input};
        tflite.run(input_1, labelProbArray);
    }
}
