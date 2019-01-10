package edu.iastate.cs.proj_309_vc_1.lockout2;



import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;

import static android.content.Intent.getIntent;

/**
 * Created by markj on 4/23/2018.
 */

public class FriendsActivity extends AppCompatActivity {

    //TextView Friendname;
    private String TAG = FriendsActivity.class.getSimpleName();
    private String tag_string = "string_req", tag_json_arry = "jarray_req", tag_json_obj = "jobj_req";
    PieChart graph;
    TextView _userText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        String friend_name = getIntent().getStringExtra("FRIEND_NAME");
//        Friendname = findViewById(R.id.friendname);
//        Friendname.setText(friend_name);

        _userText=findViewById(R.id.Username);
        String temp = friend_name;
        _userText.setText(temp);



        graph = findViewById(R.id.graph);

        Log.d("TEMP","passing through " + friend_name);
        delegate(friend_name);
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
                            colors.add(Color.rgb(255,255,255));
                        }
                        graph.setDescription(null);
                        graph.setRotationEnabled(true);
                        //pieChart.setUsePercentValues(true);
                        graph.setHoleColor(Color.rgb(72,140,249));
                        graph.setCenterTextColor(Color.rgb(255,255,255));
                        graph.setHoleRadius(35f);
                        //graph.setTransparentCircleColor(Color.rgb(47,126,151));
                        //graph.setTransparentCircleAlpha(255);
                        //graph.setTransparentCircleRadius(50f);
                        graph.setCenterText("Weekly Stats");
                        graph.setCenterTextSize(15);
                        //pieChart.setDrawEntryLabels(true);
                        //pieChart.setEntryLabelTextSize(20);
                        //More options just check out the documentation!
                        //create the data set
                        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Daily Totals");
                        pieDataSet.setSliceSpace(5);
                        pieDataSet.setValueTextSize(15);
                        pieDataSet.setValueTextColor(Color.rgb(72,140,249));
                        //add colors to dataset

//
                        pieDataSet.setColors(colors);
                        //add legend to chart
                        Legend legend = graph.getLegend();
                        legend.setForm(Legend.LegendForm.CIRCLE);
                        legend.setTextSize(15);
                        legend.setTextColor(Color.rgb(255,255,255));
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




}
