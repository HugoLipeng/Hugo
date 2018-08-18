package com.devilwwj.testin;

import android.app.Application;

import com.testin.agent.TestinAgent;
import com.testin.agent.TestinAgentConfig;

/**
 * Created by wenjiewu on 2016/6/23.
 */
public class MyApplication extends Application {
    public static final String APP_KEY = "";
    public static final String CHANNEL = "";
    public static final String USERINFO = "";

    @Override
    public void onCreate() {
        super.onCreate();
        TestinAgent.init(this, "d23db445b82820cc5edda057678e1b6e", "bugly");
        TestinAgent.setLocalDebug( true );

        /*TestinAgentConfig config = new TestinAgentConfig.Builder(this)
                .withAppKey(APP_KEY)   // 您的应用的 AppKey,如果已经在 Manifest 中配置则此处可略
                .withAppChannel(CHANNEL)   // 发布应用的渠道,如果已经在 Manifest 中配置则此处可略
                .withUserInfo(USERINFO)   // 用户信息-崩溃分析根据用户记录崩溃信息
                .withDebugModel(true)   // 输出更多SDK的debug信息
                .withErrorActivity(true)   // 发生崩溃时采集Activity信息
                .withCollectNDKCrash(true)   // 收集NDK崩溃信息
                .withOpenCrash(true)   // 收集崩溃信息开关
                .withOpenEx(true)   // 是否收集异常信息
                .withReportOnlyWifi(true)   // 仅在 WiFi 下上报崩溃信息
                .withReportOnBack(true)   // 当APP在后台运行时,是否采集信息
                .withQAMaster(true)   // 是否收集摇一摇反馈
                .withCloseOption(false)   // 是否在摇一摇菜单展示‘关闭摇一摇选项’
                .withLogcat(true)   // 是否系统操作信息
                .build();
        TestinAgent.init(config);*/
    }
}
