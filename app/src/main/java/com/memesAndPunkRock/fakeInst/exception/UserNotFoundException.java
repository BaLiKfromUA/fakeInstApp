package com.memesAndPunkRock.fakeInst.exception;


/**
 *
 * FileException wraps all checked standard Java exception and enriches them with a custom error message.
 * Throw it when error caused if user with such nickname doesn't exist in Instagram.
 *
 * @author BaLiK
 * **/
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}
