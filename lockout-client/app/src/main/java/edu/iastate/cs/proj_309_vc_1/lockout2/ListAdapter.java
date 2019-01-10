package edu.iastate.cs.proj_309_vc_1.lockout2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    private List<PackageInfo> packageList;
    private Activity context;
    private PackageManager packageManager;
    private boolean[] itemChecked;


    public ListAdapter(Activity context, List<PackageInfo> packageList,
                       PackageManager packageManager) {
        super();
        this.context = context;
        this.packageList = packageList;
        this.packageManager = packageManager;
        itemChecked = new boolean[packageList.size()];
    }

    public int getCount() {
        return packageList.size();
    }

    public Object getItem(int position) {
        return packageList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();

            holder.apkName = (TextView) convertView
                    .findViewById(R.id.textView1);
            holder.ck1 = (CheckBox) convertView
                    .findViewById(R.id.checkBox1);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        PackageInfo packageInfo = (PackageInfo) getItem(position);

        Drawable appIcon = packageManager
                .getApplicationIcon(packageInfo.applicationInfo);
        String appName = packageManager.getApplicationLabel(
                packageInfo.applicationInfo).toString();
        appIcon.setBounds(0, 0, 80, 80);
        holder.apkName.setCompoundDrawables(appIcon, null, null, null);
        holder.apkName.setCompoundDrawablePadding(15);
        holder.apkName.setText(appName);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        HashSet<String> tempSet;
        tempSet = new HashSet<String>();
        HashSet<String> lockedappList = (HashSet<String>) sharedPref.getStringSet("LockedApps", tempSet);
        if (lockedappList != null) {
            if (lockedappList.contains(appName)) {
                itemChecked[position] = true;

            } else
                itemChecked[position] = false;
        } else {
            holder.ck1.setChecked(false);
        }
        if (itemChecked[position]) {

            holder.ck1.setChecked(true);
        } else {
            holder.ck1.setChecked(false);
        }

        holder.ck1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (holder.ck1.isChecked()) {
                    itemChecked[position] = true;
                } else
                    itemChecked[position] = false;
            }
        });

        return convertView;

    }

    private class ViewHolder {
        TextView apkName;
        CheckBox ck1;
    }


}

