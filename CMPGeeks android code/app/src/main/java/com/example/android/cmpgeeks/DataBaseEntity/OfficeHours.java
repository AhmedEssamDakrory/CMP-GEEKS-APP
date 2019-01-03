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

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class OfficeHours {

    private Context context;
    private Teacher mTeacher;
    private DAYS_NAMES mDayName;
    private String mFrom;
    private String mTO;

    public OfficeHours(Context context, Teacher mTeacher, DAYS_NAMES mDayName, String mFrom, String mTO) {
        this.context = context;
        this.mTeacher = mTeacher;
        this.mDayName = mDayName;
        this.mFrom = mFrom;
        this.mTO = mTO;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Teacher getmTeacher() {
        return mTeacher;
    }

    public void setmTeacher(Teacher mTeacher) {
        this.mTeacher = mTeacher;
    }

    public DAYS_NAMES getmDayName() {
        return mDayName;
    }

    public void setmDayName(DAYS_NAMES mDayName) {
        this.mDayName = mDayName;
    }

    public String getmFrom() {
        return mFrom;
    }

    public void setmFrom(String mFrom) {
        this.mFrom = mFrom;
    }

    public String getmTO() {
        return mTO;
    }

    public void setmTO(String mTO) {
        this.mTO = mTO;
    }

    public enum DAYS_NAMES{
        Sunday,
        Monday,
        Tuesday,
        Wednesday,
        Thursday
    }
    public void Delete(){
        final String Gyear = Integer.toString(this.mTeacher.getmGY());
        final String Day = GetDayString(this.mDayName);
        final String Start = this.mFrom;
        final String Until = this.mTO;
        final String Fname = this.mTeacher.getmFName();
        final String Lname = this.mTeacher.getmLName();

        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.DELETE_OFFICE_HOUR_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            //Toast.makeText(context, jsonobject.getString("message"), Toast.LENGTH_LONG).show();

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
                params.put("Day", Day);
                params.put("Start", Start);
                params.put("Until", Until);
                params.put("Fname", Fname);
                params.put("Lname", Lname);
                params.put("Gyear", Gyear);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(context);
        r.add(stringrequest);


    }
    public String GetDayString(DAYS_NAMES day){
        if(day == DAYS_NAMES.Sunday){
            return "Sunday";
        }else if(day == DAYS_NAMES.Monday){
            return "Monday";
        }else if(day == DAYS_NAMES.Thursday){
            return "Thursday";
        }else if(day == DAYS_NAMES.Tuesday){
            return "Tuesday";
        }else{
            return "Wednesday";
        }
    }
}
