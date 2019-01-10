package edu.iastate.cs.proj_309_vc_1.lockout2;


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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupAdminFragment extends Fragment implements OnClickListener {

    public class Group {
        int id;
        String groupName;
    }

    public GroupAdminFragment() {
        // Required empty public constructor
    }
//    @Bind(R.id.PointTotal)
//    TextView TotalPointsText;
    //TextView Confirm;

    private String TAG = GroupAdminFragment.class.getSimpleName();
    //private String url = "http://proj-309-vc-1.cs.iastate.edu:8080/users/updatePoints?userName=" + AppCalls.usernameStorage + "&points=" + "0";
    String tempUsernameStorage = AppCalls.usernameStorage;
    String url = "http://proj-309-vc-1.cs.iastate.edu:8080/users/updatePoints?userName=" + AppCalls.usernameStorage + "&points=0";
    String urlGroups = AppCalls.GETALLGROUPS;

    @Bind(R.id.input_itemname) EditText _itemnameText;
    @Bind(R.id.input_itemcost) EditText _itemcostText;
    @Bind(R.id.input_itemdesc) EditText _itemdescText;
    @Bind(R.id.input_consumable) EditText _consumableText;
    @Bind(R.id.input_group) EditText _groupText;
    @Bind(R.id.Confirm) Button _confirmButton;

    Group[] groups = new Group[10];

    private String tag_string = "string_req", tag_json_arry = "jarray_req", tag_json_obj = "jobj_req";
    private ScrollView sv;
    private LinearLayout ll;
    private View myInflatedView;
    //private Spinner spSimpleSpinner;
    private ArrayList<String> namesList = new ArrayList<String>();
    private ArrayAdapter adapter;
    private int currentId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        myInflatedView = inflater.inflate(R.layout.fragment_groupadmin, container, false);
        sv = myInflatedView.findViewById(R.id.layout);
        ll = sv.findViewById(R.id.layout1);
        ButterKnife.bind(this, myInflatedView);

        currentId = 0;

        for(int i = 0; i < groups.length; i++) {
            groups[i] = new Group();
            groups[i].groupName = "";
        }

//        TextView defaultText = myInflatedView.findViewById((R.id.availablePoints));
//        String userDisplay = LoginActivity.currentUser;
//        String tempText = "Available points = " + userDisplay;
//        defaultText.setText(tempText);


        //spSimpleSpinner = (Spinner) myInflatedView.findViewById(R.id.spSimpleSpinner4);


        urlGroups = AppCalls.GETGROUPS + AppCalls.usernameStorage;
        makeJsonArryReq2();
        //adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,namesList);
        //spSimpleSpinner.setAdapter(adapter);
        //spSimpleSpinner.setOnItemSelectedListener(this);




//        TotalPointsText = myInflatedView.findViewById(R.id.PointTotal);
//        button = myInflatedView.findViewById(R.id.Confirm);
//        Confirm.setVisibility(View.VISIBLE);
        _confirmButton.setOnClickListener(GroupAdminFragment.this);

        //string request will also make a jsonobjreq that gets available points










        return myInflatedView;
    }

    private void makeStringReq() {
        Request StringReq = new StringRequest(Request.Method.GET,
                url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        //defaultText.setText(response);
                        if (response.equals("Saved")){
                            Toast.makeText(getActivity(),"Item was added successfully!",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getActivity(),"Please try again another time.",Toast.LENGTH_LONG).show();
                        }

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



    // Cancelling request
    // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);

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
                            //TotalPointsText.setText(points);
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
//        TextView tv = (TextView)v;
        //url = Const.BUY + getView().findViewById(R.id.input_username).toString() + "&password=" + getView().findViewById(R.id.input_password).toString() + "&itemName=" + ((TextView) v).getText().toString() + "&groupID=0";
        url = AppCalls.ADDITEM + _itemnameText.getText().toString() + "&itemDescription=" + _itemdescText.getText().toString() + "&itemCost=" + _itemcostText.getText().toString() + "&consumable=" + _consumableText.getText().toString() + "&groupID=" + _groupText.getText().toString();
        makeStringReq();


    }

//
//    public void onItemSelected(AdapterView<?> parent, View view,
//                               int pos, long id) {
//        // An item was selected. You can retrieve the selected item using
//        String itemName = (String) spSimpleSpinner.getItemAtPosition(pos);
//        int itemId = 0;
//
//        for (int i = 0; i < groups.length; i++) {
//            if (itemName.contentEquals(groups[i].groupName)) {
//                itemId = groups[i].id;
//                currentId = itemId;
//            }
//
//        }
//
//
//
//
//    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}

