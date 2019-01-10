package edu.iastate.cs.proj_309_vc_1.lockout2;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jay on 3/17/2018.
 */

public class AppCalls {

    private static String TAG = AppCalls.class.getSimpleName();
    private static String tag_string = "string_req", tag_json_arry = "jarray_req";

    public static final String MAINLINK = "http://proj-309-vc-1.cs.iastate.edu:8080";
    public static final String ADD = "http://proj-309-vc-1.cs.iastate.edu:8080/users/add?name=kram9&password=pass";
    public static final String POINTS = "http://proj-309-vc-1.cs.iastate.edu:8080/users/updatePoints?userName=kram9&points=";
    public static final String BUY = "http://proj-309-vc-1.cs.iastate.edu:8080/store/buyItem?userName=";
    public static final String STORE = "http://proj-309-vc-1.cs.iastate.edu:8080/store/findByGroupID?groupID=0";
    public static final String GROUPSTORE = "http://proj-309-vc-1.cs.iastate.edu:8080/store/findByGroupID?groupID=";
    public static final String POINTSOBJ = "http://proj-309-vc-1.cs.iastate.edu:8080/users/getPoints?userName=";
    public static final String GETITEMS = "http://proj-309-vc-1.cs.iastate.edu:8080/store/getItemsBought?userName=";
    public static final String GETGROUPS = "http://proj-309-vc-1.cs.iastate.edu:8080/social/getGroups?name=";
    public static final String GETALLGROUPS = "http://proj-309-vc-1.cs.iastate.edu:8080/social/all";

    public static final String GETGROUPLEADERBOARD = "http://proj-309-vc-1.cs.iastate.edu:8080/leaderboard/getLeaderboardFilteredGroup?groupName=";
    public static final String GETFRIENDS = "http://proj-309-vc-1.cs.iastate.edu:8080/social/getFriends?name=";
    public static final String ADDFRIEND= "http://proj-309-vc-1.cs.iastate.edu:8080/social/addFriend?username=";
    public static final String JOINGROUP= "http://proj-309-vc-1.cs.iastate.edu:8080/social/addMember?name=";
    public static final String ADDITEM = "http://proj-309-vc-1.cs.iastate.edu:8080/store/add?itemName=";


    public static String usernameStorage;
    public static String passwordStorage;
    public static String STATS;
    public static int[] statHolder = new int[100];

    public static long startTime;
    public static long endTime;
    public static long tempTime;
    public static long timeDelta;
    public static int elapsedTimeHOURS;
    public static int elapsedTimeMINUTES;
    public static int tempLength;

    public static int year;
    public static int month;
    public static int day;
    public static int hour;

    public static int progress;
    public static boolean graphState;


    public static void initData(String user, String pass){
        usernameStorage=user;
        passwordStorage=pass;
        Log.d("AppCallsLOG","PASSED THE FOLLOWING TO The REST OF THE APP: " + usernameStorage + "AND " + passwordStorage);

    }


    public static void LockoutStarted(){

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        startTime = SystemClock.elapsedRealtime();

    }

    public static void LockoutEnded(){
        endTime = SystemClock.elapsedRealtime();
        timeDelta = endTime - startTime;
        elapsedTimeHOURS = (int)(timeDelta / 1000 / 60 / 60);
        elapsedTimeMINUTES = (int)((timeDelta / 1000 / 60) - elapsedTimeHOURS*60);

        if (elapsedTimeMINUTES > 1) {

            STATS = "http://proj-309-vc-1.cs.iastate.edu:8080/stats/addstat?name=" + usernameStorage + "&password=" + passwordStorage + "&day=" + day + "&month=" + month + "&year=" + year + "&hours=" + elapsedTimeHOURS + "&time=" + hour + "&minutes=" + elapsedTimeMINUTES;
            UpdateStats();
            UpdatePoints();
        }

    }


    private static void UpdateStats() {
        Request StringReq = new StringRequest(Request.Method.GET, STATS,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
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

    private static void UpdatePoints() {
        Request StringReq = new StringRequest(Request.Method.GET,
                ("http://proj-309-vc-1.cs.iastate.edu:8080/users/updatePoints?userName=" + usernameStorage + "&points=" + (int)(timeDelta/1000/60)),

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
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
