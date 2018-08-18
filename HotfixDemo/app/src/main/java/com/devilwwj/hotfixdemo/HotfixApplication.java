package com.devilwwj.hotfixdemo;

import android.app.Application;
import android.content.Context;

import com.devilwwj.hotfixlib.HotFix;

import java.io.File;

/**
 * com.devilwwj.hotfixdemo
 * Created by devilwwj on 16/3/9.
 */
public class HotfixApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        File dexPath = new File(getDir("dex", Context.MODE_PRIVATE), "wwj_dex.jar");
        Utils.prepareDex(this.getApplicationContext(), dexPath, "wwj_dex.jar");
        HotFix.patch(this, dexPath.getAbsolutePath(), "com.devilwwj.wwjdex.AntilazyLoad");
        try {
            this.getClassLoader().loadClass("com.devilwwj.wwjdex.AntilazyLoad");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        /*File dexPath = new File(getDir("dex", Context.MODE_PRIVATE), "patch_dex.jar");
        Utils.prepareDex(this.getApplicationContext(), dexPath, "patch_dex.jar");
        HotFix.patch(this, dexPath.getAbsolutePath(), "com.devilwwj.hotfixdemo.BugClass");
        try {
            this.getClassLoader().loadClass("com.devilwwj.hotfixdemo.BugClass");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/

    }
}
