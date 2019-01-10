package edu.iastate.cs.proj_309_vc_1.lockout2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    NotificationManager notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Get notification manager to manage/send notifications
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean tempModeStatus = sharedPreferences.getBoolean("notif_switch", false);
        String tempStringStatus = sharedPreferences.getString("notif_text", "Stay focused!");

        if(tempModeStatus){
            Intent intentToRepeat = new Intent(context, MainActivity.class);
            intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(intentToRepeat);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            if (Build.VERSION.SDK_INT < 26) {return;}
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("default", "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

            Notification n= new Notification.Builder(context,"default")
                    .setContentTitle("Here's a message for you:")
                    .setContentText(tempStringStatus)
                    .setSmallIcon(R.drawable.ic_face_black_24dp)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    //.setOngoing(true)
                    .build();
            notificationManager.notify(1, n);
        }
        else{Log.d("LOG4","mode is off");}
    }
}
