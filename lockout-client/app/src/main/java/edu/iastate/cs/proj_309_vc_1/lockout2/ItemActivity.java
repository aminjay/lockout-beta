package edu.iastate.cs.proj_309_vc_1.lockout2;

import android.content.ClipData;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.iastate.cs.proj_309_vc_1.lockout2.AppController;

/**
 * Created by markj on 4/23/2018.
 */

public class ItemActivity  extends AppCompatActivity {

    TextView Itemname;
    String url = "http://proj-309-vc-1.cs.iastate.edu:8080/store/findByItemNameAndGroupID?itemName=";
    private String TAG = ItemActivity.class.getSimpleName();
    private String tag_string = "string_req", tag_json_arry = "jarray_req", tag_json_obj = "jobj_req";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        String friend_name = getIntent().getStringExtra("ITEM_NAME");
        int itemId = getIntent().getIntExtra("ITEM_ID", 0);
        Itemname = findViewById(R.id.itemname);
        Itemname.setText(friend_name);
    }

    private void makeJsonObjReq() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
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






}
