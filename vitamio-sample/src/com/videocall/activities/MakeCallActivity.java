package com.videocall.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.vov.vitamio.demo.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MakeCallActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    //public static final String FIREBASE_TOKEN = "dLt6koxf2JM:APA91bEDYKKuPrI7kOic8Vww1lx4-8x97Nqm3DwyNWwFtMO2H-hFLfUEI76Man2b1tvDX9wwBO0DGnnF9IUr8qxS12XU_v97PjoBZzpxqQLxh_n7YtDrALMYC2rYPiXLBNGbpr9Pvx9C";
    //public static final String FIREBASE_TOKEN = "cdGMkD1k4eY:APA91bHw2pHkOVwPtseNXN_MauiyEkGny0zaGCDdCrQZtOGARmp5wZgIRgPI4j-ZvYox7WsMKnwHDglF3VNKxdQL04FXLgN2iiC9F9yLLQmV_PmocQKzSuSw8r6MoP4IWXo27A-Katlp";
    public static final String FIREBASE_AUTH_TOKEN = "key=AAAAJ0d7nr4:APA91bHBCPz3863G26C0t8EMD2nX4PNSdMNBV68wslxLV1aPMCKZqAE66MJgZQ3Gs2wFcKTuc_Q3YoewowEmGqFVdcwd1AwiP-p2oaLzcrYD63trioYZ6dsyF5KKFCfzgn7Eqg5nXElZnYD5uIRWdaR1dc66ciFbXQ";
    ImageView image;
    String response;
    private String device_id;
    private String infoResponse;
    private  String myFireBaseToken;
    private  String toFireBaseToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle userData = getIntent().getExtras();
        if (userData != null) {
            toFireBaseToken = userData.getString("FIREBASE_TOKEN");
        }

        device_id = getDeviceId(this);
        if (device_id == null){
            device_id = saveAndGetDeviceId(this);
        }
        setContentView(R.layout.activity_make_call);
        image = (ImageView) findViewById(R.id.imageView1);
        image.setImageResource(R.drawable.icon);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetInfoTask().execute();
                new RemoteTask().execute();



            }
        });

    }

    String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .header("Authorization", FIREBASE_AUTH_TOKEN)
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    class RemoteTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            String json ="{'to' : '"
                    + toFireBaseToken
                    + "' ,'data':{'device_id': '" + device_id +"','callerFirebaseToken': '" + myFireBaseToken +  "','action' : 'IncomingCall'}}";

            try {
                JSONObject jObj = new JSONObject(json);
                response = doPostRequest("https://fcm.googleapis.com/fcm/send", jObj.toString());
            } catch (IOException e) {

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  response;

        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
            response = feed;
        }
    }

    class GetInfoTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {

            try {
                infoResponse = doGetRequest("https://iot-api.herokuapp.com/users/get_user?email=" + device_id);
                JSONObject jObj = new JSONObject(infoResponse);
                myFireBaseToken = jObj.getString("firebase_token");
                Log.e("firebase token", ">>>>>>>>>>>>>>>>>>>>" + myFireBaseToken);
            } catch (IOException e) {

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  myFireBaseToken;

        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
            Log.e("get info request", ">>>>>>>>>>>>>>>>>>>>" +feed);
            myFireBaseToken = feed;
        }
    }

    public String getDeviceId(Context context) {
        SharedPreferences settings;
        String device;
        settings = context.getSharedPreferences("DEVICE", Context.MODE_PRIVATE);
        device = settings.getString("DEVICE_ID", null);
        return device;
    }

    public String saveAndGetDeviceId(Context context) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences("DEVICE", Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("DEVICE_ID", deviceId);
        editor.commit();
        return  deviceId;
    }

}