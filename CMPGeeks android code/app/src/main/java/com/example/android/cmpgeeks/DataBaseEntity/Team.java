package com.example.android.cmpgeeks.DataBaseEntity;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.cmpgeeks.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Team {
    private Context context;
    private String mTeamNumber; //primary key
    private ArrayList<Student> mStudents;
    private String mProjectTital;

    public Team(Context context, String mTeamNumber, ArrayList<Student> mStudents, String mProjectTital) {
        this.context = context;
        this.mTeamNumber = mTeamNumber;
        this.mStudents = mStudents;
        this.mProjectTital = mProjectTital;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getmTeamNumber() {
        return mTeamNumber;
    }

    public void setmTeamNumber(String mTeamNumber) {
        this.mTeamNumber = mTeamNumber;
    }

    public ArrayList<Student> getmStudents() {
        return mStudents;
    }

    public void setmStudents(ArrayList<Student> mStudents) {
        this.mStudents = mStudents;
    }

    public String getmProjectTital() {
        return mProjectTital;
    }

    public void setmProjectTital(String mProjectTital) {
        this.mProjectTital = mProjectTital;
    }

    public void Delete(){
        final String CourseId = Constants.selectedCourse.getmID();
        final String Title = this.mProjectTital;
        final String TeamNumber = this.mTeamNumber;
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.DELETE_TEAM_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            Toast.makeText(context, jsonobject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
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

        RequestQueue r = Volley.newRequestQueue(context);
        r.add(stringrequest);


    }
}
