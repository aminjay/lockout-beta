package edu.iastate.cs.proj_309_vc_1.lockout2;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Calendar;
import java.util.Date;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;




/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment implements OnClickListener, AdapterView.OnItemSelectedListener {

    public class Group {
        int id;
        String groupName;
    }

    public StatsFragment() {
        // Required empty public constructor
    }

    @Bind(R.id.PointTotal)
    TextView TotalPointsText;

    @Bind(R.id.graph)
    PieChart graph;

    private String TAG = StatsFragment.class.getSimpleName();
    String url = "http://proj-309-vc-1.cs.iastate.edu:8080/users/updatePoints?userName=" + AppCalls.usernameStorage + "&points=0";
    Group[] groups = new Group[10];
    private String tag_string = "string_req", tag_json_arry = "jarray_req", tag_json_obj = "jobj_req";
    TextView[] txt = new TextView[10];
    private ScrollView sv;
    private LinearLayout ll;
    private View myInflatedView;
    private ArrayList<String> namesList = new ArrayList<String>();
    private ArrayAdapter adapter;
    private Spinner spSimpleSpinner;
    private int currentId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        myInflatedView = inflater.inflate(R.layout.fragment_stats, container, false);
        sv = myInflatedView.findViewById(R.id.layout);
        ll = sv.findViewById(R.id.layout1);
        ButterKnife.bind(this, myInflatedView);

        delegate(AppCalls.usernameStorage);

        currentId = 0;
        for(int i = 0; i < groups.length; i++) {
            groups[i] = new Group();
            groups[i].groupName = "";
        }

        spSimpleSpinner = (Spinner) myInflatedView.findViewById(R.id.spSimpleSpinner);
        if(namesList.contains("Stats: Global") == false) {
            namesList.add("Stats: Global");
            namesList.add("Stats: Friends");
            namesList.add("Stats: Personal");
        }

        adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,namesList);
        spSimpleSpinner.setAdapter(adapter);
        spSimpleSpinner.setOnItemSelectedListener(this);

        for(int i = 0; i < txt.length; i++) {
            txt[i] = new Button(getActivity());
            txt[i].setText("blank");
            txt[i].setTextColor(getResources().getColor(R.color.white));

            txt[i].setBackgroundColor(getResources().getColor(R.color.primary));
            ll.addView(txt[i]);
            txt[i].setVisibility(View.GONE);
        }
        TotalPointsText = myInflatedView.findViewById(R.id.PointTotal);

        makeStringReq(TotalPointsText);

        return myInflatedView;
    }

    private void makeStringReq(final TextView defaultText) {
        Request StringReq = new StringRequest(Request.Method.GET,
                url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        defaultText.setText(response);
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

    public void delegate(String username){
        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Log.d("Month tag", "is : " + month);
        Log.d("day tag", "is : " + day);
        String GETSTATS = "http://proj-309-vc-1.cs.iastate.edu:8080/stats/getStatforInterval?userName="+username+"&startDay=1&startMonth=1&startYear=2018&endDay="+day+"&endMonth="+month+"&endYear=2018";
        final ArrayList<Integer> aList = new ArrayList<>();
        final ArrayList<PieEntry> yEntrys = new ArrayList<>();

        final JsonArrayRequest req = new JsonArrayRequest(GETSTATS,
                new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "HELLO" + response.toString());
                        int length = response.length();
                        for (int i = 0; i <length; i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                double value = jsonObject.getDouble("daytotal");
                                long iPart = (long) value;
                                int intPart = ((int) iPart) * 60;
                                int fpart = (int) ((value - iPart) * 100);
                                int temp2 = intPart + fpart;
                                Log.d(TAG, "RESULT IS " + temp2);
                                aList.add(temp2);
                                int temp = response.length();
                            } catch (JSONException e) {
                                //text.setText("ERROR");
                                Log.d(TAG, "ON THIS");
                                e.printStackTrace();
                            }
                            //AppCalls.tempLength = response.length();
                        }
                        int size;
                        if (response.length()>7){size=7;}
                        else{size=response.length();}
                        ArrayList<Integer> colors = new ArrayList<>();



                        for (int i=0 ; i < size ; i++){
                            yEntrys.add(new PieEntry(aList.get(i),i));
                            colors.add(Color.rgb(47,126,252));
                        }
                        graph.setDescription(null);
                        graph.setRotationEnabled(true);
                        //pieChart.setUsePercentValues(true);
                        //pieChart.setHoleColor(Color.BLUE);
                        //pieChart.setCenterTextColor(Color.BLACK);
                        graph.setHoleRadius(35f);
                        graph.setTransparentCircleAlpha(50);
                        graph.setTransparentCircleRadius(50f);
                        graph.setCenterText("Weekly Stats");
                        graph.setCenterTextSize(15);
                        //pieChart.setDrawEntryLabels(true);
                        //pieChart.setEntryLabelTextSize(20);
                        //More options just check out the documentation!
                        //create the data set
                        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Daily Totals");
                        pieDataSet.setSliceSpace(5);
                        pieDataSet.setValueTextSize(15);
                        pieDataSet.setValueTextColor(Color.rgb(255,255,255));
                        //add colors to dataset

//
                        pieDataSet.setColors(colors);
                        //add legend to chart
                        Legend legend = graph.getLegend();
                        legend.setForm(Legend.LegendForm.CIRCLE);
                        legend.setTextSize(15);
                        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

                        //create pie data object
                        PieData pieData = new PieData(pieDataSet);
                        graph.setData(pieData);
                        graph.invalidate();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);
    }

    private void makeJsonArryReq() {
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        String item_name = "BLANK";
                        int pointsHold = 0;
                        for(int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject mJsonObjectProperty = response.getJSONObject(i);
                                item_name = mJsonObjectProperty.getString("userName");
                                pointsHold = mJsonObjectProperty.getInt("totalPoints");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String tempString = ("#" + (i) + ": " + item_name + " (" + pointsHold + " points)");
                            txt[i].setText(tempString);
                            //ll.addView(txt[i]);
                            txt[i].setOnClickListener(StatsFragment.this);
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

    private void makeJsonObjReq() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String points = response.getString("availablePoints"); //Can be changed back to totalPoints
                            String text = "Your points: " + points;
                            TotalPointsText.setText(text);
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
        TextView tv = (TextView) v;
        //url = Const.BUY + getView().findViewById(R.id.input_username).toString() + "&password=" + getView().findViewById(R.id.input_password).toString() + "&itemName=" + ((TextView) v).getText().toString() + "&groupID=0";
        url = AppCalls.BUY + AppCalls.usernameStorage + "&password=" + AppCalls.passwordStorage + "&itemName=" + ((TextView) v).getText().toString() + "&groupID=" + currentId;
        makeStringReq(TotalPointsText);

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        String itemName = (String) spSimpleSpinner.getItemAtPosition(pos);

        for (int i = 0; i < txt.length; i++) {
            txt[i].setVisibility(View.GONE);
        }

        graph.setVisibility(View.GONE);

        if(itemName.contentEquals("Stats: Personal")) {
            graph.setVisibility(View.VISIBLE);

        }

        if(itemName.contentEquals("Stats: Friends")) {
            url = "http://proj-309-vc-1.cs.iastate.edu:8080/leaderboard/getLeaderboardFilteredFriends?userName=" + AppCalls.usernameStorage + "&mode=0";
            makeJsonArryReq();
        }

        if(itemName.contentEquals("Stats: Global")) {
            url = "http://proj-309-vc-1.cs.iastate.edu:8080/leaderboard/getFullLeaderboard?mode=0";
            makeJsonArryReq();
        }



    }
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
