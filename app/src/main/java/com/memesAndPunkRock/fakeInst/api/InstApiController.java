package com.memesAndPunkRock.fakeInst.api;

import com.memesAndPunkRock.fakeInst.api.data.UserData;

public interface InstApiController{

    UserData getUserDataByUserName(final String username);

}
