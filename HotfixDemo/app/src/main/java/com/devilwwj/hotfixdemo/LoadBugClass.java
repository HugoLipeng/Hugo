package com.devilwwj.hotfixdemo;

/**
 * com.devilwwj.hotfixdemo
 * Created by devilwwj on 16/3/8.
 */
public class LoadBugClass {
    public String getBugString() {
        BugClass bugClass = new BugClass();
        return bugClass.bug();
    }
}
