package edu.iastate.cs.proj_309_vc_1.lockout2;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private StatsFragment statsFragment;
    private StoreFragment storeFragment;
    private ControlsFragment controlsFragment;
    private GroupAdminFragment groupAdminFragment;

    long createTime;
    long lastTime;
    int pointsToAdd;


    private String TAG = MainActivity.class.getSimpleName();
    private String tag_string = "string_req", tag_json_arry = "jarray_req";

    SharedPreferences sharedPref;

    private Context mContext;

    // basic on create listener for main activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext=this;
        NotificationHelper.enableBootReceiver(mContext);


        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        // Check if we need to display our OnboardingFragment
        if (!sharedPref.getBoolean(OnBoardingActivity.ONBOARDING_COMPLETE, false)) {
            // The user hasn't seen the OnboardingFragment yet, so show it
            startActivity(new Intent(this, OnBoardingActivity.class));
        }

//        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        else if (!sharedPref.getBoolean(LoginActivity.PREFS_LOGGED_TAG, false)){
            startActivity(new Intent(this, LoginActivity.class));
        }

        else if (sharedPref.getString(LoginActivity.PREFS_USER_TAG,null) == null || sharedPref.getString(LoginActivity.PREFS_PASS_TAG,null) == null){
            startActivity(new Intent(this, LoginActivity.class));
        }

        else {
            String tempUser=sharedPref.getString(LoginActivity.PREFS_USER_TAG, "fallbackuser");
            String tempPass=sharedPref.getString(LoginActivity.PREFS_PASS_TAG, "fallbackuser");
            AppCalls.initData(tempUser,tempPass);
            Log.d("MAIN LOG", "AUTO LOG IN SUCCESSFUL WITH THE FOLLOWING: " + AppCalls.usernameStorage + "AND " + AppCalls.passwordStorage);
        }





        pointsToAdd=0;
        createTime = System.currentTimeMillis();
        lastTime = createTime;



        mMainFrame = findViewById(R.id.main_frame);
        mMainNav = findViewById(R.id.main_nav);

        homeFragment = new HomeFragment();
        statsFragment = new StatsFragment();
        storeFragment = new StoreFragment();
        profileFragment = new ProfileFragment();
        controlsFragment = new ControlsFragment();
        groupAdminFragment = new GroupAdminFragment();


        mMainNav.setSelectedItemId(R.id.action_home);
        setFragment(homeFragment);




        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()){


                    case R.id.action_stats:
                        mMainNav.setItemBackgroundResource((R.color.primary));
                        setFragment(statsFragment);

                        return true;


                    case R.id.action_profile :
                        mMainNav.setItemBackgroundResource((R.color.primary));
                        setFragment(profileFragment);

                        return true;



                    case R.id.action_home :
                        mMainNav.setItemBackgroundResource(R.color.primary);
                        setFragment(homeFragment);

                        return true;

                    case R.id.action_store :
                        mMainNav.setItemBackgroundResource((R.color.primary));
                        setFragment(storeFragment);

                        return true;

                    case R.id.action_social:
                        mMainNav.setItemBackgroundResource((R.color.primary));
                        setFragment(controlsFragment);

                        return true;

                        default:
                            return false;

                }
            }
        });
    }

    public void startAlarm(){

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

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment).commit();

    }

    // inflates menu on press
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_items, menu);
        return true;
    }

    // skeleton code for menu items on splash page
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_a) {

            startActivity(new Intent(this, SettingsActivity.class));

            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_b) {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:lockouthelpers@gmail.com"));

            try {
                startActivity(emailIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this,"No suitable email clients found! :(",Toast.LENGTH_LONG).show();
            }

            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_c) {


            SharedPreferences.Editor editor = sharedPref.edit();
            //editor.putBoolean(LoginActivity.PREFS_LOGGED_TAG, false);
            editor.clear();
            editor.commit();
            startActivity(new Intent(this, MainActivity.class));


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void pointsPush(){

    }

    private void makeStringReq() {
        Request StringReq = new StringRequest(Request.Method.GET,
                ("http://proj-309-vc-1.cs.iastate.edu:8080/users/updatePoints?userName=testuser&points=" + pointsToAdd),

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "NUM2" + response);
                        //TotalPointsText.setText(response);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //TotalPointsText.setText(error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "Androidhive");
                params.put("email", "abc@androidhive.info");
                params.put("pass", "password123");

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(StringReq,
                tag_string);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }


}
