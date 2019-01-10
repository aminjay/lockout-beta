package edu.iastate.cs.proj_309_vc_1.lockout2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Bind(R.id.btn_lockout) AppCompatButton _lockoutButton;
    //@Bind(R.id.homeSplash) TextView text;

    SharedPreferences sharedPref;
    private String TAG = HomeFragment.class.getSimpleName();
    private String tag_string = "string_req", tag_json_arry = "jarray_req", tag_json_obj = "jobj_req";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myInflatedView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, myInflatedView);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        _lockoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LockoutActivity.class);
                startActivity(intent);
            }
        });
        return myInflatedView;
    }





}
