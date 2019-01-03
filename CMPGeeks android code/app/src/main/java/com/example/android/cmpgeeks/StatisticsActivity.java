package com.example.android.cmpgeeks;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {
    private TextView nStudent;
    private TextView nCourses;
    private TextView nProjects;
    private TextView nDoctors;
    private TextView nTA;
    private TextView nAssignment;
    private TextView nTeams;
    private TextView nTeachers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        getSupportActionBar().setTitle("Statistics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialization
        nStudent = (TextView)findViewById(R.id.number_student);
        nCourses = (TextView)findViewById(R.id.number_courses);
        nProjects =(TextView)findViewById(R.id.number_project);
        nDoctors =(TextView)findViewById(R.id.number_doctors);
        nTA = (TextView)findViewById(R.id.number_ta);
        nAssignment = (TextView)findViewById(R.id.number_assignment);
        nTeams =(TextView)findViewById(R.id.number_teams);
        nTeachers =(TextView)findViewById(R.id.number_teachers_in_course);

        GetInfo();

    }

    public void GetInfo(){
        final String YearNum = Integer.toString(Constants.YearId);
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.GET_INFO_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String CountStudents = "Number of Students : "+jsonobject.getJSONObject("CountStudents").getString("CountStudents");
                            String CountCourses = "Number of Courses : "+jsonobject.getJSONObject("CountCourses").getString("CountCourses");
                            String CountProjects = "Number of Projects : "+jsonobject.getJSONObject("CountProjects").getString("CountProjects");
                            String CountDoctors = "Number of Doctors : "+jsonobject.getJSONObject("CountDoctors").getString("CountDoctors");
                            String CountAssistants = "Number of TAs : "+jsonobject.getJSONObject("CountAssistants").getString("CountAssistants");


                            nStudent.setText(CountStudents);
                            nCourses.setText(CountCourses);
                            nProjects.setText(CountProjects);
                            nDoctors.setText(CountDoctors);
                            nTA.setText(CountAssistants);

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
                params.put("YearNum", YearNum);

                return params;
            }
        };


        final StringRequest stringrequest2 = new StringRequest(Request.Method.POST,
                (Constants.GET_INFO_GROUP_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String Sassignment ="\nNumber of assignments :- \n\n";
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray assignments = jsonobject.getJSONArray("Assignments");
                            for (int i = 0 ; i < assignments.length() ; i++){
                                Sassignment = Sassignment +"Course Name : " +assignments.getJSONObject(i).getString("CourseName") + "\nNumber of assignments : "+assignments.getJSONObject(i).getString("NumOfAss")+"\n\n";
                            }
                            String Steam ="\nNumber of teams :- \n\n";
                            JSONArray teams = jsonobject.getJSONArray("Teams");
                            for (int i = 0 ; i < teams.length() ; i++){
                                Steam = Steam +"Project Name : " +teams.getJSONObject(i).getString("ProjectName") + "\nNumber of teams : "+teams.getJSONObject(i).getString("NumOfTeams")+"\n\n";
                            }
                            String Steachers ="\nNumber of teachers :- \n\n";
                            JSONArray teachers = jsonobject.getJSONArray("Teachers");
                            for (int i = 0 ; i < teachers.length() ; i++){
                                Steachers = Steachers +"Course Name : " +teachers.getJSONObject(i).getString("CourseName") + "\nNumber of teachers : "+teachers.getJSONObject(i).getString("NumOfTeachers")+"\n\n";
                            }
                            nTeams.setText(Steam);
                            nAssignment.setText(Sassignment);
                            nTeachers.setText(Steachers);
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
                params.put("YearNum", YearNum);

                return params;
            }
        };

        final RequestQueue r = Volley.newRequestQueue(getBaseContext());
        r.add(stringrequest);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                r.add(stringrequest2);
            }
        }, 500);


    }
    @Override
    public void onBackPressed() {
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
