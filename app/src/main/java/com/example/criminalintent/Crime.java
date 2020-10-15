package com.example.criminalintent;

import java.util.Date;
import java.util.UUID;

//Базовый класс являющийся основой для хранения данных о преступлении

public class Crime {
    private UUID mID;
    private String mTitle;
    private Boolean mSolved;
    private Date mDate;


    public Crime(){
        mID = UUID.randomUUID();
        mDate = new Date();
    }



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

    public Boolean getmSolved() {
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




}
