package com.memesAndPunkRock.fakeInst;

import androidx.appcompat.app.AppCompatActivity;

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

import com.memesAndPunkRock.fakeInst.api.InstApiController;
import com.memesAndPunkRock.fakeInst.api.impl.InstApiControllerImpl;
import com.memesAndPunkRock.fakeInst.exception.UserNotFoundException;

public class SearchActivity extends AppCompatActivity implements SearchView.OnClickListener {

    private EditText usernameField;

    private TextView textView;

    private Button findBtn;

    private InstApiController instApi = new InstApiControllerImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        findBtn = findViewById(R.id.findBtn);
        usernameField = findViewById(R.id.usernameField);
        textView = findViewById(R.id.textView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.findBtn) {
            if (usernameField.getText() != null) {
                String username = usernameField.getText().toString();
                AsyncInstaSearch search = new AsyncInstaSearch();
                search.execute(username);
//                     instApi.getUserDataByUserName(username).toString();

//                    textView.setText(username);
            }
        }
    }

    class AsyncInstaSearch extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setText(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                return instApi.getUserDataByUserName(strings[0]).toString();
            } catch (UserNotFoundException e) {
                Log.d("fakeInst", "User not found");
                Log.d("fakeInst", "Exception msg: " + e.getMessage());
                return "-1";
            }
        }
    }
}
