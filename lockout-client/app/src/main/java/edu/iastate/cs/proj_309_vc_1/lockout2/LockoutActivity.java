package edu.iastate.cs.proj_309_vc_1.lockout2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class LockoutActivity extends AppCompatActivity {

    //@Bind(R.id.btn_leavelockout) Button _leavelockoutButton;

    NotificationManager notificationManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockout);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);



        Intent tempIntent = new Intent(this,AppLockerService.class);
        startService(tempIntent);

        AppCalls.LockoutStarted();
        /*
        _leavelockoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppCalls.LockoutEnded();
                //finish();
            }
        });*/

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean tempModeStatus = sharedPreferences.getBoolean("focused_lockout_toggle", false);

        String mode="BASIC";
        if (tempModeStatus){
            mode="FOCUSED";
        }


        notifyNotification(0,mode);







        Button leaveLockout = findViewById(R.id.btn_leavelockout);
        leaveLockout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                AppCalls.LockoutEnded();

                cancelNotification();
                finish();
            }
        });

        final ProgressBar progressBar = findViewById(R.id.progBar);
        final TextView pointsDisplay = findViewById(R.id.pointsDisplay);
        AppCalls.progress = 0;


//        final ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress",0,60);
//        animation.setDuration(60000);
//        animation.setInterpolator(new DecelerateInterpolator());
//        animation.start();


        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (AppCalls.progress==60){
                                    AppCalls.progress=1;}
                                else{
                                    AppCalls.progress += 1;
                                }

                                AppCalls.tempTime= SystemClock.elapsedRealtime();
                                long tempDelta = AppCalls.tempTime-AppCalls.startTime;
                                int fakePoints = (int)(tempDelta/1000/60);


                                progressBar.setProgress(AppCalls.progress);
                                pointsDisplay.setText("Points Earned: " + fakePoints);

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();



    }





    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //pointsToAdd=0;
        //resumeTime = System.currentTimeMillis();
        //lastTime=resumeTime;

    }

    @Override
    protected void onPause() {
        super.onPause();
        //pauseTime = System.currentTimeMillis();
        //firstTime = pauseTime;
        //pointsToAdd = ((int)(firstTime - lastTime) / 10000);
        //makeStringReq();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //stopTime = System.currentTimeMillis();
        //firstTime = stopTime;
        //pointsToAdd = ((int)(firstTime - lastTime) / 10000);
        //makeStringReq();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //destroyTime = System.currentTimeMillis();
        //firstTime = destroyTime;
        //pointsToAdd = ((int)(firstTime - lastTime) / 10000);
        //makeStringReq();
    }

    @Override
    public void onBackPressed() {

        AppCalls.LockoutEnded();
        cancelNotification();
        finish();
    }

    private void notifyNotification(double points, String mode) {


        AppLockerService.appServiceLogic=true;

        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default", "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        Notification n= new Notification.Builder(this,"default")
                .setContentTitle("LOCKOUT MODE: " + mode)
                .setContentText("You are currently earning points")
                .setSmallIcon(R.drawable.ic_lock_closed)
                //.setNumber(5)
                //.setSmallIcon(R.mipmap.ic_launcher_round)
                .setSound(null)
                .setAutoCancel(true)
                .setOngoing(true)
                .build();

        notificationManager.notify(101, n);



    }

    private void cancelNotification() {
        AppLockerService.appServiceLogic=false;
        Intent i = new Intent(this,AppLockerService.class);
        stopService(i);
        notificationManager.cancel(101);
    }


}
