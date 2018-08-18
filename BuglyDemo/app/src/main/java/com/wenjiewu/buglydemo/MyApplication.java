package com.wenjiewu.buglydemo;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by wenjiewu on 2016/5/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 最简单的初始化，Bugly2.0及以上版本还支持通过“AndroidManifest.xml”来配置APP信息
        // CrashReport.initCrashReport(getApplicationContext(), "注册时申请的APPID", false);
        CrashReport.initCrashReport(getApplicationContext(), "900029763", false);

        // 测试crash
        CrashReport.testJavaCrash();
    }
}
