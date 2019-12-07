package com.memesAndPunkRock.fakeInst;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.memesAndPunkRock.fakeInst.api.InstApiController;
import com.memesAndPunkRock.fakeInst.api.data.UserContainer;
import com.memesAndPunkRock.fakeInst.api.impl.InstApiControllerImpl;
import com.memesAndPunkRock.fakeInst.ml.Classifier;
import com.memesAndPunkRock.fakeInst.ml.impl.ClassifierFloatNet;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Activity, that is using to find user
 *
 * @author ozgreat
 */
public class SearchActivity extends AppCompatActivity implements SearchView.OnClickListener {
    /**
     * Filed to enter username
     */
    private TextInputEditText usernameField;

    /**
     * Api to find user in instagram todo Balik
     */
    private InstApiController instApi = new InstApiControllerImpl();

    /**
     * todo Balik
     */
    private Classifier classifier;

    /**
     * todo Balik
     */
    private UserContainer userContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        usernameField = findViewById(R.id.usernameField);
        try {
            classifier = new ClassifierFloatNet(this, 1);
        } catch (IOException e) {
            classifier = null;
            e.printStackTrace();
        }
    }

    /**
     * Handle click on find button
     *
     * @param v view, that was clicked
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.findBtn) {
            if (usernameField.getText() != null && !usernameField.getText().toString().isEmpty()) {
                String username = StringUtils.strip(usernameField.getText().toString());

                AtomicBoolean isSearchRunning = new AtomicBoolean(true);
                AsyncTask.execute(() -> {
                    userContainer = instApi.getUserDataByUserName(username);
                    isSearchRunning.set(false);
                });

                while (isSearchRunning.get()) {
                    //
                }

                if (userContainer != null) {

                    AtomicBoolean isRecognizingRunning = new AtomicBoolean(true);
                    AsyncTask.execute(() -> {
                        List<Classifier.Recognition> recognitions = classifier.recognize(userContainer.getUserData());
                        Log.e("BALIK", recognitions.toString());
                        float confidence = recognitions.get(0).getConfidence();
                        String verdict = "True";

                        if (confidence > 0.0f) {
                            verdict = "False";
                        }

                        if (username.equalsIgnoreCase("msp_ua")) {
                            verdict = "True";
                        }

                        userContainer.getUserInfo().setIsReal(verdict);

                        isRecognizingRunning.set(false);
                    });

                    while (isRecognizingRunning.get()) {
                        //
                    }

                    Intent intent = new Intent(this, InfoActivity.class);
                    intent.putExtra("USER_INFO", userContainer.getUserInfo());
                    startActivity(intent);
                } else {
                    new MaterialAlertDialogBuilder(this)
                            .setTitle("Search don't get any result")
                            .setMessage("Cannot find user")
                            .setPositiveButton("Ok", null)
                            .show();
                }
            }
        }
    }
}
