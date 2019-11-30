package com.memesAndPunkRock.fakeInst.api.impl;

import com.memesAndPunkRock.fakeInst.api.InstApiController;
import com.memesAndPunkRock.fakeInst.api.data.UserData;
import com.memesAndPunkRock.fakeInst.exception.UserNotFoundException;

import org.jinstagram.Instagram;

/**
 * @author BaLiK
 *
 * */
public class InstApiControllerImpl implements InstApiController {
    private final String ACCESS_TOKEN = "4779711016.1677ed0.a6ef4ef0fb6649f18b835f8ac15e2b65";
    private Instagram instagram = null;

    public InstApiControllerImpl() {
        this.instagram = new Instagram(ACCESS_TOKEN);
    }


    @Override
    public UserData getUserDataByUserName(String username) throws UserNotFoundException {
        return null;
    }
}
