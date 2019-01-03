package com.example.android.cmpgeeks.DataBaseEntity;

public class Year {
    private int mID;    //primary key
    private int mGR_Year;
    private int mNumberOfStudent;
    private Student mRepresentative; // ID****************

    public Year(int mID, int mGR_Year, int mNumberOfStudent) {
        this.mID = mID;
        this.mGR_Year = mGR_Year;
        this.mNumberOfStudent = mNumberOfStudent;
        mRepresentative = null;
    }

    public Year(int mID, int mGR_Year, int mNumberOfStudent, Student mRepresentative) {
        this.mID = mID;
        this.mGR_Year = mGR_Year;
        this.mNumberOfStudent = mNumberOfStudent;
        this.mRepresentative = mRepresentative;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public int getmGR_Year() {
        return mGR_Year;
    }

    public void setmGR_Year(int mGR_Year) {
        this.mGR_Year = mGR_Year;
    }

    public int getmNumberOfStudent() {
        return mNumberOfStudent;
    }

    public void setmNumberOfStudent(int mNumberOfStudent) {
        this.mNumberOfStudent = mNumberOfStudent;
    }

    public Student getmRepresentative() {
        return mRepresentative;
    }

    public void setmRepresentative(Student mRepresentative) {
        this.mRepresentative = mRepresentative;
    }
}
