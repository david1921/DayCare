package com.videocall.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.videocall.activities.*;
import com.videocall.receivers.*;

import org.json.JSONException;
import org.json.JSONObject;

import io.vov.vitamio.demo.R;

public class FireMsgService extends FirebaseMessagingService {
    private static final String TAG = MainActivity.class.getName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String caller_id = remoteMessage.getData().get("device_id");
        String caller_firebase_token = remoteMessage.getData().get("callerFirebaseToken");
        String action = remoteMessage.getData().get("action");
        Log.e("ACTION", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ action);
        if (action.equals("IncomingCall")){
            Log.e("SERVI",">>>>>>>>>>>>>>>>>>>>>INCOMING FIREBASE" + caller_firebase_token);
            JSONObject obj = new JSONObject(remoteMessage.getData());


            sendNotification(caller_id,caller_firebase_token );
        }
        else if (action.equals("CallAccept")){
            Log.e("ACCPT",">>>>>>>>>>>>>>>>>>>>>ACTPT" + caller_firebase_token);
            //start Mainactivity for videoCall
            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //ADD INDICATOR TO SKIP NOTIFY
            startIntent.putExtra("SKIP_NOTIFY_CALLER", 1);
            startIntent.putExtra("CALLER_ID", caller_id);
            startIntent.putExtra("CALLER_FIREBASE_TOKEN",caller_firebase_token);

            this.startActivity(startIntent);
        }
    }

    public void sendNotification(String callerId, String callerFirebaseToken) {

        Intent intent = new Intent(this, IncomingCallActivity.class);
        intent.putExtra("CALLER_ID", callerId);
        intent.putExtra("CALLER_FIREBASE_TOKEN", callerFirebaseToken);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        Intent callAcceptIntent = new Intent(this, com.videocall.receivers.CallReceiver.class);
        callAcceptIntent.setAction("com.tutorialspoint.ACCEPT");
        callAcceptIntent.putExtra("CALLER_ID", callerId);
        callAcceptIntent.putExtra("CALLER_FIREBASE_TOKEN", callerFirebaseToken);
        Log.e("THIS", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + callerFirebaseToken);
        Intent rejectCallIntent = new Intent(this, CallReceiver.class);
        rejectCallIntent.setAction("com.tutorialspoint.REJECT");

        PendingIntent callAcceptPi = PendingIntent.getBroadcast(this, 0, callAcceptIntent, 0);
        PendingIntent rejectCallPi = PendingIntent.getBroadcast(this, 0, rejectCallIntent, 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setTicker("ticker")
                        .setFullScreenIntent(pendingIntent, true)
                        .setOngoing(true)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .addAction(R.drawable.icon, "Accept", callAcceptPi)
                        .addAction(R.drawable.icon, "Reject", rejectCallPi);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PowerManager.WakeLock screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

//later
        screenLock.release();
        mNotificationManager.notify(1, mBuilder.build());
    }
}
