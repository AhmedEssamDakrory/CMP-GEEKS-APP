package com.example.android.cmpgeeks.DataBaseEntity;

import java.util.ArrayList;

public class Teacher {
    private String mFName;  //primary key
    private String mLName;  //primary key
    private int mGY;        //primary key
    private TeacherTypes mType;      // 0 for doctors & 1 for TA
    private String mEMail;
    private ArrayList<Course> mTeachIn;
    private ArrayList<OfficeHours> mOfficeHours;

    public Teacher(String mFName, String mLName, int mGY, TeacherTypes mType, String mEMail) {
        this.mFName = mFName;
        this.mLName = mLName;
        this.mGY = mGY;
        this.mType = mType;
        this.mEMail = mEMail;
        mTeachIn=null;
        mOfficeHours=null;
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

    public TeacherTypes getmType() {
        return mType;
    }

    public void setmType(TeacherTypes mType) {
        this.mType = mType;
    }

    public String getmEMail() {
        return mEMail;
    }

    public void setmEMail(String mEMail) {
        this.mEMail = mEMail;
    }

    public ArrayList<Course> getmTeachIn() {
        return mTeachIn;
    }

    public void setmTeachIn(ArrayList<Course> mTeachIn) {
        this.mTeachIn = mTeachIn;
    }

    public ArrayList<OfficeHours> getmOfficeHours() {
        return mOfficeHours;
    }

    public void setmOfficeHours(ArrayList<OfficeHours> mOfficeHours) {
        this.mOfficeHours = mOfficeHours;
    }
    public String getmName() {
        return (mFName+" "+mLName);
    }

    //Inner enum
    public enum TeacherTypes{
        //add types
        doctor,
        teacherAssistant
    }
}


