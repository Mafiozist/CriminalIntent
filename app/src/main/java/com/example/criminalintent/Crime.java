package com.example.criminalintent;

import java.util.Date;
import java.util.UUID;

//Базовый класс являющийся основой для хранения данных о преступлении

public class Crime {
    private UUID mID;
    private String mTitle;
    private boolean mSolved;
    private Date mDate;
    private String mSuspect;
    private boolean mRequiresPolice;


    public Crime(){
        this(UUID.randomUUID());
    }

    public Crime(UUID id){
        mID = id;
        mDate = new Date();
    }



    public boolean ismRequiresPolice() { return mRequiresPolice; }

    public void setmRequiresPolice(boolean mRequiresPolice) { this.mRequiresPolice = mRequiresPolice; }

    public UUID getmID() {
        return mID;
    }

    public void setmID(UUID mID) {
        this.mID = mID;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Boolean isSolved() {
        return mSolved;
    }

    public void setmSolved(Boolean mSolved) {
        this.mSolved = mSolved;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }

    public  String getPhotoFilename(){
        return "IMG_" + getmID().toString() + ".jpg";
    }
}
