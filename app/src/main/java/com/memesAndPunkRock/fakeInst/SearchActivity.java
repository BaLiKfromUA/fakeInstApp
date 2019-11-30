package com.memesAndPunkRock.fakeInst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.memesAndPunkRock.fakeInst.api.InstApiController;
import com.memesAndPunkRock.fakeInst.api.data.UserData;
import com.memesAndPunkRock.fakeInst.api.impl.InstApiControllerImpl;
import com.memesAndPunkRock.fakeInst.exception.UserNotFoundException;
import com.memesAndPunkRock.fakeInst.ml.CustomModelController;
import com.memesAndPunkRock.fakeInst.ml.impl.CustomModelControllerImpl;

public class SearchActivity extends AppCompatActivity implements SearchView.OnClickListener {

    private TextInputEditText usernameField;

    private FloatingActionButton findBtn;

    private InstApiController instApi = new InstApiControllerImpl();

    private CustomModelController modelController;

    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findBtn = findViewById(R.id.floating_action_button);
        usernameField = findViewById(R.id.usernameField);

        modelController = new CustomModelControllerImpl(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.floating_action_button) {
            if (usernameField.getText() != null && !usernameField.getText().toString().isEmpty()) {
                String username = usernameField.getText().toString();
                AsyncInstaSearch search = new AsyncInstaSearch();
                search.execute(username);

                if (userData != null) {
                    //todo here R.id.button
                }
            }
        } else if (v.getId() == R.id.button) {
            Intent intent = new Intent(this, InfoActivity.class);
            //intent.putExtra("USER_DATA", userData);
            startActivity(intent);
        }
    }

    private class AsyncInstaSearch extends AsyncTask<String, Void, UserData> {
        @Override
        protected UserData doInBackground(String... strings) {
            try {
                return instApi.getUserDataByUserName(strings[0]);
            } catch (UserNotFoundException e) {
                return null;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(UserData s) {
            super.onPostExecute(s);
            userData = s;
        }
    }
}
