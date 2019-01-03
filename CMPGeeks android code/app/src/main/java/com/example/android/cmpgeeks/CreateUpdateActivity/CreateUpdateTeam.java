package com.example.android.cmpgeeks.CreateUpdateActivity;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
import com.example.android.cmpgeeks.Constants;
import com.example.android.cmpgeeks.CourseActivity;
import com.example.android.cmpgeeks.DataBaseEntity.Project;
import com.example.android.cmpgeeks.DataBaseEntity.Student;
import com.example.android.cmpgeeks.Dialogs.MultipleSelectionDialog;
import com.example.android.cmpgeeks.LogInActivity;
import com.example.android.cmpgeeks.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateUpdateTeam extends AppCompatActivity implements MultipleSelectionDialog.DialogListener {

    private Spinner mSpinner;
    private TextView mTeamMembers;
    private Button mButton;
    private ArrayList<Project> mProjects;
    private ArrayList<Student> mStudent;
    private EditText mTeamNumber;
    private ArrayList<Student> mSelectedStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_team);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Team");


        //Initialization
        mSpinner = (Spinner) findViewById(R.id.select_project_spinner);
        mTeamMembers = (TextView) findViewById(R.id.select_team_member_text_view);
        mButton = (Button) findViewById(R.id.create_team_button);
        mProjects = new ArrayList<>();
        mStudent = new ArrayList<>();
        mTeamNumber = (EditText) findViewById(R.id.number);
        mSelectedStudent = new ArrayList<>();
        mSelectedStudent.add(new Student(Constants.user.getId(), Constants.user.getmFName() + " " + Constants.user.getmLName(), Constants.user.getmEMail(), Constants.user.getmSEC(), Constants.user.getmBN(), Constants.YearId));
        Random rand = new Random();

        int n = rand.nextInt(50) + 1;

        mTeamNumber.setText(Integer.toString(n));

        //listener
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.selectedTeam == null) {
                    new AlertDialog.Builder(CreateUpdateTeam.this).setTitle("Create New Team")
                            .setMessage("You Are included in this team, Do you want to continue?\n\"Keep in mind teams can't be updated and only the representative can delete teams\"")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    InsertTeam();

                                    setResult(Constants.CREATE_TEAM_RESULT_OK);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 500);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onBackPressed();
                                }
                            }).show();


                } else {
                    UpdateTeam();
                    setResult(Constants.UPDATE_TEAM_RESULT_OK);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                }
            }
        });

        mTeamMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAvailableStudent();

            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyText(new ArrayList<Integer>());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GetAvailableProjectss();
    }

    @Override
    public void onBackPressed() {
        if (Constants.selectedTeam != null) {
            setResult(Constants.UPDATE_TEAM_RESULT_FAILED);
        } else {
            setResult(Constants.CREATE_TEAM_RESULT_FAILED);
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


    private void InsertTeam() {
        final String CourseId = Constants.selectedCourse.getmID();
        final String TeamNumber = mTeamNumber.getText().toString();
        final String Title = mSpinner.getSelectedItem().toString();
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.ADD_TEAM_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            Toast.makeText(getBaseContext(), jsonobject.getString("message"), Toast.LENGTH_LONG).show();

                            for (int i = 0; i < mSelectedStudent.size(); i++) {
                                try {
                                    //set time in mili
                                    Thread.sleep(200);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                AssignStudentToTeam(mSelectedStudent.get(i));
                            }
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
                params.put("TeamNumber", TeamNumber);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(getBaseContext());
        r.add(stringrequest);


    }

    private void UpdateTeam() {
        //update logic
    }


    @Override
    public void applyText(ArrayList<Integer> selectedIndexs) {
        String string = "";
        mSelectedStudent.clear();
        mSelectedStudent.add(new Student(Constants.user.getId(), Constants.user.getmFName() + " " + Constants.user.getmLName(), Constants.user.getmEMail(), Constants.user.getmSEC(), Constants.user.getmBN(), Constants.YearId));
        for (int i = 0; i < selectedIndexs.size(); i++) {
            mSelectedStudent.add(mStudent.get(selectedIndexs.get(i)));
            string = string + mStudent.get(selectedIndexs.get(i)).getmName();
            if (i != selectedIndexs.size() - 1) {
                string = string + ", ";
            }
        }
        if (selectedIndexs.size() != 0) {
            mTeamMembers.setText(string);
        } else {
            mTeamMembers.setText("Select team members");
        }

    }


    public void GetAvailableProjectss() {


        if (Constants.selectedProject == null) {
            final String CourseId = Constants.selectedCourse.getmID();
            final String StudentId = Constants.user.getId();

            StringRequest stringrequest = new StringRequest(Request.Method.POST,
                    (Constants.GET_AVAILABLE_PROJECTS_REQUEST),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonobject = new JSONObject(response);
                                JSONArray projects = jsonobject.getJSONArray("projects");
                                mProjects.clear();
                                for (int i = projects.length() - 1; i >= 0; i--) {
                                    int number = Integer.parseInt(projects.getJSONObject(i).getString("MaxNum"));
                                    String link = projects.getJSONObject(i).getString("Link");
                                    String Title = projects.getJSONObject(i).getString("Title");
                                    String submit = projects.getJSONObject(i).getString("SubmitEmail");
                                    mProjects.add(new Project(getBaseContext(), submit, Title, link, number, null));
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
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constants.CourseId, CourseId);
                    params.put("StudentId", StudentId);

                    return params;
                }
            };

            RequestQueue r = Volley.newRequestQueue(this);
            r.add(stringrequest);
        } else {
            mProjects.add(Constants.selectedProject);
            FillSpinner();
        }

    }

    public void GetAvailableStudent() {

        final String CourseId = Constants.selectedCourse.getmID();
        final String Title = mSpinner.getSelectedItem().toString();
        final String YearId = Integer.toString(Constants.YearId);
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.GET_STUDENT_NOT_IN_TEAM_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray projects = jsonobject.getJSONArray("Students");
                            mStudent.clear();
                            for (int i = projects.length() - 1; i >= 0; i--) {
                                String Fname = projects.getJSONObject(i).getString("Fname");
                                String Lname = projects.getJSONObject(i).getString("Lname");
                                String Email = projects.getJSONObject(i).getString("Email");
                                String Id = projects.getJSONObject(i).getString("Id");
                                String Bn = projects.getJSONObject(i).getString("Bn");
                                String Sec = projects.getJSONObject(i).getString("Sec");
                                if (!Constants.user.getId().equals(Id)) {
                                    mStudent.add(new Student(Id, Fname + " " + Lname, Email, Integer.parseInt(Sec), Integer.parseInt(Bn), Constants.YearId));
                                }
                            }
                            String[] list = new String[mStudent.size()];
                            for (int i = 0; i < mStudent.size(); i++) {
                                list[i] = "Name : " + mStudent.get(i).getmName() + "\nSEC : " + Integer.toString(mStudent.get(i).getmSEC()) + "\nBN : " + Integer.toString(mStudent.get(i).getmBN());
                            }
                            new MultipleSelectionDialog(list).show(getSupportFragmentManager(), "multipleSelection");

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
                params.put("Title", Title);
                params.put("YearId", YearId);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);

    }

    private void FillSpinner() {

        String[] items = new String[mProjects.size()];
        for (int i = 0; i < mProjects.size(); i++) {
            items[i] = mProjects.get(i).getmTitle();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

    private void AssignStudentToTeam(Student s) {
        final String CourseId = Constants.selectedCourse.getmID();
        final String Title = mSpinner.getSelectedItem().toString();
        final String TeamNumber = mTeamNumber.getText().toString();
        final String StudentId = s.getId();

        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.ASSIGN_STUDENT_TO_TEAM),
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CourseId", CourseId);
                params.put("Title", Title);
                params.put("TeamNumber", TeamNumber);
                params.put("StudentId", StudentId);
                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);
    }
}
