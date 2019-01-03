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

public class Project {
    private String mSubmitEmail;
    private String mTitle;   //primary key
    private String mLink;
    private int mMaxStudent;
    private ArrayList<Team> mTeams; //creat team then check if he is in a team
    private Context context;
    public Project(Context context,String mSubmitEmail, String mTitle, String mLink, int mMaxStudent, ArrayList<Team> mTeams) {
        this.mSubmitEmail = mSubmitEmail;
        this.mTitle = mTitle;
        this.mLink = mLink;
        this.mMaxStudent = mMaxStudent;
        this.mTeams = mTeams;
        this.context =context;
    }

    public String getmSubmitEmail() {
        return mSubmitEmail;
    }

    public void setmSubmitEmail(String mSubmitEmail) {
        this.mSubmitEmail = mSubmitEmail;
    }

    public int getmMaxStudent() {
        return mMaxStudent;
    }

    public void setmMaxStudent(int mMaxStudent) {
        this.mMaxStudent = mMaxStudent;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmLink() {
        return mLink;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public ArrayList<Team> getmTeams() {
        return mTeams;
    }

    public void setmTeams(ArrayList<Team> mTeams) {
        this.mTeams = mTeams;
    }
    public void Delete(){
        final String CourseId = Constants.selectedCourse.getmID();
        final String Title = this.mTitle;
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.DELETE_PROJECT_REQUEST),
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

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(context);
        r.add(stringrequest);


    }
}
