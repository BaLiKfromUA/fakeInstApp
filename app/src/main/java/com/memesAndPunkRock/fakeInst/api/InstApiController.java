package com.memesAndPunkRock.fakeInst.api;

import com.memesAndPunkRock.fakeInst.api.data.UserData;
import com.memesAndPunkRock.fakeInst.exception.UserNotFoundException;

public interface InstApiController {

    UserData getUserDataByUserName(final String username) throws UserNotFoundException;

}
