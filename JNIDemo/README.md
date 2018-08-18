# 前言

因为工作需要可能要用到JNI开发，本篇文章就分享一下我在这方面的实践，以前我们使用Eclipse做NDK开发，非常麻烦，需要配cygwin的编译环境，后面NDK功能完善才逐渐简单点，如果想了解Eclipse如何配置NDK编译环境可以参考我以前发表的旧文：

[Cocos2d-x 2.2.3 使用NDK配置编译环境](http://blog.csdn.net/wwj_748/article/details/30072379)
[JNI_Android项目中调用.so动态库](http://blog.csdn.net/wwj_748/article/details/28300451)

Eclipse的如何通过NDK生成so库就不多说了，目前已经不适用于Android开发，建议使用AS进行开发，本篇也是基于AS来进行讲解。

# 什么是JNI
如果你进行Android开发一段时间还不知道什么是JNI的话，说明你还是初学者，没有理解Java层跟Native层之间是如何交互的。JNI（Java Native Interface的缩写），Java层通过JNI来调用Native层的功能模块，这样做的好处是能消除平台的差异性，避免重复制造轮子。Java的跨平台性也体现在这里。
如何通过JNI调用Native层的c/c++代码，可以参考我的一篇文章：
[JNI_最简单的Java调用C/C++代码](http://blog.csdn.net/wwj_748/article/details/28136061)

> 注：window的动态库是.dll文件，而Linux下的动态库是.so文件

# 配置NDK
如果翻不了墙，可以到这里下载相应平台的NDK版本
[http://www.androiddevtools.cn/](http://www.androiddevtools.cn/)

下载成功之后解压缩，然后配置系统环境变量，拿windows举例：
先新建NDK_HOME

![NDK_HOME](http://upload-images.jianshu.io/upload_images/224008-6c8653fe1add3bb6?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

然后再添加到PATH环境变量中

![PATH环境变量](http://upload-images.jianshu.io/upload_images/224008-a29db7e95f4d65f4?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

ok，配好之后，直接可以在命令行使用ndk-build命令：

![ndk-build](http://upload-images.jianshu.io/upload_images/224008-c1df80456c676027?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这里提示没有定义NDK_PROJECT_PATH变量，暂且不管，我们后面说。

然后，在Android Studio配置NDK路径：

![AS配置NDK](http://upload-images.jianshu.io/upload_images/224008-64935dcf98c33a69?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

上面是笔者的路径，具体按你们来配。

至此，NDK环境配置完毕。


# 定义Native方法
这里创建一个Android项目-JNIDemo，然后定义TestJNI类，代码如下：

```
package com.devilwwj.jnidemo;

/**
* Created by wwj_748 on 2016/4/27.15.47
*/
public class TestJNI {
public native boolean Init();
public native int Add(int x, int y);
public native void destory();
}

```

# 使用javah命令生成.h文件

![javah命令生成.h文件](http://upload-images.jianshu.io/upload_images/224008-db519778cda655b2?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

执行完上面的命令之后，就生成了com_devilwwj_jnidemo_TestJNI.h这个文件：

![com_devilwwj_jnidemo_TestJNI.h](http://upload-images.jianshu.io/upload_images/224008-e5a2eaa16e069a82?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

ok，这样我们就可以进行下一步操作了。

# 创建jni目录，创建.cpp文件

![创建jni目录](http://upload-images.jianshu.io/upload_images/224008-da10882c3ef3bcbf?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

然后根据.h文件，创建相对应的.cpp文件
com_devilwwj_jnidemo_TestJNI.cpp
```
//
// Created by wwj_748 on 2016/4/27.
//
#include <stdio.h>
#include <stdlib.h>
#include "com_devilwwj_jnidemo_TestJNI.h"
#include "Add.h"

CAdd *pCAdd = NULL;

JNIEXPORT jboolean JNICALL Java_com_devilwwj_jnidemo_TestJNI_Init(JNIEnv *env, jobject obj) {
if (pCAdd == NULL) {
pCAdd = new CAdd;
}
return pCAdd != NULL;
}

JNIEXPORT jint JNICALL Java_com_devilwwj_jnidemo_TestJNI_Add
(JNIEnv *env, jobject obj, jint x, jint y) {
int res = -1;
if (pCAdd != NULL) {
res = pCAdd->Add(x, y);
}
return res;
}

JNIEXPORT void JNICALL Java_com_devilwwj_jnidemo_TestJNI_destory
(JNIEnv *env, jobject obj) {
if (pCAdd != NULL) {
pCAdd = NULL;
}
}
```

这里我还需要创建两个文件，CAdd.h和CAdd.cpp：

![CAdd.h](http://upload-images.jianshu.io/upload_images/224008-d7417155ca04e0f5?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![CAdd.cpp](http://upload-images.jianshu.io/upload_images/224008-027eab29e7b93c42?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

ok，到目前我们已经完成JNI层的实现了。

# 创建Android.mk和Application.mk文件

在jni目录下，我们需要创建两个mk文件

**Android.mk**
```
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := JNIDemo
LOCAL_SRC_FILES := com_devilwwj_jnidemo_TestJNI.cpp
LOCAL_SRC_FILES += Add.cpp

include $(BUILD_SHARED_LIBRARY)
```

其中LOCAL_PATH是C/C++代码所在目录，也就是我们的jni目录。
LOCAL_MODULE是要编译的库的名称。编译器会自动在前面加上lib，在后面加上.so。
LOCAL_SRC_FILES是要编译的C/C++文件。

**Application.mk**
```
APP_ABI := all
```
表示生成所有平台的动态库。

# 配置gradle

在defaultConfig下，配置ndk：
```
ndk {
moduleName "JNIDemo" // 生成的so名字
}
```

在android标签内配置sourceSets：
```
sourceSets {
main {
jniLibs.srcDirs = ['libs']
}
}

```
生成的so文件都在src/main/libs目录下。

完整的gradle配置，可以看源码。

# 执行ndk-build命令生成所有cpu架构的so库

![ndk-build生成库](http://upload-images.jianshu.io/upload_images/224008-860dbd728c12b46a?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

然后就可以在libs目录下看到所有平台的so库：

![so库生成](http://upload-images.jianshu.io/upload_images/224008-98d0e4409343d7c5?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

至此，完整的so库实践基本完毕，期间遇到任何问题均可留言，欢迎讨论交流。


# 最后
关于NDK开发so库会有很多坑，本篇博客也只是引大家入门，我们在使用第三方的SDK的时候，就可能会用到别人提供的so库，也可能会遇到使用so库出现问题，有很多原因，可能是提供了不同的cpu架构的so库，在其他平台出现的crash或者是其他问题，相信我们会遇到的问题别人也会遇到，这时候google一下也许能找到解决方案，最后祝生活愉快。


----------
欢迎关注我的公众号：wwjblog
![wwjblog](https://raw.githubusercontent.com/fanatic-mobile-developer-for-android/A-week-to-develop-android-app-plan/master/images/wwjblog.jpg)