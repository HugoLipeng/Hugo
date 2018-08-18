package com.devilwwj.mvp.view;

/**
 * 建立View（更新UI中的View状态）
 * Created by wwj_748 on 2016/4/26.16.34
 */
public interface IUserView {
    int getID();

    String getFirstName();

    String getLastName();

    void setFirstName(String firstName);

    void setLastName(String lastName);
}
