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

public class Course {
    private Context context;
    private String mID;     //primary key
    private String mName;
    private ArrayList<Material> mMaterials;
    private ArrayList<Assignment> mAssignments;
    private ArrayList<Project> mProjects;

    public Course(Context context,String mID, String mName, ArrayList<Material> mMaterials, ArrayList<Assignment> mAssignments, ArrayList<Project> mProjects) {
        this.mID = mID;
        this.mName = mName;
        this.mMaterials = mMaterials;
        this.mAssignments = mAssignments;
        this.mProjects = mProjects;
        this.context =context;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public ArrayList<Material> getmMaterials() {
        return mMaterials;
    }

    public void setmMaterials(ArrayList<Material> mMaterials) {
        this.mMaterials = mMaterials;
    }

    public ArrayList<Assignment> getmAssignments() {
        return mAssignments;
    }

    public void setmAssignments(ArrayList<Assignment> mAssignments) {
        this.mAssignments = mAssignments;
    }

    public ArrayList<Project> getmProjects() {
        return mProjects;
    }

    public void setmProjects(ArrayList<Project> mProjects) {
        this.mProjects = mProjects;
    }

    public void Delete(){
        final String CourseId = Constants.selectedCourse.getmID();
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.DELETE_COURSE_REQUEST),
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

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(context);
        r.add(stringrequest);


    }
}
