package com.example.android.cmpgeeks.CreateUpdateActivity;

import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.cmpgeeks.Constants;
import com.example.android.cmpgeeks.Dialogs.DatePickerFragmentDialog;
import com.example.android.cmpgeeks.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateUpdateAssignment extends AppCompatActivity implements DatePickerFragmentDialog.DialogListener {

    private EditText mLinkEdit;
    private EditText mSubmitEmail;
    private Button mDateButton;
    private Button mButton;
    private TextView mDateText;

    private String deadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_assignment);

        getSupportActionBar().setTitle("Create Assignment");
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLinkEdit = (EditText) findViewById(R.id.material_link_edittext);
        mDateButton = (Button) findViewById(R.id.select_deadline);
        mButton = (Button) findViewById(R.id.create_material_button);
        mDateText = (TextView) findViewById(R.id.deadline_text);
        mSubmitEmail = (EditText) findViewById(R.id.submit_emali_edittext);

        deadline = null;
        //listeners
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();

            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Constants.selectedAssignment != null) {
                    UpdateAssignment();
                    setResult(Constants.UPDATE_ASSIGNMENT_RESULT_OK);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                } else {
                    if(deadline==null){
                        Toast.makeText(getBaseContext(),"you must select deadline",Toast.LENGTH_SHORT).show();
                    }else {
                        CreateAssignment();
                        setResult(Constants.CREATE_ASSIGNMENT_RESULT_OK);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 500);
                    }
                }


            }
        });

        if (Constants.selectedAssignment != null) {
            mLinkEdit.setText(Constants.selectedAssignment.getmLink());
            mSubmitEmail.setText(Constants.selectedAssignment.getmSubmitEmail());
            mButton.setText("Update");
            getSupportActionBar().setTitle("Update Assignment");
            getSupportActionBar().setSubtitle("Assignment " + Integer.toString(Constants.selectedAssignment.getmNumber()));
            String date = Constants.selectedAssignment.getDeadline();
            mDateText.setText(date);
            deadline = Constants.selectedAssignment.getDeadline();
        }

    }


    @Override
    public void onBackPressed() {
        if (Constants.selectedAssignment != null) {
            Constants.selectedAssignment=null;
            setResult(Constants.UPDATE_ASSIGNMENT_RESULT_FAILED);
        } else {
            setResult(Constants.CREATE_ASSIGNMENT_RESULT_FAILED);
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

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragmentDialog();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    @Override
    public void applyText(int year, int month, int day) {


        String date = Integer.toString(year) + '-' + Integer.toString(month) + '-' + Integer.toString(day);
        mDateText.setText(date);
        deadline= date + " 00:00:00";

    }

    private void CreateAssignment(){
        final String CourseId = Constants.selectedCourse.getmID();
        final String Link = mLinkEdit.getText().toString();
        final String Deadline = deadline;
        final String SubmitEmail = mSubmitEmail.getText().toString();
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.ADD_ASSIGNMENT_REQUEST),
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
                params.put("Link", Link);
                params.put("Deadline", Deadline);
                params.put("SubmitEmail", SubmitEmail);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(getBaseContext());
        r.add(stringrequest);

    }

    private void UpdateAssignment(){
        final String CourseId = Constants.selectedCourse.getmID();
        final String Link = mLinkEdit.getText().toString();
        final String Deadline = deadline;
        final String SubmitEmail = mSubmitEmail.getText().toString();
        final String AssNum = Integer.toString(Constants.selectedAssignment.getmNumber());
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.UPDATE_ASSIGNMENT_REQUEST),
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
                params.put("Link", Link);
                params.put("Deadline", Deadline);
                params.put("SubmitEmail", SubmitEmail);
                params.put("AssNum", AssNum);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(getBaseContext());
        r.add(stringrequest);
    }
}
