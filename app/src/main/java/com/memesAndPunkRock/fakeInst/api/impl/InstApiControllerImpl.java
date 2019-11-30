package com.memesAndPunkRock.fakeInst.api.impl;

import android.util.Log;

import com.memesAndPunkRock.fakeInst.api.InstApiController;
import com.memesAndPunkRock.fakeInst.api.data.UserData;
import com.memesAndPunkRock.fakeInst.exception.UserNotFoundException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author BaLiK
 *
 * */
public class InstApiControllerImpl implements InstApiController {
    private static final String TAG = "INST_API";
    private static final String BASE_URL = "https://inst-api.herokuapp.com";
    private static final String USER_REQUEST_PATTERN = BASE_URL+"/username=%s";

    private String doGet(String requestURL) throws IOException {
        URL url = new URL(requestURL);
        HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
        urlConnection.setRequestMethod("GET");
        int statusCode = urlConnection.getResponseCode();
        if (statusCode ==  200) {
            InputStream it = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader read = new InputStreamReader(it);
            BufferedReader buff = new BufferedReader(read);
            StringBuilder dta = new StringBuilder();
            String chunks;
            while((chunks = buff.readLine()) != null) {
                dta.append(chunks);
            }

            return dta.toString();
        }
        else {
            return "null";
        }
    }


    private UserData convertUserFeedDataToUserData(String feedData) {
        if (feedData == null){
            return null;
        }

        UserData convertedData = new UserData();
        float numberOfNumChars = feedData.chars()
                .filter(Character::isDigit)
                .count();
        convertedData.setRatioOfNumbersCharsToUsernameLength(numberOfNumChars / feedData.length());
        //todo:
        return convertedData;
    }

    @Override
    public UserData getUserDataByUserName(String username){
        String requestUrl = String.format(USER_REQUEST_PATTERN, username);
        try {
            String resultJsonString = doGet(requestUrl);
            Log.e(TAG,resultJsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}