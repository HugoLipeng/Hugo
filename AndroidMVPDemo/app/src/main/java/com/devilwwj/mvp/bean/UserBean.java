package com.devilwwj.mvp.bean;

/**
 * 建立Bean
 * Created by wwj_748 on 2016/4/26.16.30
 */
public class UserBean {
    private String mFirstName;
    private String mLastName;

    public UserBean(String mFirstName, String mLastName) {
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }
}
