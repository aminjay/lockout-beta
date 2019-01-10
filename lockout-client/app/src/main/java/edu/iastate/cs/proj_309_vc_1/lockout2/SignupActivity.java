package edu.iastate.cs.proj_309_vc_1.lockout2;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


//copied in START

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

//copied in END





import butterknife.ButterKnife;
import butterknife.Bind;
public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private static final String tag_json_obj = "jobj_req";
    private static final String tag_string = "string_req", tag_json_arry = "jarray_req";
    volatile public static String currentUser;
    String addCall;
    String checkerCall;
    char signupCheck = 'f';
    @Bind(R.id.input_username) EditText _usernameText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_confirmPassword) EditText _confirmPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }

    public void signup() {
        Log.d(TAG, "Signup");
        if (!validate()) {
            onSignupFailed();
            return;
        }
        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String username = _usernameText.getText().toString();
        //AppCalls.usernameStorage = username;
        //currentUser = username;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        //AppCalls.passwordStorage = password;
        String confirmPassword = _confirmPasswordText.getText().toString();

        addCall = ("http://proj-309-vc-1.cs.iastate.edu:8080/users/addWEmail?name=" + username + "&password=" + password + "&email=" + email);

        serverSignupCheck();

        // temporarily implementing a forced condition that sign up check is true, until I can get the "saved" checker working
        //signupCheck = false;

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (signupCheck == 'u') {
                            onSignupTaken();
                        }

                        else if (signupCheck == 'f') {
                            onSignupFailed();
                        }

                        else if (signupCheck == 't'){
                            //AppCalls.usernameStorage = currentUser;
                            onSignupSuccess();
                        }
                        progressDialog.dismiss();
                    }
                },
                2000
        );
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        //finish();

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Preparing account...");
        progressDialog.show();
        Toast.makeText(getBaseContext(), "Signup successful! Log in and get started!", Toast.LENGTH_LONG).show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivityForResult(intent, 0);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        progressDialog.dismiss();
                    }
                },
                4000
        );

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public void onSignupTaken() {
        //Toast.makeText(getBaseContext(), "Username Unavailable", Toast.LENGTH_LONG).show();
        _usernameText.setError("Username unavailable");
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();
        String email = _emailText.getText().toString();
        String confirmPassword = _confirmPasswordText.getText().toString();

        checkerCall = ("http://proj-309-vc-1.cs.iastate.edu:8080/users/findByUserName?userName=" + username);

        // hard validate for now
        //userNameAvailability();
        // usernameAvailabile=true;


        if (username.isEmpty()) {
            _usernameText.setError("Username cannot be left blank");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 15) {
            _passwordText.setError("Password must be between 3 and 15 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Email address is not valid");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (confirmPassword.isEmpty() || confirmPassword.length() < 3 || confirmPassword.length() > 15 || !(confirmPassword.equals(password))) {
            _confirmPasswordText.setError("Passwords do not match");
            valid = false;
        } else {
            _confirmPasswordText.setError(null);
        }

        return valid;
    }

    private void serverSignupCheck() {
        Request StringReq = new StringRequest(Request.Method.GET,
                addCall,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);

                        if (response.equals("Saved")){
                            signupCheck = 't';
                        }

                        else if (response.equals("Failed, username is already taken")){
                            signupCheck = 'u';
                        }

                        else {
                            signupCheck = 'f';
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
}
