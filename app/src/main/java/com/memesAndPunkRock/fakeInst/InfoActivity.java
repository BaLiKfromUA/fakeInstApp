package com.memesAndPunkRock.fakeInst;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.memesAndPunkRock.fakeInst.api.data.UserContainer;

public class InfoActivity extends AppCompatActivity {

    private UserContainer userContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }
}
