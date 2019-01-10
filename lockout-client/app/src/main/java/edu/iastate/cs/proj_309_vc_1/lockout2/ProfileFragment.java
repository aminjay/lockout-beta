package edu.iastate.cs.proj_309_vc_1.lockout2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.iastate.cs.proj_309_vc_1.lockout2.AppCalls;
import edu.iastate.cs.proj_309_vc_1.lockout2.AppController;

import android.view.View.OnClickListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements OnClickListener {


    public ProfileFragment() {
        // Required empty public constructor
    }

    private ScrollView sv;
    private LinearLayout ll;

    @Bind(R.id.profileSplash)
    TextView Username;
    TextView AvailablePoints;
    TextView TotalPoints;

    private String TAG = StoreFragment.class.getSimpleName();
    private String tag_string = "string_req", tag_json_arry = "jarray_req", tag_json_obj = "jobj_req";
    String url = AppCalls.POINTSOBJ;
    String urlStore = AppCalls.GETITEMS;
    TextView[] txt = new TextView[20];
    TextView blank;
    ImageButton button;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myInflatedView = inflater.inflate(R.layout.fragment_profile, container, false);

        sv = myInflatedView.findViewById(R.id.layout2);
        ll = sv.findViewById(R.id.layout3);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, myInflatedView);

        for(int i = 0; i < txt.length; i++) {
            txt[i] = new Button(getActivity());
            txt[i].setTextColor(getResources().getColor(R.color.white));

            txt[i].setBackgroundColor(getResources().getColor(R.color.primary));
            txt[i].setText("blank");
        }

        blank = new Button(getActivity());
        blank.setText("placeholder");



        Username = myInflatedView.findViewById(R.id.Username);
        TotalPoints = myInflatedView.findViewById(R.id.totalpts);
        Username.setText(AppCalls.usernameStorage);
        url = AppCalls.POINTSOBJ + AppCalls.usernameStorage;
        makeJsonObjReq();
        urlStore = AppCalls.GETITEMS + AppCalls.usernameStorage;
        makeJsonArryReq();


        //TextView defaultText = myInflatedView.findViewById((R.id.socialSplash));
        //String tempText = "Ready to focus, " + AppCalls.usernameStorage + "?";
        //defaultText.setText(tempText);
        return myInflatedView;
    }


    /**
     * Making json object request
     * */
    private void makeJsonObjReq() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String pointsAvailable = response.getString("availablePoints");
                            String pointsTotal = response.getString("totalPoints");
                            TotalPoints.setText("SEASON POINTS: " + pointsTotal);

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


    /**
     * Making json array request
     * */
    private void makeJsonArryReq() {
        JsonArrayRequest req = new JsonArrayRequest(urlStore,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        String item_name = "BLANK";
                        for(int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject mJsonObjectProperty = response.getJSONObject(i);
                                JSONObject bundle = mJsonObjectProperty.getJSONObject("item");
                                item_name = bundle.getString("itemName");
                                //item_name = mJsonObjectProperty.getString("itemName");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            txt[i].setText(item_name);
                            ll.addView(txt[i]);
                            txt[i].setOnClickListener(ProfileFragment.this);

                        }
                        ll.addView(blank);
                        blank.setVisibility(View.INVISIBLE);

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

    private void makeStringReq(final TextView defaultText) {
        Request StringReq = new StringRequest(Request.Method.GET,
                url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        //defaultText.setText(response);


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



    public void onClick(View v) {
        TextView tv = (TextView) v;
        String name = ((TextView)v).getText().toString();


        if(name.equals("profile_tier_1")) {

            button.setImageResource(R.drawable.option1);
            makeStringReq(TotalPoints);
        }

        if(name.equals("profile_tier_2")) {

            button.setImageResource(R.drawable.option2);

        }

        if(name.equals("profile_tier_3")) {

            button.setImageResource(R.drawable.option3);

        }
    }



}
