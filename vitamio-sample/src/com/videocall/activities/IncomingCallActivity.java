package com.videocall.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.Arrays;

import io.vov.vitamio.demo.R;

public class IncomingCallActivity extends Activity {
    private GestureDetector gd;
    private  ImageView pickUpIcon;
    private  String callerDevice = "";
    private  String callerFirebaseToken= "";
    ScaleAnimation scaleAnimation;
    float x1,x2;
    // SwipeActionAdapter swipeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;

        getWindow().addFlags(flags);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            callerDevice = data.getString("CALLER_ID");
            callerFirebaseToken = data.getString("CALLER_FIREBASE_TOKEN");
            Log.e("bundle",">>>>>>>>>>>>>>>>>>>>>somethinghere" + data);
        }

        setContentView(R.layout.activity_incoming_call);
        pickUpIcon = (ImageView) findViewById(R.id.callPickUpIcon);
        pickUpIcon.setImageResource(R.drawable.pickup);
        Log.e("iINC",">>>>>>>>>>>>>>>>>>>>>INCOMING FIREBASE" + callerFirebaseToken);
        scaleAnimation = new ScaleAnimation(1/3f, 1f, 1/3f, 1.0f,  pickUpIcon.getWidth() / 2.0f,  pickUpIcon.getHeight() / 2.0f);
        scaleAnimation.setDuration(3000);
        scaleAnimation.setInterpolator(this, android.R.interpolator.accelerate_decelerate);
        pickUpIcon.startAnimation(scaleAnimation);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.e("ScaleActivity", ">>>>>>>>>>>>>>>>>>>>>>>>Scale started");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e("ScaleActivity", ">>>>>>>>>>>>>>>>>>>>>>>>>Scale ended");
                pickUpIcon.startAnimation(scaleAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }



    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN: {
                x1 = touchevent.getX();
                break;
            }
            case MotionEvent.ACTION_UP: {
                x2 = touchevent.getX();


                if (x1 < x2) {
                    Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
                    Intent startIntent = new Intent(getApplicationContext(), com.videocall.activities.MainActivity.class);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startIntent.putExtra("tag", "ACCEPT");
                    startIntent.putExtra("CALLER_ID", callerDevice);
                    startIntent.putExtra("CALLER_FIREBASE_TOKEN", callerFirebaseToken);
                    Log.e("MOT",">>>>>>>>>>>>>>>>>>>>>MOTON FIREBASE" + callerFirebaseToken);
                    getApplicationContext().startActivity(startIntent);
                }


                if (x1 > x2) {
                    Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
                    this.finish();
                }
                break;
            }
        }
        return false;
    }

}
