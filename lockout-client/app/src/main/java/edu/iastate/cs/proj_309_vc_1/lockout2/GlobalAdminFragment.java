package edu.iastate.cs.proj_309_vc_1.lockout2;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class GlobalAdminFragment extends Fragment implements OnClickListener {

    public class Group {
        int id;
        String groupName;
    }

    public GlobalAdminFragment() {
        // Required empty public constructor
    }
//    @Bind(R.id.PointTotal)
//    TextView TotalPointsText;
    //TextView Confirm;

    private String TAG = GlobalAdminFragment.class.getSimpleName();
    //private String url = "http://proj-309-vc-1.cs.iastate.edu:8080/users/updatePoints?userName=" + AppCalls.usernameStorage + "&points=" + "0";
    String tempUsernameStorage = AppCalls.usernameStorage;
    String url = "http://proj-309-vc-1.cs.iastate.edu:8080/users/updatePoints?userName=" + AppCalls.usernameStorage + "&points=0";
    String urlGroups = AppCalls.GETALLGROUPS;

    @Bind(R.id.input_command) EditText _inputCommandText;
    @Bind(R.id.Confirm) Button _confirmButton;
    @Bind(R.id.TEXT_STATUS_ID)
    TextView _statusText;

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

        myInflatedView = inflater.inflate(R.layout.fragment_globaladmin, container, false);
        sv = myInflatedView.findViewById(R.id.layout);
        ll = sv.findViewById(R.id.layout1);
        ButterKnife.bind(this, myInflatedView);

        _confirmButton.setOnClickListener(GlobalAdminFragment.this);

        _statusText.setMovementMethod(new ScrollingMovementMethod());

        return myInflatedView;
    }

    private void makeStringReq(String string) {
        Request StringReq = new StringRequest(string,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        //defaultText.setText(response);
                        //Toast.makeText(getContext(),response,Toast.LENGTH_LONG);
                        _statusText.setText(response);

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


    @Override
    public void onClick(View v) {
        url = AppCalls.MAINLINK + "/" + _inputCommandText.getText().toString();
        makeStringReq(url);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}

