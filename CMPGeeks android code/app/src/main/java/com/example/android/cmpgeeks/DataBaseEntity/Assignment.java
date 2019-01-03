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

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class Assignment {

    private Context context;
    private int mNumber;    //primary key
    private String mLink;
    private String deadline;
    private String mSubmitEmail;

    public Assignment(Context context, int mNumber, String mLink, String deadline, String mSubmitEmail) {
        this.context = context;
        this.mNumber = mNumber;
        this.mLink = mLink;
        this.deadline = deadline;
        this.mSubmitEmail = mSubmitEmail;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getmNumber() {
        return mNumber;
    }

    public void setmNumber(int mNumber) {
        this.mNumber = mNumber;
    }

    public String getmLink() {
        return mLink;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getmSubmitEmail() {
        return mSubmitEmail;
    }

    public void setmSubmitEmail(String mSubmitEmail) {
        this.mSubmitEmail = mSubmitEmail;
    }

    public void Delete(){
        final String CourseId = Constants.selectedCourse.getmID();
        final String AssNum = Integer.toString(this.mNumber);
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.DELETE_ASSIGNMENT_REQUEST),
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
                params.put("AssNum", AssNum);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(context);
        r.add(stringrequest);


    }
}
