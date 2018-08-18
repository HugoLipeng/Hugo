package com.devilwwj.jnidemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.devilwwj.jni.TestJNI;

public class MainActivity extends AppCompatActivity {

    static {
        // 加载动态库
        System.loadLibrary("mylib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 测试Native Crash
     * @param view
     */
    public void testNativeCrash(View view) {
        TestJNI testJNI = new TestJNI();
        Log.d("test", "create a native crash");
        testJNI.createANativeCrash();
    }
}
