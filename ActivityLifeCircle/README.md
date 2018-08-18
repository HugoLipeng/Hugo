我们来回顾一下关于Activity的知识点，这个是面试中最常问的问题之一，Activity也是Android中四大组件，最重要的一个组件，我们在实际开发中，需要我们开发者理解它的生命周期还有对启动模式特殊场景的使用。

# 生命周期

那七个方法你不可能不熟悉：
onCreate->onRestart->onStart->onResume->onPause->onStop->onDestory

Activity的创建和销毁都会回调这七个方法：
onCreate：Activity创建成功回调。
onRestart：Activity重新启动时回调。
onStart：Activity正在被启动时回调。
onResume：Activity可见时回调。
onPause：Activity不可见时回调。
onStop：Activity停止时回调。
onDestory：Activity被销毁时回调。

这样简单描述，你们肯定都懂。我们来看具体场景：

场景一：正常启动
会执行：onCreate->onStart->onResume
这时候，Activity是完全可见的。

场景二：新开Activity
会执行：onPause->onStop
这时候，Activity是完全不可见的。

场景三：重新回到Activity
会执行：onRestart->onStart->onResume
这时候，Activity又变得可见了。

场景四：按back退出
会执行：onPause->onStop->onDestory
这时候，Activity被销毁了。

好了，这些场景你们开发肯定会遇到，通过打log自己尝试。

```

package com.devilwwj.activitylifecircle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * @desc Activity生命周期-典型情况下
 * @author devilwwj
 * @date 2016/3/7
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    /**
     * 表示Activity正在创建
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 表示Activity正在重新启动
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    /**
     * 表示Activity正在被启动
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    /**
     * 表示Activity已经可见
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    /**
     * 表示Activity正在停止
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    /**
     * 表示Activity即将停止
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    /**
     * 表示Activity即将被销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}

```

上面讲的是正常情况下的生命周期，那异常情况下的生命周期又是怎样的？
引入两个方法

- onSaveInstanceState
- onRestoreInstanceState

一般情况下，这两个方法不会被执行，那什么时候会执行？
有两种情形：
情形1： 系统配置发生改变时导致Activity被杀死并重新创建
最典型的场景就是：旋转屏幕
```
 android:configChanges="orientation|screenSize"
```

可以在Activity的配置，增加这个来避免重新创建Activity

情形2：系统资源不足（内存）低优先级的Activity被杀死

三种优先级：

- 前台Activity，优先级最高
- 可见但非前台Activity
- 后台Activity，优先级最低
上面两种情形导致Activity被销毁，这时我们可以通过上面说的两个方法进行数据的保存和恢复，例如：

```
/**
     * 正常情况不会回调这个方法
     * Activity被意外销毁的时候会保存数据
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putString("extra_test", "test");
    }
 /**
     * 正常情况不会回调这个方法
     * 在onStart之后调用
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState");
        String test = savedInstanceState.getString("extra_test");
        Log.d(TAG, "[onRestoreInstanceState] restore extra_test:" + test);
    }
我们也可以在onCreate方法进行数据恢复：

```

 /**
     * 表示Activity正在创建
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d(TAG, "onCreate");

        // 可以在onCreate方法内进行数据恢复，但需要判断是否为null
        if (savedInstanceState != null) {
            String test = savedInstanceState.getString("extra_test");
            Log.d(TAG, "[onCreate] restore extra_test:" + test);
        }
    }
```

好了，关于Activity的生命周期就回顾到这里了。这个对于我们Android开发者是一个很基础的问题，更加深入的就是系统的回收机制相关的了，大家可以继续深入的学习。