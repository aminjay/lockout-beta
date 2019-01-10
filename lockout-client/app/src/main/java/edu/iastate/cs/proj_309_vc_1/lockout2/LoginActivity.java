package edu.iastate.cs.proj_309_vc_1.lockout2;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String tag_json_obj = "jobj_req";
    boolean loginCheck = false;
    String call;
    volatile public static String currentUser;
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_PASSWORD = 0;
    @Bind(R.id.input_username) EditText _usernameText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
    @Bind(R.id.link_forgotpass) TextView _forgotLink;
    SharedPreferences sharedPref;

    public static String PREFS_USER_TAG = "USERNAME_STORAGE";
    public static String PREFS_PASS_TAG = "PASSWORD_STORAGE";
    public static String PREFS_LOGGED_TAG = "LOGGED_STORAGE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        _forgotLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotActivity.class);
                startActivityForResult(intent, REQUEST_PASSWORD);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });



        
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

       sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("User1", "sdf");
//        editor.commit();



        _loginButton.setEnabled(false);

        // animated processing dialog during server login
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        // gets texts from username and password fields
        String username = _usernameText.getText().toString();
        //AppCalls.usernameStorage = username;
        currentUser = username;
        String password = _passwordText.getText().toString();
        //AppCalls.passwordStorage = password;
        call = (AppCalls.MAINLINK + "/users/login?userName=" + username + "&password=" + password);

        serverLoginCheck();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (loginCheck){
                            //AppCalls.usernameStorage = _usernameText.getText().toString();
                            String passingUser = _usernameText.getText().toString();
                            String passingPass = _passwordText.getText().toString();
                            loginPrefs(passingUser,passingPass);
                            onLoginSuccess();}
                        else {
                            onLoginFailed();}
                        progressDialog.dismiss();
                    }
                }, 2000);


    }

    public void loginPrefs(String user, String pass){
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(PREFS_USER_TAG, user);
        editor.putString(PREFS_PASS_TAG, pass);
        editor.putBoolean(PREFS_LOGGED_TAG, true);
        editor.commit();


        String tempUser = sharedPref.getString(PREFS_USER_TAG, "could not find user info");
        String tempPass = sharedPref.getString(PREFS_PASS_TAG, "could not find pass info");
        boolean tempLog = sharedPref.getBoolean(PREFS_LOGGED_TAG, false);

        AppCalls.initData(tempUser,tempPass);
        Log.d("PREF_LOG", tempUser + " " + tempPass + " " + tempLog);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {






                // SIGN UP AUTHENTICATION WITH JSON HERE






                //currently simulates successful sign up and login
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }

    public void onLoginSuccess() {

        setup();

        _loginButton.setEnabled(true);
        finish();
    }

    public void setup(){

    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty()) {
            _usernameText.setError("You must enter a username to log in!");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 15) {
            _passwordText.setError("Password must be between 3 and 15 characters!");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void serverLoginCheck() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                call, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "DIS ON RESPONSE" + response.toString());
                        //AppCalls.jHolder = response;
                        if (response != null){
                            loginCheck = true;
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
