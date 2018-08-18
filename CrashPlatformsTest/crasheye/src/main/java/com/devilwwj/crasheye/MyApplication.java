package com.devilwwj.crasheye;

import android.app.Application;

import com.xsj.crasheye.Crasheye;

/**
 * Created by wenjiewu on 2016/6/14.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Crasheye.init(this, "eb53bd80");
    }
}
