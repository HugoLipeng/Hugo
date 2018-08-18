package com.devilwwj.jni;

/**
 * com.devilwwj.jnidemo
 * Created by devilwwj on 16/4/30.
 */
public class TestJNI {
    public native String hello();
    public native boolean Init();
    public native int Add(int x, int y);
    public native void destory();

    public native void createANativeCrash();

    public native void createANativeCrash2();
}
