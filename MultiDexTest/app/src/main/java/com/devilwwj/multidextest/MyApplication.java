package com.devilwwj.multidextest;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by wwj_748 on 2016/2/23.17.05
 */
public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 第三种方案
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
