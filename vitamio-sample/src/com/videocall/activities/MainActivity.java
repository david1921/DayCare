package com.videocall.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.twilio.common.TwilioAccessManager;
import com.twilio.common.TwilioAccessManagerFactory;
import com.twilio.common.TwilioAccessManagerListener;
import com.twilio.conversations.AudioOutput;
import com.twilio.conversations.AudioTrack;
import com.twilio.conversations.CameraCapturer;
import com.twilio.conversations.CameraCapturerFactory;
import com.twilio.conversations.CapturerErrorListener;
import com.twilio.conversations.CapturerException;
import com.twilio.conversations.Conversation;
import com.twilio.conversations.ConversationCallback;
import com.twilio.conversations.ConversationListener;
import com.twilio.conversations.ConversationsClient;
import com.twilio.conversations.ConversationsClientListener;
import com.twilio.conversations.IncomingInvite;
import com.twilio.conversations.LocalMedia;
import com.twilio.conversations.LocalMediaFactory;
import com.twilio.conversations.LocalMediaListener;
import com.twilio.conversations.LocalVideoTrack;
import com.twilio.conversations.LocalVideoTrackFactory;
import com.twilio.conversations.MediaTrack;
import com.twilio.conversations.OutgoingInvite;
import com.twilio.conversations.Participant;
import com.twilio.conversations.ParticipantListener;
import com.twilio.conversations.TwilioConversations;
import com.twilio.conversations.TwilioConversationsException;
import com.twilio.conversations.VideoRendererObserver;
import com.twilio.conversations.VideoTrack;
import com.twilio.conversations.VideoViewRenderer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.twilio.common.TwilioAccessManager;
import com.twilio.common.TwilioAccessManagerFactory;
import com.twilio.common.TwilioAccessManagerListener;
import com.twilio.conversations.AudioOutput;
import com.twilio.conversations.AudioTrack;
import com.twilio.conversations.CameraCapturer;
import com.twilio.conversations.CameraCapturerFactory;
import com.twilio.conversations.CapturerErrorListener;
import com.twilio.conversations.CapturerException;
import com.twilio.conversations.Conversation;
import com.twilio.conversations.ConversationCallback;
import com.twilio.conversations.ConversationListener;
import com.twilio.conversations.ConversationsClient;
import com.twilio.conversations.ConversationsClientListener;
import com.twilio.conversations.IncomingInvite;
import com.twilio.conversations.LocalMedia;
import com.twilio.conversations.LocalMediaFactory;
import com.twilio.conversations.LocalMediaListener;
import com.twilio.conversations.LocalVideoTrack;
import com.twilio.conversations.LocalVideoTrackFactory;
import com.twilio.conversations.MediaTrack;
import com.twilio.conversations.OutgoingInvite;
import com.twilio.conversations.Participant;
import com.twilio.conversations.ParticipantListener;
import com.twilio.conversations.TwilioConversations;
import com.twilio.conversations.TwilioConversationsException;
import com.twilio.conversations.VideoRendererObserver;
import com.twilio.conversations.VideoTrack;
import com.twilio.conversations.VideoViewRenderer;
import com.videocall.receivers.CallReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import io.vov.vitamio.demo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String mAccessToken;
    private static final String TAG = MainActivity.class.getName();
    /*
    * Android application UI elements
    */
    private FrameLayout previewFrameLayout;
    private ViewGroup localContainer;
    private ViewGroup participantContainer;
    private FloatingActionButton callActionFab;
    private OkHttpClient client = new OkHttpClient();

    private TwilioAccessManager accessManager;
    private ConversationsClient conversationsClient;
    private CameraCapturer cameraCapturer;

    private Conversation conversation;
    private OutgoingInvite outgoingInvite;
    private Context mContext;
    private Serializable sObj;
    public static final String FIREBASE_AUTH_TOKEN = "key=AAAAJ0d7nr4:APA91bHBCPz3863G26C0t8EMD2nX4PNSdMNBV68wslxLV1aPMCKZqAE66MJgZQ3Gs2wFcKTuc_Q3YoewowEmGqFVdcwd1AwiP-p2oaLzcrYD63trioYZ6dsyF5KKFCfzgn7Eqg5nXElZnYD5uIRWdaR1dc66ciFbXQ";
    /*
    * A VideoViewRenderer receives frames from a local or remote video track and renders the frames to a
    */
    String caller_firebase_token = "";
    private VideoViewRenderer participantVideoRenderer;
    private VideoViewRenderer localVideoRenderer;
    private boolean accepted = false;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private String responseId;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing.

     */
    private GoogleApiClient client2;
    private String device_id;
    private String acceptResponse;
    private int caller = 0;
    private int skip_notify_caller = 0;
    private String participant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
        device_id = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Bundle data = getIntent().getExtras();
        if (data != null) {

            caller_firebase_token = data.getString("CALLER_FIREBASE_TOKEN");
            String caller_id = data.getString("CALLER_ID");
            participant = caller_id;
            skip_notify_caller = data.getInt("SKIP_NOTIFY_CALLER");

            Log.e("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>CALLER "+ caller_id);
        }

        int flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;

        getWindow().addFlags(flags);
        setContentView(R.layout.activity_main);

   /*
    * Check camera and microphone permission.
    */
        if (!checkPermissionForCameraAndMicrophone()) {
            requestPermissionForCameraAndMicrophone();
        }

        previewFrameLayout = (FrameLayout) findViewById(R.id.previewFrameLayout);
        localContainer = (ViewGroup) findViewById(R.id.localContainer);
        participantContainer = (ViewGroup) findViewById(R.id.participantContainer);

        callActionFab = (FloatingActionButton) findViewById(R.id.call_action_fab);
        callActionFab.setOnClickListener(callActionFabClickListener());
        getCapabilityToken();

        Toast.makeText(this,"ONCREATE",Toast.LENGTH_LONG);

        //initializeTwilioSdk(mAccessToken);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }


    private void getCapabilityToken() {
        try {Log.e(TAG + ">>>>>>>>>>>>>>>>>>>>>>", "my identity " + device_id);
            run("http://cryptic-mesa-46379.herokuapp.com/token?name=" + device_id, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String token = response.body().string();
                        JSONObject obj = new JSONObject(token);
                        mAccessToken = obj.getString("token");
                        Log.e(TAG + ">>>>>>>>>>>>>>>>>>>>>>", "WE GOT OUR TOKEN" + token);
                        initializeTwilioSdk(mAccessToken);
                        //save credentials to database
                        //we dont even get here
                        Log.e("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>>>>> WE FINISHED INITIALIZATION " + caller);

                        new PostIdentityTask().execute(mAccessToken);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeTwilioSdk(final String accessToken) {
        TwilioConversations.setLogLevel(TwilioConversations.LogLevel.DEBUG);
        if (!TwilioConversations.isInitialized()) {
            TwilioConversations.initialize(this.mContext, new TwilioConversations.InitListener() {
                @Override
                public void onInitialized() {
                    Log.e("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> WE ARE INITIALIZING NOW");
                    accessManager = TwilioAccessManagerFactory.createAccessManager(accessToken, accessManagerListener());
                    conversationsClient = TwilioConversations.createConversationsClient(accessManager, conversationsClientListener());
                    // Specify the audio output to use for this conversation client
                    conversationsClient.setAudioOutput(AudioOutput.SPEAKERPHONE);

                    // Initialize the camera capturer and start the camera preview
                    cameraCapturer = CameraCapturerFactory.createCameraCapturer(MainActivity.this, CameraCapturer.CameraSource.CAMERA_SOURCE_FRONT_CAMERA, previewFrameLayout, capturerErrorListener());
                    startPreview();

                    //conversattionCient is valid here but returns 'invalid participant call'
                    conversationsClient.listen();
                    //IF WE MADE THE CALL SKIP THIS
                    if ( skip_notify_caller!= 1) {
                        //if im not the caller tell the caller we are ready for the convo
                        Log.e("TOK",">>>>>>>>>>>>>>>>>>>>>TOKEN" + caller_firebase_token);
                        new NotifyAcceptCallTask().execute(caller_firebase_token);

                    }else{
                        caller = 1;
                    }


                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(MainActivity.this,
                            "Failed to initialize the Twilio Conversations SDK",
                            Toast.LENGTH_LONG).show();
                    Log.e("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>> FAILED TO INITIALIZED");
                }
            });
        }

    }

    private void startPreview() {
        cameraCapturer.startPreview();
        Log.e("TAG", ">>>>>>>>>>>>>>>>>>>>>>>> WE ARE PREVIEWING");
    }

    private void stopPreview() {
        if (cameraCapturer != null && cameraCapturer.isPreviewing()) {
            cameraCapturer.stopPreview();
        }
    }

    private void reset() {
        Log.e("TAG", ">>>>>>>>>>>>>>>>>>>>>>>> WE ARE in RESET");
        if (participantVideoRenderer != null) {
            participantVideoRenderer = null;
        }
        localContainer.removeAllViews();
        participantContainer.removeAllViews();

        if (conversation != null) {
            conversation.dispose();
            conversation = null;
        }
        outgoingInvite = null;

        if (conversationsClient != null) {
            conversationsClient.setAudioOutput(AudioOutput.HEADSET);
        }
        setCallAction();
        startPreview();
    }

    private void hangup() {
        if (conversation != null) {
            conversation.disconnect();
        } else if (outgoingInvite != null) {
            outgoingInvite.cancel();
        }
    }

    private void setHangupAction() {
        callActionFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.holo_red_dark)));
        callActionFab.show();
        callActionFab.setOnClickListener(hangupActionFabClickListener());
    }

    private LocalMedia setupLocalMedia() {
        LocalMedia localMedia = LocalMediaFactory.createLocalMedia(localMediaListener());
        LocalVideoTrack localVideoTrack = LocalVideoTrackFactory.createLocalVideoTrack(cameraCapturer);
        localMedia.addLocalVideoTrack(localVideoTrack);
        return localMedia;
    }

    private void setCallAction() {
        callActionFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.holo_green_dark)));
        callActionFab.show();
        callActionFab.setOnClickListener(callActionFabClickListener());
    }

    /*
    * UTILITY FUNCTIONS
    */

    private boolean checkPermissionForCameraAndMicrophone() {
        int resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return (resultCamera == PackageManager.PERMISSION_GRANTED) && (resultMic == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissionForCameraAndMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(this, "Camera and Microphone permissions needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    private Call run(String url, Callback callback) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call response = client.newCall(request);
        response.enqueue(callback);
        return response;
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

    String doFcmRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .header("Authorization", FIREBASE_AUTH_TOKEN)
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /*
    * EVENT LISTENERS
    */
    private TwilioAccessManagerListener accessManagerListener() {
        return new TwilioAccessManagerListener() {
            @Override
            public void onAccessManagerTokenExpire(TwilioAccessManager twilioAccessManager) {
            }

            @Override
            public void onTokenUpdated(TwilioAccessManager twilioAccessManager) {
            }

            @Override
            public void onError(TwilioAccessManager twilioAccessManager, String s) {
            }
        };
    }

    private ConversationsClientListener conversationsClientListener() {
        return new ConversationsClientListener() {
            @Override
            public void onStartListeningForInvites(ConversationsClient conversationsClient) {
            }

            @Override
            public void onStopListeningForInvites(ConversationsClient conversationsClient) {
            }

            @Override
            public void onFailedToStartListening(ConversationsClient conversationsClient, TwilioConversationsException e) {
            }

            @Override
            public void onIncomingInvite(ConversationsClient conversationsClient, IncomingInvite incomingInvite) {
                Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>incoming call");

                if (conversation == null ) {
                    Log.e("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>INCONVERSATION");
                    LocalMedia localMedia = setupLocalMedia();
                    incomingInvite.accept(localMedia, new ConversationCallback() {
                        @Override
                        public void onConversation(Conversation conversation, TwilioConversationsException e) {
                            if (e == null) {
                                MainActivity.this.conversation = conversation;
                                conversation.setConversationListener(conversationListener());
                            } else {
                                Log.e(TAG, e.getMessage());
                                hangup();
                                reset();
                            }
                        }
                    });
                    setHangupAction();
                } else {
                    Log.w(TAG, String.format("Conversation in progress. Invite from %s ignored", incomingInvite.getInvitee()));
                }
            }


            @Override
            public void onIncomingInviteCancelled(ConversationsClient conversationsClient, IncomingInvite incomingInvite) {

            }
        };
    }

    private CapturerErrorListener capturerErrorListener() {
        return new CapturerErrorListener() {
            @Override
            public void onError(CapturerException e) {
                Log.e(TAG, "Camera capturer error:" + e.getMessage());
            }
        };
    }

    private View.OnClickListener hangupActionFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hangup();
                setCallAction();
            }
        };
    }

    private ConversationListener conversationListener() {
        return new ConversationListener() {

            @Override
            public void onFailedToConnectParticipant(Conversation conversation, Participant participant, TwilioConversationsException e) {
                Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>> onFailedToConnectParticipan" + e);
            }

            @Override
            public void onConversationEnded(Conversation conversation, TwilioConversationsException e) {
                reset();
            }

            @Override
            public void onParticipantConnected(Conversation conversation, Participant participant) {
                Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>> onParticipantConnected: Participant connected");
                participant.setParticipantListener(participantListener());
            }

            @Override
            public void onParticipantDisconnected(Conversation conversation, Participant participant) {
                reset();
            }
        };
    }

    private ParticipantListener participantListener() {

        return new ParticipantListener() {
            @Override
            public void onVideoTrackAdded(Conversation conversation, Participant participant, VideoTrack videoTrack) {
                Log.e(TAG, ">>>>>>>>> onVideoTrackAdded >>>>>>>>>> " + participant.getIdentity());

                // Remote participant
                participantVideoRenderer = new VideoViewRenderer(MainActivity.this, participantContainer);
                participantVideoRenderer.setObserver(new VideoRendererObserver() {

                    @Override
                    public void onFirstFrame() {
                        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Participant onFirstFrame");
                    }

                    @Override
                    public void onFrameDimensionsChanged(int width, int height, int i2) {
                        Log.i(TAG, "Participant onFrameDimensionsChanged " + width + " " + height);
                    }

                });
                videoTrack.addRenderer(participantVideoRenderer);
            }

            @Override
            public void onVideoTrackRemoved(Conversation conversation, Participant participant, VideoTrack videoTrack) {
                participantContainer.removeAllViews();

            }

            @Override
            public void onAudioTrackAdded(Conversation conversation, Participant participant, AudioTrack audioTrack) {

            }

            @Override
            public void onAudioTrackRemoved(Conversation conversation, Participant participant, AudioTrack audioTrack) {

            }

            @Override
            public void onTrackEnabled(Conversation conversation, Participant participant, MediaTrack mediaTrack) {

            }

            @Override
            public void onTrackDisabled(Conversation conversation, Participant participant, MediaTrack mediaTrack) {

            }
        };
    }

    private LocalMediaListener localMediaListener() {
        return new LocalMediaListener() {
            @Override
            public void onLocalVideoTrackAdded(LocalMedia localMedia, LocalVideoTrack localVideoTrack) {
                localVideoRenderer = new VideoViewRenderer(MainActivity.this, localContainer);
                localVideoTrack.addRenderer(localVideoRenderer);
            }

            @Override
            public void onLocalVideoTrackRemoved(LocalMedia localMedia, LocalVideoTrack localVideoTrack) {
                localContainer.removeAllViews();
            }

            @Override
            public void onLocalVideoTrackError(LocalMedia localMedia, LocalVideoTrack localVideoTrack, TwilioConversationsException e) {
                Log.e(TAG, e.getMessage());
            }
        };
    }

    private View.OnClickListener callActionFabClickListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>conversation client " + conversationsClient);
                if (conversationsClient != null) {
                    stopPreview();

                    Set<String> participants = new HashSet<>();
                    participants.add(participant);

                    // Create local media
                    LocalMedia localMedia = setupLocalMedia();

                    outgoingInvite = conversationsClient.sendConversationInvite(participants, localMedia, new ConversationCallback() {
                        @Override
                        public void onConversation(Conversation conversation, TwilioConversationsException e) {
                            if (e == null) {
                                // Participant has accepted invite, we are in active conversation
                                MainActivity.this.conversation = conversation;
                                conversation.setConversationListener(conversationListener());
                                setHangupAction();
                            } else {
                                Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>didnt accept call" + e);
                                hangup();
                                reset();
                            }
                        }
                    });
                } else {

                    Log.e(TAG, "invalid participant call");
                }
            }
        };
    }

    public void makeCall(){
        //conversation client is null here
        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>my conversation client " + conversationsClient);
        if (conversationsClient != null) {
            stopPreview();

            Set<String> participants = new HashSet<>();
            participants.add(participant);
            Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>participant added " + participant);
            // Create local media
            LocalMedia localMedia = setupLocalMedia();
            Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>about to make a call to" + participant);
            outgoingInvite = conversationsClient.sendConversationInvite(participants, localMedia, new ConversationCallback() {
                @Override
                public void onConversation(Conversation conversation, TwilioConversationsException e) {
                    if (e == null) {
                        // Participant has accepted invite, we are in active conversation
                        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>client accepted");
                        MainActivity.this.conversation = conversation;
                        conversation.setConversationListener(conversationListener());
                        setHangupAction();
                    } else {
                        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>didnt accept call" + e);
                        hangup();
                        reset();
                    }
                }
            });
        } else {

            Log.e(TAG, "invalid participant call");
        }
    }
    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Log.e(TAG, ">>>>>>>>>>>>>>>> O NEW INTENT" + extras);

        if (extras != null) {
            accepted = true;
            Log.e(TAG, ">>>>>>>>>>>>>>>>call accepted ");

        } else {
            Log.d("TEMP", "Extras are NULL");

        }
    }

    public void sendNotification() {

//Get an instance of NotificationManager//
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("screen", "debugScreen");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        Intent callAcceptIntent = new Intent(this, CallReceiver.class);
        callAcceptIntent.setAction("com.tutorialspoint.CUSTOM_INTENT");

        Intent rejectCallIntent = new Intent(this, CallReceiver.class);
        rejectCallIntent.setAction("com.tutorialspoint.REJECT");

        PendingIntent callAcceptPi = PendingIntent.getBroadcast(this, 0, callAcceptIntent, 0);
        PendingIntent rejectCallPi = PendingIntent.getBroadcast(this, 0, rejectCallIntent, 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>displaying notification");
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


    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(this,"ONSTART",Toast.LENGTH_LONG);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://io.vov.vitamio.demo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);



    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG , ">>>>>>>>>>>>>>>>>>>>>> ON PAUSE");
        int flags;
        if (caller == 1){
            keepScreenOn();
            makeCall();
        }
    }
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generatedlement the App Indexing APi
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is corct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://io.vov.vitamio.demo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putSerializable("starttime", sObj);
    }

    class PostIdentityTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {


            String json ="{'email' : '"
                    + device_id
                    + "' ,'twilio_token': '" + urls[0] + "'}";

            try {
                JSONObject jObj = new JSONObject(json);
                responseId = doPostRequest("https://iot-api.herokuapp.com/users", jObj.toString());
            } catch (IOException e) {

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  responseId;

        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    class NotifyAcceptCallTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            String json ="{'to' : '"
                    + urls[0]
                    + "' ,'data':{'device_id': '" + device_id + "','action' : 'CallAccept'}}";


            try {
                JSONObject jObj = new JSONObject(json);
                acceptResponse = doFcmRequest("https://fcm.googleapis.com/fcm/send", jObj.toString());
            } catch (IOException e) {

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  acceptResponse;

        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
            Log.e("notify", ">>>>>>>>>>>>>>>>>>>>" +feed);
            acceptResponse = feed;
        }
    }

    public void keepScreenOn(){
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire();
    }


}


