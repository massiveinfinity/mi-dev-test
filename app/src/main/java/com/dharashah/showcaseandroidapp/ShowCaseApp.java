package com.dharashah.showcaseandroidapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by USER on 21-01-2016.
 */
public class ShowCaseApp extends Application {
    private static ShowCaseApp mApp;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp= this;
        mContext = this;
    }

    public static ShowCaseApp getAppContext(){
        if(mApp == null) {
            mApp = (ShowCaseApp)mContext;
        }
        return mApp;
    }
}
