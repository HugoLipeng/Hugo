package com.devilwwj.testin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNullPointerException();
    }


    /**
     * 创建一个空指针异常
     * 调用了未经初始化的对象或者不存在的对象
     */
    public static void createNullPointerException() {
        Object object = null;
        object.toString();
    }
}
