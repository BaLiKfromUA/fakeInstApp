package com.memesAndPunkRock.fakeInst.api.impl;

import com.memesAndPunkRock.fakeInst.api.InstApiController;
import com.memesAndPunkRock.fakeInst.api.data.UserData;
import com.memesAndPunkRock.fakeInst.exception.UserNotFoundException;

import org.jinstagram.Instagram;
import org.jinstagram.entity.users.feed.UserFeed;
import org.jinstagram.entity.users.feed.UserFeedData;
import org.jinstagram.exceptions.InstagramException;

import java.util.List;

/**
 * @author BaLiK
 *
 * */
public class InstApiControllerImpl implements InstApiController {
    private static final String TAG = "INST_API";

    private final String ACCESS_TOKEN = "4779711016.1677ed0.a6ef4ef0fb6649f18b835f8ac15e2b65";
    private Instagram instagram = null;

    public InstApiControllerImpl() {
        this.instagram = new Instagram(ACCESS_TOKEN);
    }

    private UserData convertUserFeedDataToUserData(UserFeedData feedData) {
        if (feedData == null){
            return null;
        }

        UserData convertedData = new UserData();
        float numberOfNumChars = feedData.getUserName().chars()
                                                        .filter(Character::isDigit)
                                                        .count();
        convertedData.setRatioOfNumbersCharsToUsernameLength(numberOfNumChars / feedData.getUserName().length());
        //todo:
        return convertedData;
    }

    @Override
    public UserData getUserDataByUserName(String username) throws UserNotFoundException {
        try {
            if(username == null || username.isEmpty()){
                throw new InstagramException("invalid username");
            }

            UserFeed userFeed = instagram.searchUser(username);
            List<UserFeedData> users = userFeed.getUserList();
            if (users.isEmpty()){
                throw new InstagramException("user list is empty");
            }

            return convertUserFeedDataToUserData(users.get(0));
        } catch (InstagramException e) {
            throw new UserNotFoundException("Search user error. Reason:\n"+e.getMessage());
        }
    }

}
