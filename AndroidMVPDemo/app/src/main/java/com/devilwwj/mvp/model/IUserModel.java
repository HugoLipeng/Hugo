package com.devilwwj.mvp.model;

import com.devilwwj.mvp.bean.UserBean;

/**
 *建立model（处理业务逻辑，这里指数据读写），先写接口，后写实现
 * Created by wwj_748 on 2016/4/26.16.32
 */
public interface IUserModel {
    void setID(int id);

    void setFirstName(String firstName);

    void setLastName(String lastName);

    int getID();

    UserBean load(int id); // 通过id读取user信息，返回UserBean
}
