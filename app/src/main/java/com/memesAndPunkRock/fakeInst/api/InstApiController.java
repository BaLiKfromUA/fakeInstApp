package com.memesAndPunkRock.fakeInst.api;

import com.memesAndPunkRock.fakeInst.api.data.UserContainer;

public interface InstApiController{

    UserContainer getUserDataByUserName(final String username);

}
