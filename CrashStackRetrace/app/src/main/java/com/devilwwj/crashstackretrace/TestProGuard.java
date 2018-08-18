package com.devilwwj.crashstackretrace;

import android.util.Log;

/**
 * com.devilwwj.crashstackretrace
 * Created by devilwwj on 16/9/4.
 */
public class TestProGuard {

    private static final String TAG = "TestProGuard";

    public static void beforeProGuard() {
        Log.d(TAG, "beforeProGuard");
        createAnCrash();
    }


    public static void createAnCrash() {
        throw new NullPointerException("This is a nullpointerException");
    }
}
