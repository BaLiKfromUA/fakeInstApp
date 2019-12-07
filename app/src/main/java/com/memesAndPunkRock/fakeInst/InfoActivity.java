package com.memesAndPunkRock.fakeInst;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.memesAndPunkRock.fakeInst.api.data.UserInfo;
import com.squareup.picasso.Picasso;

/**
 * Activity, that display information of instagram user and result of recognition
 */
public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //Getting userInfo from previous activity
        UserInfo userInfo = (UserInfo) getIntent().getSerializableExtra("USER_INFO");

        //Bind view to variables
        TextView usernameTV = findViewById(R.id.usernameTV);
        TextView followerCount = findViewById(R.id.followerCountTV);
        TextView followsCount = findViewById(R.id.followingCountTV);
        TextView statusTv = findViewById(R.id.statusTV);
        ImageView avatar = findViewById(R.id.avatar);

        //Setting followers and following from userInfo
        followerCount.setText(userInfo.getFollowers());
        followsCount.setText(userInfo.getFollowing());

        //Setting result of recognition
        if(userInfo.getIsReal().equalsIgnoreCase("false")){
            statusTv.setText(R.string.fake);
            statusTv.setBackground(ContextCompat.getDrawable(this, R.drawable.fake_rounded_shape));
        }else if(userInfo.getIsReal().equalsIgnoreCase("true")){
            statusTv.setText(R.string.real);
            statusTv.setBackground(ContextCompat.getDrawable(this, R.drawable.real_container));
        }

        //Loading user's avatar
        Picasso.get().load(userInfo.getAvatarUrl()).into(avatar);

        //Setting up username
        usernameTV.setText(userInfo.getUsername());
    }
}
