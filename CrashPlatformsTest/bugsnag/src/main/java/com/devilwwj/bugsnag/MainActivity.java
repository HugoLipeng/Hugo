package com.devilwwj.bugsnag;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bugsnag.android.Bugsnag;
import com.devilwwj.jni.TestJNI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bugsnag.init(this);

//        Bugsnag.notify(new RuntimeException("Non-fatal"));

//        testJavaCrash();
        testNativeCrash();

    }


    public void testJavaCrash() {
        throw new RuntimeException(
                "This Crash create for Test! You can go to Bugly see more detail!");
    }


    public void testNativeCrash() {
        TestJNI testJNI = new TestJNI();
        testJNI.createANativeCrash();
    }
}
