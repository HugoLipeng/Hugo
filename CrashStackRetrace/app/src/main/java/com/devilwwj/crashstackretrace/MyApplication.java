package com.devilwwj.crashstackretrace;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * com.devilwwj.crashstackretrace
 * Created by devilwwj on 16/9/4.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化Bugly崩溃捕获
        CrashReport.initCrashReport(getApplicationContext(), "900029763", false);

    }
}
