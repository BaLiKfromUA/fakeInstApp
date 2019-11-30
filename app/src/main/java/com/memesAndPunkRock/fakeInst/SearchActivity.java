package com.memesAndPunkRock.fakeInst;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

        findBtn = findViewById(R.id.findBtn);
        usernameField = findViewById(R.id.usernameField);
        textView = findViewById(R.id.textView);

        Log.d("onCreate", "WTF");

//        findBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.findBtn) {
            try {
                if (usernameField.getText() != null) {
                    String username = usernameField.getText().toString();
                    instApi.getUserDataByUserName(username);

                    textView.setText(username);
                }
            } catch (UserNotFoundException e) {
                Log.d("User don't founded", "ERROR");
                Toast.makeText(this, "User didn't find", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
