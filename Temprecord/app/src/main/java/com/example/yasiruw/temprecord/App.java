package com.example.yasiruw.temprecord;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.TimeZone;

public class App extends Application {
    private static Context mContext;
    private static TimeZone timeZone;

    @Override
    public void onCreate() {
        super.onCreate();
        timeZone = TimeZone.getDefault();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }

    public static TimeZone getInit_TimeZone(){
        return timeZone;
    }
}
