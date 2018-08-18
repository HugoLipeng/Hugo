package com.devilwwj.activitylaunchmode;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

/**
 * Created by wwj_748 on 2016/3/7.17.24
 */
public class SecondActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    /**
     * 这里跳转到ThirdActivity
     * @param view
     */
    public void enterThirdActivity(View view) {
        // action的匹配，必须设置
        Intent intent = new Intent("com.devilwwj.action.c");
        // categoty的匹配，可不设，如果设置了，必须与配置文件一致
        intent.addCategory("com.devilwwj.category.c");
        // data匹配，Uri默认值为content和file
        intent.setDataAndType(Uri.parse("file://abc"), "text/plain");
        startActivity(intent);
    }
}
