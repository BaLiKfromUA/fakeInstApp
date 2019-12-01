package com.memesAndPunkRock.fakeInst.api.impl;

import android.util.Log;

import com.memesAndPunkRock.fakeInst.api.InstApiController;
import com.memesAndPunkRock.fakeInst.api.data.UserContainer;
import com.memesAndPunkRock.fakeInst.api.data.UserData;
import com.memesAndPunkRock.fakeInst.api.data.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author BaLiK
 *
 * */
public class InstApiControllerImpl implements InstApiController {
    private static final String TAG = "INST_API";
    private static final String BASE_URL = "https://inst-api.herokuapp.com";
    private static final String USER_REQUEST_PATTERN = BASE_URL+"/";

    private String createConnection(String urlS, String methodInvoked, String patchBody, String postBody, String putBody){
        URL url;
        BufferedReader br = null;
        String toBeReturned="";

        try {
            url = new URL(urlS);
            HostnameVerifier hostnameVerifier = (hostname, session) -> true;
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                            return myTrustedAnchors;
                        }
                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // Create an SSLContext that uses our TrustManager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, null);

            HttpsURLConnection  connection = (HttpsURLConnection) url.openConnection();
            connection.setConnectTimeout(60000);
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            connection.setSSLSocketFactory(sc.getSocketFactory());
            connection.setHostnameVerifier(hostnameVerifier);

            if (patchBody  != null ){
                Log.i(TAG, " createConnection with PATH with body" );
                connection.setRequestMethod("PATCH");
                connection.setRequestProperty("username",patchBody);
                connection.addRequestProperty("Content-Type", "application/json");
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(patchBody);
                dStream.flush();
                dStream.close();
            }
            if (methodInvoked.equalsIgnoreCase("PATCH") && patchBody == null ){
                Log.i(TAG, " createConnection with PATH without body" );
                connection.setRequestMethod("PATCH");
//              connection.addRequestProperty("Content-Type", "application/json");
//              connection.setDoOutput(true);
            }
            if (postBody != null){
                Log.i(TAG, " createConnection with POST with body" );
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(postBody);
                dStream.flush();
                dStream.close();
            }

            if (methodInvoked.equalsIgnoreCase("POST") && postBody == null ){
                Log.i(TAG, " createConnection with POST without body" );
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //connection.addRequestProperty("Content-Type", "application/json");
            }

            if (putBody != null){
                Log.i(TAG, " createConnection with PUT with body" );
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.addRequestProperty("Content-Type", "application/json");
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(putBody);
                dStream.flush();
                dStream.close();
            }


            int responseCode = connection.getResponseCode();
            InputStream in= null;
            if(responseCode >= HttpsURLConnection.HTTP_BAD_REQUEST)
            {

                in = connection.getErrorStream();
                br = new BufferedReader( new InputStreamReader(connection.getErrorStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                String toBeReturned_1 = sb.toString();
                Log.i(TAG, " createConnection1 error received " +  responseCode  + "  " + toBeReturned_1) ;

            }
            else{

                br = new BufferedReader( new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                toBeReturned = sb.toString();

            }

        } catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } // TODO Auto-generated catch block
        finally{
            try {
                if (br!=null)
                    br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        Log.i(TAG, " createConnetion  finally returned" +  toBeReturned );
        return toBeReturned;
    }


    private UserData parseUserData(String userDataString) throws JSONException {
        if (userDataString == null || userDataString.isEmpty()){
            return null;
        }

        final JSONObject jObject = new JSONObject(userDataString);

        UserData convertedData = new UserData();
        convertedData.setIsUserWithoutAvatar((float) jObject.getDouble("profile pic"));
        convertedData.setRatioOfNumbersCharsToUsernameLength((float)jObject.getDouble("nums/length username"));
        convertedData.setFullNameTokensNumber((float)jObject.getDouble("fullname words"));
        convertedData.setRatioOfNumbersCharsToFullNameLength((float)jObject.getDouble("nums/length fullname"));
        convertedData.setIsUsernameEqualsFullname((float)jObject.getDouble("name==username"));
        convertedData.setDescriptionLength((float)jObject.getDouble("description length"));
        convertedData.setHasExternalUrl((float)jObject.getDouble("external URL"));
        convertedData.setPostsNumber((float)jObject.getDouble("#posts"));
        convertedData.setFollowersNumber((float)jObject.getDouble("#followers"));
        convertedData.setFollowsNumber((float)jObject.getDouble("#follows"));
        convertedData.setIsPrivate((float)jObject.getDouble("private"));

        return convertedData;
    }

    private UserInfo parseUserInfo(String userInfoString) throws JSONException {
        if(userInfoString == null || userInfoString.isEmpty()){
            return null;
        }

        final JSONObject jObject = new JSONObject(userInfoString);

        UserInfo userInfo = new UserInfo();
        userInfo.setAvatarUrl(jObject.getString("pic_url"));
        userInfo.setUsername(jObject.getString("username"));
        userInfo.setFollowers(jObject.getString("followers"));
        userInfo.setFollowing(jObject.getString("follows"));
        userInfo.setIsReal("True");// todo: refactor

        return userInfo;
    }

    @Override
    public UserContainer getUserDataByUserName(String username){
        try {
            final String methodInvoked = "GET";
            final String jsonString = createConnection (USER_REQUEST_PATTERN, methodInvoked, username,
                    null,null);

            final JSONObject jObject = new JSONObject(jsonString);
            final String message = jObject.getString("message");

            if (message.equals("empty") || message.equals("unsuccess")){
                return null;
            }

            UserInfo userInfo = parseUserInfo(message);
            UserData userData = parseUserData(jsonString);

            return new UserContainer(userData, userInfo);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }
}