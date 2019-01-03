package com.example.android.cmpgeeks.CreateUpdateActivity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.cmpgeeks.Constants;
import com.example.android.cmpgeeks.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateUpdatePost extends AppCompatActivity {

    private EditText mEditText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_updare_post);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Post");


        mEditText = (EditText) findViewById(R.id.create_post_edittext);
        mButton = (Button) findViewById(R.id.create_post_button);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mEditText.getTextSize() == 0) {
                    mButton.setEnabled(false);
                } else {
                    mButton.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (mEditText.getTextSize() == 0) {
                    mButton.setEnabled(false);
                } else {
                    mButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (mEditText.getTextSize() == 0) {
                    mButton.setEnabled(false);
                } else {
                    mButton.setEnabled(true);
                }

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constants.selectedPost != null) {
                    if (mEditText.getText().toString().length() > 2) {
                        UpdatePost();
                        setResult(Constants.UPDAE_POST_SUCCESSSFULLY);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 500);
                    } else {
                        Toast.makeText(getBaseContext(), "Invalid Text", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (mEditText.getText().toString().length() > 2) {
                        InsertPost();
                        setResult(Constants.CREATE_POST_RESULT_OK);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 500);
                    } else {
                        Toast.makeText(getBaseContext(), "Invalid Text", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

        if (Constants.selectedPost != null) {
            getSupportActionBar().setTitle("Update Post");
            mButton.setText("Update");
            mEditText.setText(Constants.selectedPost.getmText());
        }
    }

    private void InsertPost() {

        final String Email = Constants.user.getmEMail();
        final String Text = mEditText.getText().toString();
        final String Color = "R";
        final String Date = "2018-12-12 09:30:00";
        final String YearNum = Integer.toString(Constants.YearId);
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.CERATE_POST_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonobject.getString("message"), Toast.LENGTH_LONG).show();

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
                params.put(Constants.Email, Email);
                params.put(Constants.PostText, Text);
                params.put(Constants.PostColor, Color);
                params.put(Constants.PostDate, Date);
                params.put(Constants.YearNumber, YearNum);
                return params;
            }
        };

        try {
            //set time in mili
            Thread.sleep(100);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);

    }

    private void UpdatePost() {
        final String Id = Integer.toString(Constants.selectedPost.getmID());
        final String Text = mEditText.getText().toString();

        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.UPDATE_POST_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonobject.getString("message"), Toast.LENGTH_LONG).show();

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
                params.put(Constants.PostId, Id);
                params.put(Constants.PostText, Text);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);

    }

    @Override
    public void onBackPressed() {
        if (Constants.selectedPost != null) {
            Constants.selectedPost = null;
            setResult(Constants.UPDATE_POST_FAILED);
        } else {
            setResult(Constants.CREATE_POST_RESULT_FAILD);
        }
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }


}
