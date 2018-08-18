//
// Created by DevilWwj on 16/8/28.
//
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <jni.h>
#include <assert.h>

/**
 * 定义native方法
 */
JNIEXPORT jstring JNICALL native_hello(JNIEnv *env, jclass clazz)
{
    printf("hello in c native code./n");
    return (*env)->NewStringUTF(env, "hello world returned.");
}

// 指定要注册的类
#define JNIREG_CLASS "com/devilwwj/library/JavaHello"

// 定义一个JNINativeMethod数组，其中的成员就是Java代码中对应的native方法
static JNINativeMethod gMethods[] = {
    { "hello", "()Ljava/lang/String;", (void*)native_hello},
};


static int registerNativeMethods(JNIEnv* env, const char* className,
JNINativeMethod* gMethods, int numMethods) {
    jclass clazz;
    clazz = (*env)->FindClass(env, className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if ((*env)->RegisterNatives(env, clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

/***
 * 注册native方法
 */
static int registerNatives(JNIEnv* env) {
    if (!registerNativeMethods(env, JNIREG_CLASS, gMethods, sizeof(gMethods) / sizeof(gMethods[0]))) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

/**
 * 如果要实现动态注册，这个方法一定要实现
 * 动态注册工作在这里进行
 */
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env = NULL;
    jint result = -1;

    if ((*vm)-> GetEnv(vm, (void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    assert(env != NULL);

    if (!registerNatives(env)) { //注册
        return -1;
    }
    result = JNI_VERSION_1_4;

    return result;

}






