package com.example.administrator.myapplication;

import android.app.Application;

public class MyApplication extends Application {
    private String reback = null;
    private String typevalue = "";

    public void setScore_type(String ss){this.typevalue = ss;}

    public String getScore_type(){return typevalue;}

    public String getScore() {
        return reback;
    }

    public void setScore(String ss) {
        this.reback = ss;
    }
}
