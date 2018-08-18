//
// Created by DevilWwj on 16/4/30.
//

//
// Created by wwj_748 on 2016/4/27.
//
#include <stdio.h>
#include <stdlib.h>
#include "com_devilwwj_jni_TestJNI.h"
#include "CAdd.h"

CAdd *pCAdd = NULL;

JNIEXPORT jboolean JNICALL Java_com_devilwwj_jni_TestJNI_Init(JNIEnv *env, jobject obj) {
  if (pCAdd == NULL) {
    pCAdd = new CAdd();
  }
  return pCAdd != NULL;
  }

JNIEXPORT jint JNICALL Java_com_devilwwj_jni_TestJNI_Add
  (JNIEnv *env, jobject obj, jint x, jint y) {
    int res = -1;
    if (pCAdd != NULL) {
        res = pCAdd->Add(x, y);
    }
    return res;
  }

  JNIEXPORT void JNICALL Java_com_devilwwj_jni_TestJNI_destory
    (JNIEnv *env, jobject obj) {
    if (pCAdd != NULL) {
        pCAdd = NULL;
    }
    }

JNIEXPORT void JNICALL
Java_com_devilwwj_jni_TestJNI_createANativeCrash(JNIEnv *env, jobject instance) {

    // TODO 制造一个native crash
    int *p = 0; // 空指针
    *p = 1; // 写空指针指向的内存，产生SIGSEGV信号，造成crash
    if (pCAdd != NULL) {
       pCAdd->Divide(12, 0);
    }
}