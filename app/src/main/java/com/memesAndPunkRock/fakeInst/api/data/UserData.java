package com.memesAndPunkRock.fakeInst.api.data;

/**
 * @author BaLiK
 *
 * **/
public class UserData {
    /**
     * user has profile picture or not
     * **/
    private Float isUserWithoutAvatar;

    /**
     * ratio of number of numerical chars in username to its length
     * **/
    private Float ratioOfNumbersCharsToUsernameLength;

    /**
     * full name in word tokens
     * **/
    private Float fullNameTokensNumber;

    /**
     * ratio of number of numerical characters in full name to its length
     * **/
    private Float ratioOfNumbersCharsToFullNameLength;

    /**
     * are username and full name literally the same
     **/
    private Float isUsernameEqualsFullname;

    /**
     * bio length in characters
     **/
    private Float descriptionLength;


    /**
     * has external URL or not
     **/
    private Float hasExternalUrl;

    /**
     * private or not
     **/
    private Float isPrivate;

    private Float postsNumber;

    private Float followersNumber;

    private Float followsNumber;

}
