package com.example.android.cmpgeeks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpDoctorsActivity extends AppCompatActivity {

    private EditText mFName;
    private EditText mLName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mGY;
    private Spinner mTypeSpinner;
    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_doctors);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up members.
        mFName = (EditText) findViewById(R.id.first_name);
        mLName  = (EditText) findViewById(R.id.last_name_edittext);
        mEmail= (EditText) findViewById(R.id.email_sign_up);
        mPassword = (EditText) findViewById(R.id.password_sign_up);
        mGY = (EditText) findViewById(R.id.gy_sign_up);
        mButton = (Button)findViewById(R.id.email_sign_in_button);
        mTypeSpinner = (Spinner)findViewById(R.id.year_sign_up);

        //Listeners
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpTeacher();
            }
        });

    }

    @Override
    public void onBackPressed() {
        setResult(Constants.SIGN_UP_FAILD);
        finish();
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

    private void SignUpTeacher() {
        final String Email = mEmail.getText().toString();
        final String Lname = mLName.getText().toString();
        final String gy = mGY.getText().toString();
        final int typeIndex = mTypeSpinner.getSelectedItemPosition();
        final String Fname = mFName.getText().toString();
        final String Password = mPassword.getText().toString();
        final String type;
        if(typeIndex==0){
            type="D";
        }else{
            type="T";
        }
        Log.i("signUp Activity ", "Email : " + Email);
        Log.i("signUp Activity", "Password : " + Password);

        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                ( Constants.TEACHER_SIGN_UP_REQUEST),
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
                params.put(Constants.Fname, Fname);
                params.put(Constants.Email, Email);
                params.put(Constants.Password,Password);
                params.put(Constants.Lname, Lname);
                params.put(Constants.GraduationYear,gy);
                params.put(Constants.TeacherType, type);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);

    }
}
