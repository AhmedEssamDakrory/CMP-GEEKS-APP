package com.example.android.cmpgeeks.CreateUpdateActivity;

import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.android.cmpgeeks.Constants;
import com.example.android.cmpgeeks.DataBaseEntity.Teacher;
import com.example.android.cmpgeeks.Dialogs.TimePickerFragmentDialog;
import com.example.android.cmpgeeks.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateUpdateOfficeHour extends AppCompatActivity implements TimePickerFragmentDialog.DialogListener {

    private TextView textView;
    private Spinner mDayNmae;
    private Spinner mTeacjerSpinner;
    private TextView mFromText;
    private TextView mToText;
    private Button mButton;
    private ArrayList<Teacher> teachers;

    private String mFrom;
    private String mTo;

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crate_update_office_hour);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Office Hour");

        //Initialization
        mDayNmae = (Spinner) findViewById(R.id.select_day_spinner);
        mTeacjerSpinner = (Spinner) findViewById(R.id.select_teachrt_spinner);
        mFromText = (TextView) findViewById(R.id.from_text);
        mToText = (TextView) findViewById(R.id.to_text);
        mButton = (Button) findViewById(R.id.creat_office_hour);
        textView = (TextView)findViewById(R.id.text);
        teachers = new ArrayList<>();
        mTo = null;
        mFrom = null;

        GetAvailableTeachers();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.selectedOfficeHour != null) {
                    UpdateOfficeHour();
                    setResult(Constants.UPDATE_OFFICE_HOUR_RESULT_OK);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
finish();                        }
                    }, 500);
                } else {
                    if(mTo ==null){
                        Toast.makeText(getBaseContext(),"you must select time(TO)",Toast.LENGTH_SHORT).show();

                    }else if(mFrom ==null){
                        Toast.makeText(getBaseContext(),"you must select time(From)",Toast.LENGTH_SHORT).show();

                    }else {
                        InsertOfficeHour();
                        setResult(Constants.CREATE_OFFICE_HOUR_RESULT_OK);
                        finish();
                    }
                }


            }
        });


        if (Constants.selectedOfficeHour != null) {
            mFrom = Constants.selectedOfficeHour.getmFrom();
            mTo = Constants.selectedOfficeHour.getmTO();
            mFromText.setText(mFrom);
            mToText.setText(mTo);
            mDayNmae.setSelection(Constants.selectedOfficeHour.getmDayName().ordinal());
            mButton.setText("Update");
            textView.setText("Select Teacher\n(Again)");
            textView.setTextColor(getResources().getColor(R.color.red));
            getSupportActionBar().setTitle("Update Office hour");
        }

        //Listeners
        mFromText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                showTimePickerDialog();
            }
        });
        mToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                showTimePickerDialog();
            }
        });

    }


    private void UpdateOfficeHour() {
        Constants.selectedOfficeHour.Delete();
        InsertOfficeHour();
    }

    private void InsertOfficeHour() {
        final String Day = mDayNmae.getSelectedItem().toString();
        final String Start = this.mFrom;
        final String Until = this.mTo;
        final String Fname = teachers.get(mTeacjerSpinner.getSelectedItemPosition()).getmFName();
        final String Lname = teachers.get(mTeacjerSpinner.getSelectedItemPosition()).getmLName();
        final String Gyear = Integer.toString(teachers.get(mTeacjerSpinner.getSelectedItemPosition()).getmGY());

        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.ADD_OFFICE_HOUR_REQUEST),
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
                params.put("Day", Day);
                params.put("Start", Start);
                params.put("Until", Until);
                params.put("Fname", Fname);
                params.put("Lname", Lname);
                params.put("Gyear", Gyear);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(getBaseContext());
        r.add(stringrequest);
    }

    @Override
    public void onBackPressed() {
        if (Constants.selectedOfficeHour != null) {
            Constants.selectedOfficeHour = null;
            setResult(Constants.UPDATE_OFFICE_HOUR_RESULT_FAILED);
        } else {
            setResult(Constants.CREATE_OFFICE_HOUR_RESULT_FAILED);
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

    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragmentDialog();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void applyText(int hour, int minute) {
        String result;
        result = Integer.toString(hour) + ":" + Integer.toString(minute)+":00";
        if (flag) {
            mFromText.setText(result);
            mFrom = result;
        } else {
            mToText.setText(result);
            mTo =result;
        }
    }

    private void GetAvailableTeachers() {
        if (Constants.user_type == Constants.USER_TYPE.REPRESENTATIVE) {
            final String CourseId = Constants.selectedCourse.getmID();
            StringRequest stringrequest = new StringRequest(Request.Method.POST,
                    (Constants.GET_TEACHER_IN_COURSE_REQUEST),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonobject = new JSONObject(response);
                                JSONArray contancts = jsonobject.getJSONArray("info");
                                teachers.clear();
                                for (int i = contancts.length() - 1; i >= 0; i--) {
                                    String Fnmae = contancts.getJSONObject(i).getString(Constants.Fname);
                                    String Lname = contancts.getJSONObject(i).getString(Constants.Lname);
                                    int gy = Integer.parseInt(contancts.getJSONObject(i).getString("Gyear"));
                                    teachers.add(new Teacher(Fnmae, Lname, gy, Teacher.TeacherTypes.teacherAssistant, null));
                                }
                                FillSpinner();
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

                    return params;
                }
            };

            RequestQueue r = Volley.newRequestQueue(getBaseContext());
            r.add(stringrequest);

        } else {
            teachers.clear();
            teachers.add(new Teacher(Constants.user.getmFName(), Constants.user.getmLName(), Constants.user.getmGY(), Constants.user.getmType(), Constants.user.getmEMail()));
            FillSpinner();
        }
    }

    private void FillSpinner() {
        String[] items = new String[teachers.size()];
        for (int i = 0; i < teachers.size(); i++) {
            String tital;
            if (teachers.get(i).getmType() == Teacher.TeacherTypes.doctor) {
                tital = "Dr.";
            } else {
                tital = "Eng.";
            }
            items[i] = tital + teachers.get(i).getmFName() + " " + teachers.get(i).getmLName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTeacjerSpinner.setAdapter(adapter);
    }
}
