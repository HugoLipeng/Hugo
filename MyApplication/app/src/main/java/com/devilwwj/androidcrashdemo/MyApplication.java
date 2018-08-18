package com.devilwwj.androidcrashdemo;

import android.app.Application;

/**
 * com.devilwwj.androidcrashdemo
 * Created by devilwwj on 16/5/27.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 这里为应用设置异常处理，然后程序才能获取未处理的异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }
}
