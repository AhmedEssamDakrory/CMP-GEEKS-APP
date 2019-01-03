package com.example.android.cmpgeeks.DataBaseEntity;

public class MaterialContent {
    private String mLink;
    private MaterialTypes mType;

    public MaterialContent(String mLink, MaterialTypes mType) {
        this.mLink = mLink;
        this.mType = mType;
    }

    public String getmLink() {
        return mLink;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public MaterialTypes getmType() {
        return mType;
    }

    public void setmType(MaterialTypes mType) {
        this.mType = mType;
    }


    //Inner enum
    enum MaterialTypes{
        //add types
        unknown,
    }

}

