package com.devilwwj.mvp.presenter;

import com.devilwwj.mvp.bean.UserBean;
import com.devilwwj.mvp.model.IUserModel;
import com.devilwwj.mvp.model.impl.UserModel;
import com.devilwwj.mvp.view.IUserView;

/**
 * 建立presenter(主导器，通过iView和iModel接口操作model和View）
 * activity可以把所有逻辑交给presenter处理，这样java逻辑就从手机的activity中分离出来
 * Created by wwj_748 on 2016/4/26.16.37
 */
public class UserPresenter {
    private IUserView mUserView;
    private IUserModel mUserModel;

    public UserPresenter(IUserView view) {
        mUserView = view;
        mUserModel = new UserModel();
    }

    public void saveUser(int id, String firstName, String lastName) {
        mUserModel.setID(id);
        mUserModel.setFirstName(firstName);
        mUserModel.setLastName(lastName);
    }

    public void loadUser(int id) {
        UserBean user = mUserModel.load(id);
        mUserView.setFirstName(user.getFirstName());
        mUserView.setLastName(user.getLastName());
    }
}
