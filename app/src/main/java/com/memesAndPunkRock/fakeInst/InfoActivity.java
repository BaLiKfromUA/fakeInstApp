package com.memesAndPunkRock.fakeInst;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.memesAndPunkRock.fakeInst.api.data.UserInfo;
import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity {

    private UserInfo userContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        userContainer = (UserInfo) getIntent().getSerializableExtra("USER_CONTAINER");
        TextView usernameTV = findViewById(R.id.usernameTV);
        TextView followerCount = findViewById(R.id.followerCountTV);
        TextView followsCount = findViewById(R.id.followingCountTV);
        TextView statusTv = findViewById(R.id.statusTV);
        ImageView avatar = findViewById(R.id.avatar);
        followerCount.setText(userContainer.getFollowers());
        followsCount.setText(userContainer.getFollowing());
        statusTv.setText(userContainer.getIsReal());
        if(userContainer.getIsReal().equalsIgnoreCase("fake")){
            statusTv.setBackground(ContextCompat.getDrawable(this, R.drawable.fake_rounded_shape));
        }else{
            statusTv.setBackground(ContextCompat.getDrawable(this, R.drawable.real_container));
        }
        Picasso.get().load(userContainer.getAvatarUrl()).into(avatar);
        usernameTV.setText(userContainer.getUsername());
    }
}
