package com.example.android.cmpgeeks.CreateUpdateActivity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CreateUpdateProject extends AppCompatActivity {

    private EditText mLinkEdit;
    private EditText mSubmitEmailEdit;
    private EditText mTitalEdit;
    private EditText mMaxNumberEdit;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_project);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Project");


        //Initialization
        mLinkEdit = (EditText)findViewById(R.id.Project_link_edittext);
        mSubmitEmailEdit = (EditText)findViewById(R.id.submit_emali_edittext);
        mTitalEdit =(EditText)findViewById(R.id.project_topic_edittext);
        mMaxNumberEdit = (EditText)findViewById(R.id.max_number_edittext);
        mButton =(Button)findViewById(R.id.create_project_button);



        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constants.selectedProject != null){
                    UpdateProject();
                    setResult(Constants.UPDATE_PROJECT_RESULT_OK);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                }else {
                    if (mMaxNumberEdit.getText().toString().length()>0 && mTitalEdit.getText().toString().length()>2) {
                        InsertProject();
                        setResult(Constants.CREATE_PROJECT_RESULT_OK);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 500);
                    } else {
                        Toast.makeText(getBaseContext(),"You must fill Number and Title",Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

        if(Constants.selectedProject!=null){
            mLinkEdit.setText(Constants.selectedProject.getmLink());
            mMaxNumberEdit.setText(Integer.toString(Constants.selectedProject.getmMaxStudent()));
            mTitalEdit.setText(Constants.selectedProject.getmTitle());
            mTitalEdit.setEnabled(false);
            mSubmitEmailEdit.setText(Constants.selectedProject.getmSubmitEmail());
            mButton.setText("Update");
        }
    }

    private void UpdateProject(){
        final String CourseId = Constants.selectedCourse.getmID();
        final String Title = this.mTitalEdit.getText().toString();
        final String MaxNum = this.mMaxNumberEdit.getText().toString();
        final String SubmitEmail = this.mSubmitEmailEdit.getText().toString();
        final String Link = this.mLinkEdit.getText().toString();

        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.UPDATE_PROJECT_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            Toast.makeText(getBaseContext(), jsonobject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CourseId", CourseId);
                params.put("Title", Title);
                params.put("Link", Link);
                params.put("MaxNum", MaxNum);
                params.put("SubmitEmail", SubmitEmail);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(getBaseContext());
        r.add(stringrequest);    }

    private void InsertProject(){
        final String CourseId = Constants.selectedCourse.getmID();
        final String Title = this.mTitalEdit.getText().toString();
        final String MaxNum = this.mMaxNumberEdit.getText().toString();
        final String SubmitEmail = this.mSubmitEmailEdit.getText().toString();
        final String Link = this.mLinkEdit.getText().toString();

        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.ADD_PROJECT_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            Toast.makeText(getBaseContext(), jsonobject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CourseId", CourseId);
                params.put("Title", Title);
                params.put("Link", Link);
                params.put("MaxNum", MaxNum);
                params.put("SubmitEmail", SubmitEmail);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(getBaseContext());
        r.add(stringrequest);
    }

    @Override
    public void onBackPressed() {
        if (Constants.selectedProject != null){
            Constants.selectedProject = null;
            setResult(Constants.UPDATE_PROJECT_RESULT_FAILED);
        }else {
            setResult(Constants.CREATE_PROJECT_RESULT_FAILED);
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
