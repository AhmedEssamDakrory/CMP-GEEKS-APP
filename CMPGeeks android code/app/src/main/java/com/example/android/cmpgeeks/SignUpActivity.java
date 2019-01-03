package com.example.android.cmpgeeks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.cmpgeeks.DataBaseEntity.Year;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends AppCompatActivity  {



   //layout
    private EditText mFName;
    private EditText mLName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mSEC;
    private EditText mBN;
    private Spinner mYearSpinner;
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up members.

        mFName = (EditText) findViewById(R.id.first_name);
        mLName  = (EditText) findViewById(R.id.last_name_edittext);
        mEmail= (EditText) findViewById(R.id.email_sign_up);
        mPassword = (EditText) findViewById(R.id.password_sign_up);
        mSEC = (EditText) findViewById(R.id.sec_sign_up);
        mBN = (EditText) findViewById(R.id.bn_sign_up);
        mYearSpinner = (Spinner) findViewById(R.id.year_sign_up) ;
        mButton = (Button)findViewById(R.id.email_sign_in_button);


        //Listeners
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpStudent();
            }
        });

    }

    @Override
    public void onBackPressed() {
        setResult(Constants.SIGN_UP_FAILD);
        super.onBackPressed();
        return;
    }

    protected void onStop() {
        setResult(Constants.SIGN_UP_FAILD);
        super.onStop();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            setResult(Constants.SIGN_UP_FAILD);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void SignUpStudent() {
        final String Email = mEmail.getText().toString();
        final String Lname = mLName.getText().toString();
        final String Sec = mSEC.getText().toString();
        final String Bn = mBN.getText().toString();
        final int yearIndex = mYearSpinner.getSelectedItemPosition()+1;
        final String Fname = mFName.getText().toString();
        final String Password = mPassword.getText().toString();

        Log.i("signUp Activity ", "Email : " + Email);
        Log.i("signUp Activity", "Password : " + Password);

        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                ( Constants.STUDENT_SIGN_UP_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonobject.getString("message"), Toast.LENGTH_LONG).show();
                            if(jsonobject.getBoolean("SignedUp")){
                                setResult(Constants.SIGN_UP_SUCCESSFULLY);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Fname", Fname);
                params.put("Email", Email);
                params.put("Password",Password);
                params.put("Lname", Lname);
                params.put("Sec",Sec);
                params.put("Bn", Bn);
                params.put("YearNum", Integer.toString(yearIndex));

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);


    }


}

