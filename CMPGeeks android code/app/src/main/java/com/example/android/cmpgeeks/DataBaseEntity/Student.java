package com.example.android.cmpgeeks.DataBaseEntity;

import java.util.ArrayList;

public class Student {
    private String Id;
    private String mName;
    private String mEMail;
    private int mSEC;
    private int mBN;
    private int mYear; //ID

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmEMail() {
        return mEMail;
    }

    public void setmEMail(String mEMail) {
        this.mEMail = mEMail;
    }

    public int getmSEC() {
        return mSEC;
    }

    public void setmSEC(int mSEC) {
        this.mSEC = mSEC;
    }

    public int getmBN() {
        return mBN;
    }

    public void setmBN(int mBN) {
        this.mBN = mBN;
    }

    public int getmYear() {
        return mYear;
    }

    public void setmYear(int mYear) {
        this.mYear = mYear;
    }

    public Student(String id, String mName, String mEMail, int mSEC, int mBN, int mYear) {
        Id = id;
        this.mName = mName;
        this.mEMail = mEMail;
        this.mSEC = mSEC;
        this.mBN = mBN;
        this.mYear = mYear;
    }

    public Student(String mName, String mEMail, int mSEC, int mBN, int mYear) {
        this.mName = mName;
        this.mEMail = mEMail;
        this.mSEC = mSEC;
        this.mBN = mBN;
        this.mYear = mYear;

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
