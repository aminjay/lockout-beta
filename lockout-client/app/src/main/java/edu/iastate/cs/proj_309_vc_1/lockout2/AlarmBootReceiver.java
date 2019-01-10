package edu.iastate.cs.proj_309_vc_1.lockout2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AlarmBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String tempString = sharedPreferences.getString("notif_time", "10:10");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            NotificationHelper.scheduleRepeatingRTCNotification(context,tempString);
        }
    }
}
