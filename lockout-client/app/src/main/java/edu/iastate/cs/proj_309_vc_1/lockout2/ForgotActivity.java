package edu.iastate.cs.proj_309_vc_1.lockout2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForgotActivity extends AppCompatActivity {


    private static final String TAG = "ForgotActivity";
    String call;
    volatile public static String currentUser;
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_PASSWORD = 0;
    @Bind(R.id.input_username) EditText _usernameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.btn_forgot) Button _forgotButton;
    @Bind(R.id.link_login) TextView _loginLink;
    SharedPreferences sharedPref;
    private static final String tag_string = "string_req";
    int forgotCheck = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ButterKnife.bind(this);


        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        _forgotButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                recover();
            }
        });

    }

    public void recover() {
        Log.d(TAG, "RECOVER");

        if (!validate()) {
            onForgotFailed();
            return;
        }

        _forgotButton.setEnabled(false);

        // gets texts from username and password fields
        String username = _usernameText.getText().toString();
        //AppCalls.usernameStorage = username;
        //currentUser = username;
        String email = _emailText.getText().toString();
        //AppCalls.passwordStorage = password;
        call = (AppCalls.MAINLINK + "/help/recoverPassword?name=" + username + "&email=" + email);

        ServerForgotCheck();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {






                // SIGN UP AUTHENTICATION WITH JSON HERE






                //currently simulates successful sign up and login
                //this.finish();



            }
        }
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }

    public void onForgotSuccess() {
        _forgotButton.setEnabled(true);
        setResult(RESULT_OK, null);
        //finish();

        final ProgressDialog progressDialog = new ProgressDialog(ForgotActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending email...");
        progressDialog.show();
        Toast.makeText(getBaseContext(), "Email with recovered password sent successfully!", Toast.LENGTH_LONG).show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivityForResult(intent, REQUEST_PASSWORD);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        progressDialog.dismiss();
                    }
                },
                4000
        );

    }

    public void onForgotFailed() {
        Toast.makeText(getBaseContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();

        _forgotButton.setEnabled(true);
    }

    public void onForgotErrUser() {
        //Toast.makeText(getBaseContext(), "Username Unavailable", Toast.LENGTH_LONG).show();
        _usernameText.setError("Username was not found");
        _forgotButton.setEnabled(true);
    }

    public void onForgotErrEmail() {
        //Toast.makeText(getBaseContext(), "Username Unavailable", Toast.LENGTH_LONG).show();
        _emailText.setError("Email address for this user is incorrect");
        _forgotButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String email = _emailText.getText().toString();

        if (username.isEmpty()) {
            _usernameText.setError("You must enter a username to recover the password!");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("You must enter a valid email address!");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        return valid;
    }

    private void ServerForgotCheck() {
        Request StringReq = new StringRequest(Request.Method.GET,
                call,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);

                        if (response.equals("Done")){
                            forgotCheck = 1;
                        }

                        else if (response.equals("Invalid user")){
                            forgotCheck = 2;
                        }

                        else if (response.equals("Invalid email address")){
                            forgotCheck = 3;
                        }

                        else {
                            forgotCheck = 4;
                        }

                        ServerResponseLogic();

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

    public void ServerResponseLogic(){

        final ProgressDialog progressDialog = new ProgressDialog(ForgotActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Validating...");
        progressDialog.show();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        if (forgotCheck == 1){
                            //AppCalls.usernameStorage = currentUser;
                            onForgotSuccess();
                        }

                        else if (forgotCheck == 2) {
                            onForgotErrUser();
                        }

                        else if (forgotCheck == 3) {
                            onForgotErrEmail();
                        }

                        else if (forgotCheck == 4){
                            //AppCalls.usernameStorage = currentUser;
                            onForgotFailed();
                            Log.d("BOB","Failed forgot logic");
                        }
                        progressDialog.dismiss();
                    }
                }, 2000);
    }
}
