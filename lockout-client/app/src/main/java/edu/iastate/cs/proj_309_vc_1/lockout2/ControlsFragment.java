package edu.iastate.cs.proj_309_vc_1.lockout2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.iastate.cs.proj_309_vc_1.lockout2.AppCalls;
import edu.iastate.cs.proj_309_vc_1.lockout2.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class ControlsFragment extends Fragment implements OnClickListener, AdapterView.OnItemSelectedListener {

    public class Group {
        int id;
        String groupName;
    }

    public ControlsFragment() {
        // Required empty public constructor
    }
    //@Bind(R.id.PointTotal)
    Button Confirm;

    private String TAG = ControlsFragment.class.getSimpleName();
    //private String url = "http://proj-309-vc-1.cs.iastate.edu:8080/users/updatePoints?userName=" + AppCalls.usernameStorage + "&points=" + "0";
    String tempUsernameStorage = AppCalls.usernameStorage;
    String urlGroupLeaderboard = AppCalls.GETGROUPLEADERBOARD;
    String url = "http://proj-309-vc-1.cs.iastate.edu:8080/users/updatePoints?userName=" + AppCalls.usernameStorage + "&points=0";
    String urlGroups = "http://proj-309-vc-1.cs.iastate.edu:8080/social/getGroups?name=" + AppCalls.usernameStorage;
    String urlFriendsList = AppCalls.GETFRIENDS;
    String urlGroupsList = AppCalls.GETALLGROUPS;

    @Bind(R.id.input_groupname) EditText _groupnameText;
    @Bind(R.id.input_friendname) EditText _friendnameText;

    Group[] groups = new Group[10];

    private String tag_string = "string_req", tag_json_arry = "jarray_req", tag_json_obj = "jobj_req";
    TextView[] txt = new TextView[100];
    private ScrollView sv;
    private LinearLayout ll;
    private View myInflatedView;
    private Spinner spSimpleSpinner;
    private ArrayList<String> namesList = new ArrayList<String>();
    private ArrayAdapter adapter;
    private int currentId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        myInflatedView = inflater.inflate(R.layout.fragment_controls, container, false);
        sv = myInflatedView.findViewById(R.id.layout);
        ll = sv.findViewById(R.id.layout1);
        ButterKnife.bind(this, myInflatedView);


//        TextView defaultText = myInflatedView.findViewById((R.id.availablePoints));
//        String userDisplay = LoginActivity.currentUser;
//        String tempText = "Available points = " + userDisplay;
//        defaultText.setText(tempText);

        currentId = 0;

        for(int i = 0; i < groups.length; i++) {
            groups[i] = new Group();
            groups[i].groupName = "";
        }

        spSimpleSpinner = (Spinner) myInflatedView.findViewById(R.id.spSimpleSpinner3);

        if(namesList.contains("Groups") == false) {
            namesList.add("Groups");
            namesList.add("Friends");
        }

        urlGroups = AppCalls.GETGROUPS + AppCalls.usernameStorage;
        makeJsonArryReq2();
        adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,namesList);
        spSimpleSpinner.setAdapter(adapter);
        spSimpleSpinner.setOnItemSelectedListener(this);

        for(int i = 0; i < txt.length; i++) {
            txt[i] = new Button(getActivity());
            txt[i].setTextColor(getResources().getColor(R.color.white));

            txt[i].setBackgroundColor(getResources().getColor(R.color.primary));
            txt[i].setText("blank");
            ll.addView(txt[i]);
            txt[i].setVisibility(View.GONE);
        }



        Confirm = myInflatedView.findViewById(R.id.Confirm);
        makeStringReq();
        //string request will also make a jsonobjreq that gets available points










        return myInflatedView;
    }

    private void makeStringReq( ) {
        Request StringReq = new StringRequest(Request.Method.GET,
                url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        //defaultText.setText(response);
                        url = AppCalls.POINTSOBJ + AppCalls.usernameStorage;
                        makeJsonObjReq();

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
             */
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

        AppController.getInstance().addToRequestQueue(StringReq,
                tag_string);
    }


    private void makeJsonArryReq3() {
        JsonArrayRequest req = new JsonArrayRequest(urlGroupLeaderboard,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        String item_name = "";
                        int points = 0;
                        for(int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject mJsonObjectProperty = response.getJSONObject(i);
                                item_name = mJsonObjectProperty.getString("userName");
                                points = mJsonObjectProperty.getInt("totalPoints");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            txt[i].setText(item_name + " " + points + " pts");
                            txt[i].setOnClickListener(ControlsFragment.this);
                            txt[i].setVisibility(View.VISIBLE);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
    }



    private void makeJsonArryReq2() {
        JsonArrayRequest req = new JsonArrayRequest(urlGroups,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        String item_name = "";
                        int id = 0;
                        for(int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject mJsonObjectProperty = response.getJSONObject(i);
                                item_name = mJsonObjectProperty.getString("groupName");
                                id = mJsonObjectProperty.getInt("id");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(namesList.contains(item_name) == false) {
                                namesList.add(item_name);
                            }
                            groups[i].groupName = item_name;
                            groups[i].id = id;

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
    }


    /**
     * Making json array request
     * */
    private void makeJsonArryReq() {
        JsonArrayRequest req = new JsonArrayRequest(urlFriendsList,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        String item_name = "BLANK";
                        for(int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject mJsonObjectProperty = response.getJSONObject(i);
                                item_name = mJsonObjectProperty.getString("userName");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            txt[i].setText(item_name);
                            txt[i].setVisibility(View.VISIBLE);
                            txt[i].setOnClickListener(ControlsFragment.this);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
    }

    // Adding request to request queue

    /**
     * Making json array request
     * */
    private void makeJsonArryReq4() {
        JsonArrayRequest req = new JsonArrayRequest(urlGroupsList,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        String item_name = "BLANK";
                        for(int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject mJsonObjectProperty = response.getJSONObject(i);
                                item_name = mJsonObjectProperty.getString("groupName");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            txt[i].setText(item_name);
                            txt[i].setVisibility(View.VISIBLE);
                            txt[i].setOnClickListener(ControlsFragment.this);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
    }

    // Adding request to request queue





    /**
     * Making json object request
     * */
    private void makeJsonObjReq() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String points = response.getString("availablePoints");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

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
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }



    @Override
    public void onClick(View v) {

        TextView tv = (TextView)v;

        if(((TextView) v).getText().toString().contentEquals("Confirm")) {

            if (currentId == -2) {

                int valid = 1;

                for(int i = 0; i < txt.length; i++) {

                    if(txt[i].getText().toString().contentEquals(_friendnameText.getText().toString())) {

                        valid = 0;
                    }
                }

                if(valid == 1) {

                    url = AppCalls.ADDFRIEND + AppCalls.usernameStorage + "&friendname=" + _friendnameText.getText().toString();
                    makeStringReq();
                    urlFriendsList = AppCalls.GETFRIENDS + AppCalls.usernameStorage;
                    makeJsonArryReq();
                }
            }

            if (currentId == -3) {

                url = AppCalls.JOINGROUP + AppCalls.usernameStorage + "&groupName=" + _groupnameText.getText().toString();
                makeStringReq();
                Log.d("TAG1","REQ FROM GROUPS BUTTON with tag: " + url);
            }

        }

        else if (currentId == -2){
            Intent intent = new Intent(getActivity(), FriendsActivity.class); //Code for opening up friends page
            intent.putExtra("FRIEND_NAME", ((TextView) v).getText().toString());
            startActivity(intent);
        }


    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        String itemName = (String) spSimpleSpinner.getItemAtPosition(pos);
        int itemId = 0;

        for (int i = 0; i < groups.length; i++) {
            if (itemName.contentEquals(groups[i].groupName)) {
                itemId = groups[i].id;
            }
        }

        for (int i = 0; i < txt.length; i++) {
            txt[i].setVisibility(View.GONE);
        }

        _groupnameText.setVisibility(View.GONE);
        _friendnameText.setVisibility(View.GONE);
        Confirm.setVisibility(View.GONE);

        if(itemId != 0) {
            currentId = itemId;
            urlGroupLeaderboard = AppCalls.GETGROUPLEADERBOARD + itemName + "&mode=0";
            makeJsonArryReq3();
        }

        if(itemName.contentEquals("Friends")) {
            currentId = -2;
            Confirm.setVisibility(View.VISIBLE);
            Confirm.setOnClickListener(ControlsFragment.this);
            _friendnameText.setVisibility(View.VISIBLE);
            urlFriendsList = AppCalls.GETFRIENDS + AppCalls.usernameStorage;
            makeJsonArryReq();
        }

        if(itemName.contentEquals("Groups")) {

            currentId = -3;
            Confirm.setVisibility(View.VISIBLE);
            Confirm.setOnClickListener(ControlsFragment.this);
            _groupnameText.setVisibility(View.VISIBLE);
            urlGroupsList = AppCalls.GETALLGROUPS;
            makeJsonArryReq4();
        }




    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}
