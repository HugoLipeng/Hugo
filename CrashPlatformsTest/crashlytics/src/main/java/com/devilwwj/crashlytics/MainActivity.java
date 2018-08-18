package com.devilwwj.crashlytics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.appsee.Appsee;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.devilwwj.jni.TestJNI;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

//    static {
//        System.loadLibrary("mylib");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        long start = System.currentTimeMillis();
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        long end = System.currentTimeMillis();
        Log.e("time:", (end - start) + "");


//        Appsee.start(getString(R.string.com_appsee_apikey));
        // 自定义log
//        Crashlytics.log("Higgs-Boson detected! Bailing out...");
//        // 自定义key
//        Crashlytics.setInt("current_level", 3);
//        Crashlytics.setString("last_UI_action", "fffff");
//
//
//        testNativeCrash();
    }


    public void testJavaCrash() {
        throw new RuntimeException(
                "This Crash create for Test! You can go to Bugly see more detail!");

    }

    public void testNull() {
        Object obj = null;
        obj.toString();
    }

    public static void createIndexOutOfBoundsException() {
        String[] strs = new String[2];
        strs[0] = "123";
        strs[1] = "456";
        strs[2] = "789"; // 这里数组越界了
    }

    /**
     * 触发一个ANR异常 之前版本是用Receiver做一个ANR保证其在主线程执行，但ANR本身就可能采集失败，为了节省包体积，先改为直接执行
     */
    public void testANRCrash() {
        try {
            int trySleep = 0;
            while (trySleep++ < 30) {
                Log.d("ANR", "try main sleep for make a test anr! try:" + trySleep + "/30" + ", kill it if you don't want to wait!");

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
    }


    public void testNativeCrash() {
        TestJNI testJNI = new TestJNI();
        testJNI.createANativeCrash();
    }
}
