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

public class Post {

    private Context context;
    private int mID; //primary key
    private String mFName;  //foreign key
    private String mLName;  //foreign key
    private int mGY;        //foreign key
    private Student mRepresentative; //foreign key
    private String mText ;
    private int mColor;
    private String mDate;

    public Post(Context context, int mID, String mFName, String mLName, int mGY, Student mRepresentative, String mText, int mColor, String mDate) {
        this.context = context;
        this.mID = mID;
        this.mFName = mFName;
        this.mLName = mLName;
        this.mGY = mGY;
        this.mRepresentative = mRepresentative;
        this.mText = mText;
        this.mColor = mColor;
        this.mDate = mDate;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmFName() {
        return mFName;
    }

    public void setmFName(String mFName) {
        this.mFName = mFName;
    }

    public String getmLName() {
        return mLName;
    }

    public void setmLName(String mLName) {
        this.mLName = mLName;
    }

    public int getmGY() {
        return mGY;
    }

    public void setmGY(int mGY) {
        this.mGY = mGY;
    }

    public Student getmRepresentative() {
        return mRepresentative;
    }

    public void setmRepresentative(Student mRepresentative) {
        this.mRepresentative = mRepresentative;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
    public void Delete(){
        final String Id = Integer.toString(mID);

        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.DELETE_POST_REQUEST),
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
                params.put(Constants.PostId, Id);
                return params;
            }
        };
        try {
            //set time in mili
            Thread.sleep(50);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestQueue r = Volley.newRequestQueue(context);
        r.add(stringrequest);


    }
}
