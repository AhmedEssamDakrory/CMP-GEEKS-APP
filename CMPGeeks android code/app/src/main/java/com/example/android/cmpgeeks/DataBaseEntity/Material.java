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

import kotlin.jvm.internal.PropertyReference0Impl;

public class Material {
    private String Topic;
    private String mLink;
    private MATERIAL_TYPE mMaterialType;
    private Context context;

    public Material(Context context, String topic, String mLink, MATERIAL_TYPE mMaterialType) {
        Topic = topic;
        this.mLink = mLink;
        this.mMaterialType = mMaterialType;
        this.context =context;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public String getmLink() {
        return mLink;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public MATERIAL_TYPE getmMaterialType() {
        return mMaterialType;
    }

    public void setmMaterialType(MATERIAL_TYPE mMaterialType) {
        this.mMaterialType = mMaterialType;
    }

    public enum MATERIAL_TYPE{
       LECTURE_SLIDES,
       LECTURE_WRITTEN,
       SHEET,
       SHEET_SOLUTION,
       SECTION,
       NOTE,
       VIDEO,
       REFERENCE,
       OTHER
   }

    public void Delete(){
        final String CourseId = Constants.selectedCourse.getmID();
        final String Topic = this.Topic;
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.DELETE_MATERIALS_REQUEST),
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
                params.put("Topic", Topic);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(context);
        r.add(stringrequest);


    }
}
