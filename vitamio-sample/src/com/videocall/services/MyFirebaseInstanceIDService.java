package com.videocall.services;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    public MyFirebaseInstanceIDService() {
    }

    private static final String TAG = "MyFirebaseIIDService";
    private String device_id;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private String responseId;
    OkHttpClient client = new OkHttpClient();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        Log.e("firebase token", ">>>>>>>>>>>>>>>>>>>>TOKEN REFRESHED");
        // Get updated InstanceID token.
        device_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        //**************need to modify the code such that it can update exisiting user data***********************
        sendRegistrationToServer(refreshedToken);
        //**************need to modify the code such that it can update exisiting user data***********************
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        String json ="{'email' : '"
                + device_id
                + "' ,'firebase_token': '" + token + "'}";

        try {
            JSONObject jObj = new JSONObject(json);
            responseId = doPostRequest("https://iot-api.herokuapp.com/users", jObj.toString());
        } catch (IOException e) {

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}
