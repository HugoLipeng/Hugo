package com.devilwwj.blockcanarydemo;

import android.app.Application;

import com.github.moduth.blockcanary.BlockCanary;

/**
 * Created by wenjiewu on 2016/7/4.
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }
}
