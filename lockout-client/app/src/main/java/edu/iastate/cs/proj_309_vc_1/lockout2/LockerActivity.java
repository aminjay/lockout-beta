package edu.iastate.cs.proj_309_vc_1.lockout2;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class LockerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView apps;
    PackageManager packageManager;
    TreeSet<String> checkedValue;
    SharedPreferences sharedPref;
    HashSet<String> tempSet, lockedappList;
    HashSet<String> allApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locker);
        apps = (ListView) findViewById(R.id.listView1);


        tempSet = new HashSet<String>();
        checkedValue = new TreeSet<String>();
        checkedValue.clear();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();


        lockedappList = (HashSet<String>) sharedPref.getStringSet("LockedApps", tempSet);
        editor.putBoolean("PREFS_FOCUS_ENABLE", true);
        editor.apply();


//        Intent intent = new Intent();
//        intent.setAction("com.test.SEND");
//        sendBroadcast(intent);




        packageManager = getPackageManager();

        final List<PackageInfo> packageList = packageManager
                .getInstalledPackages(PackageManager.GET_META_DATA); // all apps in the phone
        final List<PackageInfo> packageList1 = packageManager
                .getInstalledPackages(0);

        try {
            packageList1.clear();
            for (int n = 0; n < packageList.size(); n++) {

                PackageInfo PackInfo = packageList.get(n);
                if (((PackInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) != true)
                //check weather it is system app or user installed app
                {
                    try {

                        packageList1.add(packageList.get(n)); // add in 2nd list if it is user installed app
                        Collections.sort(packageList1, new Comparator<PackageInfo>()
                                // this will sort App list on the basis of app name
                        {
                            public int compare(PackageInfo o1, PackageInfo o2) {
                                return o1.applicationInfo.loadLabel(getPackageManager()).toString()
                                        .compareToIgnoreCase(o2.applicationInfo.loadLabel(getPackageManager())
                                                .toString());// compare and return sorted packagelist.
                            }
                        });


                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        allApps = new HashSet<String>();
        for (PackageInfo inf : packageList1) {
            String appName = packageManager.getApplicationLabel(
                    inf.applicationInfo).toString();

            allApps.add(appName);
        }
        sharedPref.edit().putStringSet("allApps", allApps).apply();

        android.widget.ListAdapter Adapter = new ListAdapter(this, packageList1, packageManager);
        apps.setAdapter(Adapter);
        apps.setOnItemClickListener(this);


    }


    @Override
    public void onItemClick(AdapterView arg0, View v, int position, long arg3) {
        // TODO Auto-generated method stub
        CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox1);
        cb.performClick();
        TextView tv = (TextView) v.findViewById(R.id.textView1);


        //Log.d("MAIN ACTIBVI", "Checkbox is clicked on item" + tv.getText().toString());


        if (cb.isChecked()) {
            lockedappList.add(tv.getText().toString());

            Log.d("MAIN ACTIBVI", "Adding " + tv.getText().toString());


        } else if (!cb.isChecked()) {
            lockedappList.remove(tv.getText().toString());

            Log.d("MAIN ACTIBVI", "Removing " + tv.getText().toString());
        }
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.remove("LockedApps");
        editor.commit();
        editor.putStringSet("LockedApps", lockedappList).commit();
        for (String s : lockedappList) {
            editor.putBoolean(s, true).apply();
        }


    }


}
