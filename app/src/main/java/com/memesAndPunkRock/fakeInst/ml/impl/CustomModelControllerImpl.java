package com.memesAndPunkRock.fakeInst.ml.impl;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.common.modeldownload.FirebaseRemoteModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelOptions;
import com.memesAndPunkRock.fakeInst.api.data.UserData;
import com.memesAndPunkRock.fakeInst.ml.CustomModelController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

public class CustomModelControllerImpl implements CustomModelController {
    private static final String TAG = "Model Controller";

    /**
     * Name of the model file hosted with Firebase.
     */
    private static final String HOSTED_MODEL_NAME = "balik_version";
    private static final String LOCAL_MODEL_ASSET = "model.tflite";
    /**
     * Name of the label file stored in Assets.
     */
    private static final String LABEL_PATH = "labels.txt";

    /**
     * An instance of the driver class to run model inference with Firebase.
     */
    private FirebaseModelInterpreter mInterpreter;
    /**
     * Data configuration of input & output data of model.
     */
    private FirebaseModelInputOutputOptions mDataOptions;

    /**
     * Labels corresponding to the output of the vision model.
     */
    private List<String> mLabelList;

    private static final int RESULTS_TO_SHOW = 2;

    private final PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    (o1, o2) -> (o1.getValue()).compareTo(o2.getValue()));


    /**
     * Reads label list from Assets.
     */
    private List<String> loadLabelList(Activity activity) {
        List<String> labelList = new ArrayList<>();
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(activity.getAssets().open
                             (LABEL_PATH)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                labelList.add(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to read label list.", e);
        }
        return labelList;
    }

    private void initCustomModel(Activity activity) {
        mLabelList = loadLabelList(activity);

        int [] inputDims = {11};
        int [] outputDims = {1};
        try {
            mDataOptions =
                    new FirebaseModelInputOutputOptions.Builder()
                            .setInputFormat(0, FirebaseModelDataType.FLOAT32, inputDims)
                            .setOutputFormat(0, FirebaseModelDataType.FLOAT32, outputDims)
                            .build();
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions
                    .Builder()
                    .requireWifi()
                    .build();
            FirebaseRemoteModel remoteModel = new FirebaseRemoteModel.Builder
                    (HOSTED_MODEL_NAME)
                    .enableModelUpdates(true)
                    .setInitialDownloadConditions(conditions)
                    .setUpdatesDownloadConditions(conditions)  // You could also specify
                    // different conditions
                    // for updates
                    .build();
            FirebaseLocalModel localModel =
                    new FirebaseLocalModel.Builder("asset")
                            .setAssetFilePath(LOCAL_MODEL_ASSET).build();
            FirebaseModelManager manager = FirebaseModelManager.getInstance();
            manager.registerRemoteModel(remoteModel);
            manager.registerLocalModel(localModel);
            FirebaseModelOptions modelOptions =
                    new FirebaseModelOptions.Builder()
                            .setRemoteModelName(HOSTED_MODEL_NAME)
                            .setLocalModelName("asset")
                            .build();
            mInterpreter = FirebaseModelInterpreter.getInstance(modelOptions);
            Log.e(TAG, "model setup");
        } catch (FirebaseMLException e) {
            Log.e(TAG, "Error while setting up the model\n"+e.getMessage());
        }
    }

    /**
     * Gets the top labels in the results.
     */
    private synchronized List<String> getTopLabels(byte[][] labelProbArray) {
        for (int i = 0; i < mLabelList.size(); ++i) {
            sortedLabels.add(
                    new AbstractMap.SimpleEntry<>(mLabelList.get(i), (labelProbArray[0][i] &
                            0xff) / 255.0f));
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }

        List<String> result = new ArrayList<>();
        final int size = sortedLabels.size();
        for (int i = 0; i < size; ++i) {
            Map.Entry<String, Float> label = sortedLabels.poll();
            result.add(label.getKey() + ":" + label.getValue());
        }
        Log.d(TAG, "labels: " + result.toString());
        return result;
    }

    private void runModelInference(float[] inputNums) {
        if (mInterpreter == null) {
            Log.e(TAG, "Image classifier has not been initialized; Skipped.");
            return;
        }

        try {
            FirebaseModelInputs inputs = new FirebaseModelInputs.Builder().add(inputNums).build();
            // Here's where the magic happens!!
            mInterpreter
                    .run(inputs, mDataOptions)
                    .addOnFailureListener(e ->
                            Log.e(TAG, "Error running model inference\n"+e.getMessage()))
                    .continueWith(
                            task -> {
                                byte[][] labelProbArray = Objects.requireNonNull(task.getResult())
                                        .getOutput(0);
                                List<String> topLabels = getTopLabels(labelProbArray);
                                Log.d(TAG, topLabels.toString());
                                return topLabels;
                            });
        } catch (FirebaseMLException e) {
            Log.e(TAG,"Error running model inference\n"+e.getMessage());
        }
    }

    public CustomModelControllerImpl(Activity activity) {
        initCustomModel(activity);
    }

    private float[] prepareData(UserData data){
        return new float[]{data.getIsUserWithoutAvatar(), data.getRatioOfNumbersCharsToUsernameLength(),
                data.getFullNameTokensNumber(),data.getRatioOfNumbersCharsToFullNameLength(),
                data.getIsUsernameEqualsFullname(),
                data.getDescriptionLength(), data.getHasExternalUrl(),
                data.getIsPrivate(),data.getPostsNumber(), data.getFollowersNumber(), data.getFollowsNumber()};
    }

    @Override
    public List<String> getPredictions(UserData user) {
        runModelInference(prepareData(user));
        return mLabelList;
    }
}
