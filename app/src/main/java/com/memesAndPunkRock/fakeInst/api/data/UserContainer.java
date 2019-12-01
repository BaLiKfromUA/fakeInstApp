package com.memesAndPunkRock.fakeInst.api.data;

import java.io.Serializable;

public class UserContainer  implements Serializable {
    private UserData userData = null;
    private UserInfo userInfo = null;

    public UserContainer() {
    }

    public UserContainer(UserData userData, UserInfo userInfo) {
        this.userData = userData;
        this.userInfo = userInfo;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
