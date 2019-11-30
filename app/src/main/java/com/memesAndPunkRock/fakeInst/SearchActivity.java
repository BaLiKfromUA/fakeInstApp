package com.memesAndPunkRock.fakeInst;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.memesAndPunkRock.fakeInst.api.InstApiController;
import com.memesAndPunkRock.fakeInst.api.data.UserData;
import com.memesAndPunkRock.fakeInst.api.impl.InstApiControllerImpl;
import com.memesAndPunkRock.fakeInst.exception.UserNotFoundException;
import com.memesAndPunkRock.fakeInst.ml.CustomModelController;
import com.memesAndPunkRock.fakeInst.ml.impl.CustomModelControllerImpl;

public class SearchActivity extends AppCompatActivity implements SearchView.OnClickListener {

    private TextInputEditText usernameField;

    private MaterialButton findBtn;

    private InstApiController instApi = new InstApiControllerImpl();

    private CustomModelController modelController;

    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findBtn = findViewById(R.id.findBtn);
        usernameField = findViewById(R.id.usernameField);

        modelController = new CustomModelControllerImpl(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.findBtn) {
            if (usernameField.getText() != null && !usernameField.getText().toString().isEmpty()) {
                String username = usernameField.getText().toString();
                AsyncInstaSearch search = new AsyncInstaSearch();
                search.execute(username);

                if (userData != null) {
                    Intent intent = new Intent(this, InfoActivity.class);
                    intent.putExtra("USER_DATA", userData);
                    startActivity(intent);
                }else{
                    new MaterialAlertDialogBuilder(this)
                            .setTitle("Search don't get any result")
                            .setMessage("Cannot find user")
                            .setPositiveButton("Ok", null)
                            .show();
                }
            }
        }
    }

    private class AsyncInstaSearch extends AsyncTask<String, Void, UserData> {
        @Override
        protected UserData doInBackground(String... strings) {
            return instApi.getUserDataByUserName(strings[0]);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(UserData userData) {
            super.onPostExecute(userData);
            SearchActivity.this.userData = userData;
        }
    }
}
