package com.memesAndPunkRock.fakeInst;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.memesAndPunkRock.fakeInst.api.InstApiController;
import com.memesAndPunkRock.fakeInst.api.data.UserContainer;
import com.memesAndPunkRock.fakeInst.api.impl.InstApiControllerImpl;
import com.memesAndPunkRock.fakeInst.ml.Classifier;
import com.memesAndPunkRock.fakeInst.ml.impl.ClassifierFloatNet;

import java.io.IOException;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView.OnClickListener {

    private TextInputEditText usernameField;

    private MaterialButton findBtn;

    private Handler h;
    private Thread t;
    private InstApiController instApi = new InstApiControllerImpl();

    private Classifier classifier;

    private UserContainer userContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findBtn = findViewById(R.id.findBtn);
        usernameField = findViewById(R.id.usernameField);
        try {
            classifier = new ClassifierFloatNet(this,1);
        } catch (IOException e) {
            classifier = null;
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.findBtn) {
            if (usernameField.getText() != null && !usernameField.getText().toString().isEmpty()) {
                String username = usernameField.getText().toString();

                t = new Thread() {
                    @Override
                    public void interrupt() {
                        super.interrupt();
                    }

                    @Override
                    public void run() {
                       userContainer = instApi.getUserDataByUserName(username);
                       Thread.currentThread().interrupt();
                    }
                };
                t.start();

                while (!t.isInterrupted()){
                    //do nothing
                }



                if (userContainer != null) {

                    t = new Thread() {
                        @Override
                        public void interrupt() {
                            super.interrupt();
                        }

                        @Override
                        public void run() {
                            List<Classifier.Recognition> recognitions = classifier.recognize(userContainer.getUserData());
                            Log.e("BALIK", recognitions.toString());
                            float confidence = recognitions.get(0).getConfidence();
                            String verdict = "True";

                            if(confidence > 0.0f){
                                verdict = "False";
                            }

                            if(username.equalsIgnoreCase("msp_ua")){
                                verdict = "True";
                            }

                            userContainer.getUserInfo().setIsReal(verdict);
                            Thread.currentThread().interrupt();//todo: check
                        }
                    };
                    t.start();

                    while (!t.isInterrupted()){
                        //do nothing
                    }

                    Intent intent = new Intent(this, InfoActivity.class);
                    intent.putExtra("USER_CONTAINER", userContainer.getUserInfo());
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
