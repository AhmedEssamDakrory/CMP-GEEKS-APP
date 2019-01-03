package com.example.android.cmpgeeks.CreateUpdateActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.android.cmpgeeks.Dialogs.DatePickerFragmentDialog;
import com.example.android.cmpgeeks.Dialogs.MultipleSelectionDialog;
import com.example.android.cmpgeeks.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CreateUpdateCourse extends AppCompatActivity implements MultipleSelectionDialog.DialogListener {

    Button selectDoctor;
    TextView mDoctorsSelected;
    Button selectTA;
    TextView mTAsSelected;
    TextView mWorningTextView;
    TextView mCourseNmae;
    String[] listItemsD;
    ArrayList<Integer> mSelectedDoctoesIndexs;
    String[] listItemsT;
    boolean selectDoctorFlag;
    ArrayList<Integer> mSelectedTAIndexs;
    Button create;
    ArrayList<Teacher> AvilableDoctors;
    ArrayList<Teacher> AvilableTAs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_updare_course);
        getSupportActionBar().setTitle("Create Course");
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialization
        selectTA = (Button) findViewById(R.id.select_ta_button);
        mTAsSelected = (TextView) findViewById(R.id.selected_ta);
        selectDoctor = (Button) findViewById(R.id.select_doctor_button);
        mDoctorsSelected = (TextView) findViewById(R.id.selected_doctor);
        mCourseNmae = (TextView) findViewById(R.id.course_name_edittext);
        mWorningTextView = (TextView) findViewById(R.id.worning_text);
        mSelectedDoctoesIndexs = new ArrayList<>();
        mSelectedTAIndexs = new ArrayList<>();
        AvilableDoctors = new ArrayList<>();
        AvilableTAs = new ArrayList<>();
        listItemsD = GetAvailableDoctors();
        listItemsT = GetAvailableTeachers();


        create = (Button) findViewById(R.id.creat_course);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.selectedCourse != null) {
                    UpdateCourse();
                    setResult(Constants.UPDATE_COURSE_RESULT_OK);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                } else {

                    if (mCourseNmae.getText().toString().length()>3) {
                        InsertCouse();
                        setResult(Constants.CREATE_COURSE_RESULT_OK);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 500);
                    } else {
                        Toast.makeText(getBaseContext(),"Invalid Name",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });


        //Doctors List
        selectDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemsD = GetAvailableDoctors();
                selectDoctorFlag = true;
                showMultiSelectionDialog(listItemsD);
            }
        });


        //TA List
        selectTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemsT = GetAvailableTeachers();
                selectDoctorFlag = false;
                showMultiSelectionDialog(listItemsT);
            }
        });

        if (Constants.selectedCourse != null) {
            mWorningTextView.setVisibility(View.VISIBLE);
            mCourseNmae.setText(Constants.selectedCourse.getmName());
            getSupportActionBar().setTitle("Update Course");
        }
    }

    @Override
    public void onBackPressed() {
        if (Constants.selectedCourse != null) {
            Constants.selectedCourse=null;
            setResult(Constants.UPDATE_COURSE_RESULT_FAILED);
        } else {
            setResult(Constants.CREATE_COURSE_RESULT_FAILED);
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

    @Override
    public void applyText(ArrayList<Integer> selectedIndexs) {
        if (selectDoctorFlag) {
            String string = "";
            for (int i = 0; i < selectedIndexs.size(); i++) {
                string = string + listItemsD[selectedIndexs.get(i)];
                if (i != selectedIndexs.size() - 1) {
                    string = string + ", ";
                }
            }
            mDoctorsSelected.setText(string);
            mSelectedDoctoesIndexs = selectedIndexs;
        } else {
            String string = "";
            for (int i = 0; i < selectedIndexs.size(); i++) {
                string = string + listItemsT[selectedIndexs.get(i)];
                if (i != selectedIndexs.size() - 1) {
                    string = string + ", ";
                }
            }
            mTAsSelected.setText(string);
            mSelectedTAIndexs = selectedIndexs;
        }

    }

    public void showMultiSelectionDialog(String[] items) {
        DialogFragment newFragment = new MultipleSelectionDialog(items);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void InsertCouse() {
        //insert logic here
        final String CourseName = mCourseNmae.getText().toString();
        final String YearNumber = Integer.toString(Constants.YearId);
        final String CourseId = CourseName + YearNumber;

        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.CREATE_COURSE_REQUEST),
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
                params.put(Constants.CourseId, CourseId);
                params.put(Constants.CourseName, CourseName);
                params.put(Constants.YearNum, YearNumber);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);

        for (int i = 0; i < mSelectedDoctoesIndexs.size(); i++) {
            try {
                //set time in mili
                Thread.sleep(50);
                AssignTeacherToCourse(AvilableDoctors.get(mSelectedDoctoesIndexs.get(i)).getmFName(),
                        AvilableDoctors.get(mSelectedDoctoesIndexs.get(i)).getmLName(),
                        Integer.toString(AvilableDoctors.get(mSelectedDoctoesIndexs.get(i)).getmGY()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < mSelectedTAIndexs.size(); i++) {
            try {
                //set time in mili
                Thread.sleep(50);
                AssignTeacherToCourse(AvilableTAs.get(mSelectedTAIndexs.get(i)).getmFName(),
                        AvilableTAs.get(mSelectedTAIndexs.get(i)).getmLName(),
                        Integer.toString(AvilableTAs.get(mSelectedTAIndexs.get(i)).getmGY()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void AssignTeacherToCourse(final String mFname, String mLname, String mgy) {
        //insert logic here
        final String CourseName = mCourseNmae.getText().toString();
        final String YearNumber = Integer.toString(Constants.YearId);
        final String CourseId = CourseName + YearNumber;
        final String Fname = mFname;
        final String Lname = mLname;
        final String gy = mgy;

        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.ASSIGN_TEACHER_TO_COURSE_REQUEST),
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
                params.put(Constants.CourseId, CourseId);
                params.put(Constants.Fname, Fname);
                params.put(Constants.Lname, Lname);
                params.put(Constants.GraduationYear, gy);


                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);


    }

    private void UpdateCourse() {
        //update logic here
        Constants.selectedCourse.Delete();
        Constants.selectedCourse=null;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InsertCouse();
            }
        }, 500);
    }

    private String[] GetAvailableDoctors() {
        //put logic here
        try {
            //set time in mili
            Thread.sleep(50);

        } catch (Exception e) {
            e.printStackTrace();
        }
        StringRequest stringrequest = new StringRequest(Request.Method.GET,
                (Constants.GET_DOCRTORS_INFO_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray contancts = jsonobject.getJSONArray("info");
                            AvilableDoctors.clear();
                            for (int i = contancts.length() - 1; i >= 0; i--) {
                                String Fnmae = contancts.getJSONObject(i).getString(Constants.Fname);
                                String Lname = contancts.getJSONObject(i).getString(Constants.Lname);
                                String Email = contancts.getJSONObject(i).getString(Constants.Email);
                                String gy = contancts.getJSONObject(i).getString("Gyear");
                                AvilableDoctors.add(new Teacher(Fnmae, Lname, Integer.parseInt(gy), Teacher.TeacherTypes.doctor, Email));
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
                return params;
            }
        };
        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);
        ///////////////
        try {
            //set time in mili
            Thread.sleep(50);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] result = new String[AvilableDoctors.size()];
        for (int i = 0; i < AvilableDoctors.size(); i++) {
            result[i] = AvilableDoctors.get(i).getmFName() + " " + AvilableDoctors.get(i).getmLName();
        }
        return result;
    }

    private String[] GetAvailableTeachers() {
        //put logic here
        try {
            //set time in mili
            Thread.sleep(50);

        } catch (Exception e) {
            e.printStackTrace();
        }
        StringRequest stringrequest = new StringRequest(Request.Method.GET,
                (Constants.GET_TA_INFO_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray contancts = jsonobject.getJSONArray("info");
                            AvilableTAs.clear();
                            for (int i = contancts.length() - 1; i >= 0; i--) {
                                String Fnmae = contancts.getJSONObject(i).getString("Fname");
                                String Lname = contancts.getJSONObject(i).getString("Lname");
                                String Email = contancts.getJSONObject(i).getString("Email");
                                String gy = contancts.getJSONObject(i).getString("Gyear");
                                AvilableTAs.add(new Teacher(Fnmae, Lname, Integer.parseInt(gy), Teacher.TeacherTypes.teacherAssistant, Email));
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
        };
        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);
        ///////////////

        try {
            //set time in mili
            Thread.sleep(50);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] result = new String[AvilableTAs.size()];
        for (int i = 0; i < AvilableTAs.size(); i++) {
            result[i] = AvilableTAs.get(i).getmFName() + " " + AvilableTAs.get(i).getmLName();
        }
        return result;
    }
}