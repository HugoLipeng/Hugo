package com.devilwwj.bughd;

import android.app.Application;

import im.fir.sdk.FIR;

/**
 * Created by wenjiewu on 2016/6/23.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FIR.init(this);
    }
}
