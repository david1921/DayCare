package com.videocall.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.videocall.activities.*;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class CallReceiver extends BroadcastReceiver {
    NotificationManager mNotificationManager;
    public CallReceiver() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        mNotificationManager = (NotificationManager)  context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bundle data = intent.getExtras();

        String call = intent.getAction();
        Log.e("REC1", ">>>>>>>>>>>>>>>>>>>>>>>>>>>" + call);
        if(call == "com.tutorialspoint.ACCEPT")
        {
            String callerDevice = "";
            String callerFirebaseToken= "";
            mNotificationManager.cancel(1);
            JSONObject obj =null;
            String frtoken="";
            if (data != null) {
                String json = data.toString();
                try {
                    obj = new JSONObject(json);
                    frtoken = obj.getString("CALLER_FIREBASE_TOKEN");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("REC1", ">>>>>>>>>>>>>>>>>>>>>>>>>>>" +frtoken);
                callerDevice = data.getString("CALLER_ID");
                callerFirebaseToken = data.getString("CALLER_FIREBASE_TOKEN");
            }
            Log.e("REC2",">>>>>>>>>>>>>>>>>>>>>>>>>>>" + callerFirebaseToken);
            Intent startIntent = new Intent(context, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startIntent.putExtra("tag", "ACCEPT");
            startIntent.putExtra("CALLER_ID", callerDevice);
            startIntent.putExtra("CALLER_FIREBASE_TOKEN", callerFirebaseToken);
            context.startActivity(startIntent);
        }
        else if(call == "com.tutorialspoint.REJECT")
        {
            mNotificationManager.cancel(1);
        }
    }
}
