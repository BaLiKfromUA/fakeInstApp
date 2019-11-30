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

    private Float postsNumber;

    private Float followersNumber;

    private Float followsNumber;

    public Float getIsUserWithoutAvatar() {
        return isUserWithoutAvatar;
    }

    public void setIsUserWithoutAvatar(Float isUserWithoutAvatar) {
        this.isUserWithoutAvatar = isUserWithoutAvatar;
    }

    public Float getRatioOfNumbersCharsToUsernameLength() {
        return ratioOfNumbersCharsToUsernameLength;
    }

    public void setRatioOfNumbersCharsToUsernameLength(Float ratioOfNumbersCharsToUsernameLength) {
        this.ratioOfNumbersCharsToUsernameLength = ratioOfNumbersCharsToUsernameLength;
    }

    public Float getFullNameTokensNumber() {
        return fullNameTokensNumber;
    }

    public void setFullNameTokensNumber(Float fullNameTokensNumber) {
        this.fullNameTokensNumber = fullNameTokensNumber;
    }

    public Float getRatioOfNumbersCharsToFullNameLength() {
        return ratioOfNumbersCharsToFullNameLength;
    }

    public void setRatioOfNumbersCharsToFullNameLength(Float ratioOfNumbersCharsToFullNameLength) {
        this.ratioOfNumbersCharsToFullNameLength = ratioOfNumbersCharsToFullNameLength;
    }

    public Float getIsUsernameEqualsFullname() {
        return isUsernameEqualsFullname;
    }

    public void setIsUsernameEqualsFullname(Float isUsernameEqualsFullname) {
        this.isUsernameEqualsFullname = isUsernameEqualsFullname;
    }

    public Float getDescriptionLength() {
        return descriptionLength;
    }

    public void setDescriptionLength(Float descriptionLength) {
        this.descriptionLength = descriptionLength;
    }

    public Float getHasExternalUrl() {
        return hasExternalUrl;
    }

    public void setHasExternalUrl(Float hasExternalUrl) {
        this.hasExternalUrl = hasExternalUrl;
    }

    public Float getPostsNumber() {
        return postsNumber;
    }

    public void setPostsNumber(Float postsNumber) {
        this.postsNumber = postsNumber;
    }

    public Float getFollowersNumber() {
        return followersNumber;
    }

    public void setFollowersNumber(Float followersNumber) {
        this.followersNumber = followersNumber;
    }

    public Float getFollowsNumber() {
        return followsNumber;
    }

    public void setFollowsNumber(Float followsNumber) {
        this.followsNumber = followsNumber;
    }
}
