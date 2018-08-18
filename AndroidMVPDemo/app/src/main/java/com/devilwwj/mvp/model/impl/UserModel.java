package com.devilwwj.mvp.model.impl;

import com.devilwwj.mvp.bean.UserBean;
import com.devilwwj.mvp.model.IUserModel;

/**
 * Created by wwj_748 on 2008/8/26.17.35
 */
public class UserModel implements IUserModel {
    @Override
    public void setID(int id) {

    }

    @Override
    public void setFirstName(String firstName) {

    }

    @Override
    public void setLastName(String lastName) {

    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public UserBean load(int id) {
        return new UserBean("11", "22");
    }
}
