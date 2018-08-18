package com.devilwwj.activitylifecircle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by wwj_748 on 2016/3/7.16.02
 */
public class SecondActivity extends Activity {

    private static final String TAG = "SecondActivity";

    /**
     * 表示Activity正在创建
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d(TAG, "onCreate");

        // 可以在onCreate方法内进行数据恢复，但需要判断是否为null
        if (savedInstanceState != null) {
            String test = savedInstanceState.getString("extra_test");
            Log.d(TAG, "[onCreate] restore extra_test:" + test);
        }
    }

    /**
     * 表示Activity正在重新启动
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    /**
     * 表示Activity正在被启动
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    /**
     * 正常情况不会回调这个方法
     * 在onStart之后调用
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState");
        String test = savedInstanceState.getString("extra_test");
        Log.d(TAG, "[onRestoreInstanceState] restore extra_test:" + test);
    }

    /**
     * 表示Activity已经可见
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    /**
     * 表示Activity正在停止
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    /**
     * 正常情况不会回调这个方法
     * Activity被意外销毁的时候会保存数据
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putString("extra_test", "test");
    }

    /**
     * 表示Activity即将停止
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    /**
     * 表示Activity即将被销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
