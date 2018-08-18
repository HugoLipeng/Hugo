package com.devilwwj.umeng;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;


/**
 * Created by wenjiewu on 2016/6/23.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobclickAgent.setDebugMode(true);
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.openActivityDurationTrack(false);

    }
}
