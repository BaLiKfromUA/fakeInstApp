package com.memesAndPunkRock.fakeInst.api.impl;

import android.util.Log;

import com.memesAndPunkRock.fakeInst.api.InstApiController;
import com.memesAndPunkRock.fakeInst.api.data.UserData;
import com.memesAndPunkRock.fakeInst.exception.UserNotFoundException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author BaLiK
 *
 * */
public class InstApiControllerImpl implements InstApiController {
    private static final String TAG = "INST_API";
    private static final String BASE_URL = "https://inst-api.herokuapp.com";
    private static final String USER_REQUEST_PATTERN = BASE_URL+"/username=%s";

    private String createConnection(String urlS, String methodInvoked, String patchBody, String postBody, String putBody){
        URL url ;
        BufferedReader br = null;
        String toBeReturned="";
        String error;

        try {
            url = new URL(urlS);
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override

                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
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
                connection.setRequestProperty("data",patchBody);
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
                Log.i(TAG, " createConnetion error received " +  responseCode  + "  " + toBeReturned_1) ;

            }
            else{


                br = new BufferedReader( new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                toBeReturned = sb.toString();


            }


        } catch (IOException e) {
            error = e.getMessage();
            e.printStackTrace();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
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
            String methodInvoked = "GET";
            createConnection (requestUrl, methodInvoked,null, null,null);
            createConnection (requestUrl, methodInvoked,null, null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}