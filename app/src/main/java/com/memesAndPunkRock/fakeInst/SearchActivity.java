package com.memesAndPunkRock.fakeInst;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Tasks;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.memesAndPunkRock.fakeInst.api.InstApiController;
import com.memesAndPunkRock.fakeInst.api.data.UserContainer;
import com.memesAndPunkRock.fakeInst.api.impl.InstApiControllerImpl;
import com.memesAndPunkRock.fakeInst.ml.CustomModelController;
import com.memesAndPunkRock.fakeInst.ml.impl.CustomModelControllerImpl;

public class SearchActivity extends AppCompatActivity implements SearchView.OnClickListener {

    private TextInputEditText usernameField;

    private MaterialButton findBtn;

    private InstApiController instApi = new InstApiControllerImpl();

    private CustomModelController modelController;

    private UserContainer userContainer;

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


                while (search.getStatus() != AsyncTask.Status.FINISHED){
                    Log.d("Async" , "Run");
                }

                if (userContainer != null) {
                    Intent intent = new Intent(this, InfoActivity.class);
                    intent.putExtra("USER_CONTAINER", userContainer);
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

    private class AsyncInstaSearch extends AsyncTask<String, Void, UserContainer> {
        @Override
        protected UserContainer doInBackground(String... strings) {
            Log.i("ASYNC", "BACK");
            return instApi.getUserDataByUserName(strings[0]);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(UserContainer userContainer) {
            super.onPostExecute(userContainer);
            Log.i("Async", "POST");
            SearchActivity.this.userContainer = userContainer;
        }
    }
}
