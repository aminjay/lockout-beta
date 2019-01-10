package edu.iastate.cs.proj_309_vc_1.lockout2;

import android.app.IntentService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.HashSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class AppLockerService extends IntentService {
    PackageManager packageManager;
    SharedPreferences sharedPref;

    public AppLockerService() {
        super("AppLockerService");
    }

    public AppLockerService(String name) {
        super(name);
    }

    Context context;

    public static volatile boolean appServiceLogic = true;
    public static volatile boolean globalServiceLogic = false;


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("AppLockerService", "onHandleIntent:Service has started.");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        globalServiceLogic = sharedPreferences.getBoolean("focused_lockout_toggle", false);

        while (true) {
            try {

                if (!appServiceLogic || !globalServiceLogic){
                    stopSelf();
                    return;
                }


                String temp = sharedPreferences.getString("notif_text","unabavilable");
                Log.d("tag999",temp);
                Thread.sleep(1000);
                sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                HashSet<String> tempSet;
                tempSet = new HashSet<String>();
                HashSet<String> tempSet2;
                tempSet2 = new HashSet<String>();
                HashSet<String> lockedappList = (HashSet<String>) sharedPref.getStringSet("LockedApps", null);
//                HashSet<String> allApps = (HashSet<String>) sharedPref.getStringSet("LockedApps", null);
                packageManager = getPackageManager();
                String appPackage = printForegroundTask();
                ApplicationInfo applicationInfo = null;
                try     {applicationInfo = packageManager.getApplicationInfo(appPackage, 0);}
                catch   (final PackageManager.NameNotFoundException e) {}
                final String appTitle = (String) ((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo) : "???");

                if (lockedappList != null) {
                    if (lockedappList.contains(appTitle)) {
                        sharedPref.edit().putString("lastApp", appTitle).commit();
                        Log.d("AppLockerService", "App name is supposed to be locked " + appTitle);
                        Intent i = new Intent();
                        i.setClass(this, LockedActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        boolean isLocked = sharedPref.getBoolean(appTitle, true);
                        if (isLocked) {startActivity(i);}
                    }
                }
                Log.d("AppLockerService", "App name is" + appTitle);
            }
            catch (InterruptedException e) {e.printStackTrace();}
        }

    }

    private String printForegroundTask() {
        String currentApp = "NULL";
        UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);}
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();}
        }
        return currentApp;
    }
}
