# 前言
好几个月之前关于Android App热补丁修复火了一把，源于QQ空间团队的一篇文章[安卓App热补丁动态修复技术介绍](http://zhuanlan.zhihu.com/magilu/20308548)，然后各大厂的开源项目都出来了，本文的实践基于[HotFix](https://github.com/dodola/HotFix)，也就是QQ空间技术团队那篇文章所应用的技术，笔者会把整个过程的细节和思路在文章中详说，研究这个的出发点也是为了能紧急修复app的bug，而不需要重复发包，不需要用户重新下载app就能把问题解决，个人觉得这个还是蛮有价值的，虽然老板不知道....。

# 项目结构

![项目结构](http://img.blog.csdn.net/20160309152937798)

这里笔者创建一个新的项目"HotFixDemo"，带大家一步一步来完成Hotfix这个框架实现，这个项目包含以下module：
- app ：我们的Android应用程序Module。
- buildsrc ：使用Groovy实现的项目，提供了一个类，用来实现修改class文件的操作。
- hackdex ：提供了一个类，后面会用来打包成hack.dex，也是buildsrc里面实现在所有类的构造函数插入的一段代码所引用到的类。
- hotfixlib ：这个module最终会被app关联，里面提供实现热补丁的核心方法

这个Demo里面的代码跟HotFix框架基本无异，主要是告诉大家它实现的过程，如果光看代码，不实践是无法把它应用到你自己的app上去的，因为有很多比较深入的知识需要你去理解。

# 先说原理
关于实现原理，QQ空间那篇文章已经说过了，这里我再重新阐述一遍：
- Android使用的是PathClassLoader作为其类的加载器
- 一个ClassLoader可以包含多个dex文件，每个dex文件是一个Element，多个dex排列成一个有序的**dexElements**数组
- 当找类的时候会遍历dexElements数组，从dex文件中找类，找到则返回，否则继续下一个dex文件查找
- 热补丁的方案，其实就是将有问题的类单独打包成一个dex文件（如：patch.dex），然后将这个dex插入到dexElements数组的最前面去。

ok，这个就是HotFix对app进行热补丁的原理，**其实就是用ClassLoader加载机制，覆盖掉有问题的方法，然后我们所谓的补丁就是将有问题的类打成一个包**。

# 再说问题
当然要实现热补丁动态修复不会很容易，我们首要解决的一个问题是：
>**当虚拟机启动时，当verify选项被打开时，如果static方法、private方法、构造函数等，其中的直接引用（第一层关系）到的类都在同一个dex文件中，那么该类会被打上CLASS_ISPREERIFIED标记**

如下图所示：
![CLASS_ISPREERIFIED](http://img.blog.csdn.net/20160309161102236)

如果一个类被打上了CLASS_ISPREERIFIED这个标志，如果该类引用的另外一个类在另一个dex文件，就会报错。简单来说，就是你在打补丁之前，你所修复的类已经被打上标记，你通过补丁去修复bug的时候这个时候你就不能完成校验，就会报错。

# 解决问题
要解决上一节所提到的问题就要在apk打包之前就阻止相关类打上CLASS_ISPREERIFIED标志，解决方案如下：
 在所有类的构造函数插入一段代码，如：

```
public class BugClass {
    public BugClass() {
        System.out.println(AntilazyLoad.class);
    }
    
    public String bug() {
        return "bug class";
    }
}

```

> 其中引用到的AntilazyLoad这个类会单独打包成hack.dex，这样当安装apk的时候，classes.dex内的类都会引用一个不相同的dex中的AntilazyLoad类，这样就解决CLASS_ISPREERIFIED标记问题了。


# 实现细节
上面几节讲完原理、之后抛出了问题，再提出解决方案，相信大家对整个热补丁修复框架有了一定的认识，至少我们知道它到底是怎么一回事。下面来讲实现细节：

## 创建两个类

```
package com.devilwwj.hotfixdemo;

/**
 * com.devilwwj.hotfixdemo
 * Created by devilwwj on 16/3/8.
 */
public class BugClass {

    public String bug() {
        return "bug class";
    }
}
```

```
package com.devilwwj.hotfixdemo;

/**
 * com.devilwwj.hotfixdemo
 * Created by devilwwj on 16/3/8.
 */
public class LoadBugClass {
    public String getBugString() {
        BugClass bugClass = new BugClass();
        return bugClass.bug();
    }
}
```

我们需要做的是在这两个类的class文件的构造方法中插入一段代码：

```
System.out.println(AntilazyLoad.class);
```

## 创建hackdex模块并创建AntilazyLoad类

看图就好了：

![hackdex模块](http://img.blog.csdn.net/20160309164412186)


## 将AntilazyLoad单独打成hack_dex.jar包

通过以下命令来实现：

```
jar cvf hack.jar com.devilwwj.hackdex/*
```
这个命令会将AntilazyLoad类打包成hack.jar文件

```
dx --dex --output hack_dex.jar hack.jar
```
这个命令使用dx工具对hack.jar进行转化，生成hack_dex.jar文件


dx工具在我们的sdk/build-tools下
![dx工具](http://img.blog.csdn.net/20160309165327073)

最终我们把hack_dex.jar文件放到项目的assets目录下：

![hack_dex.jar](http://img.blog.csdn.net/20160309165550425)

## 使用javassist实现动态代码注入
创建buildSrc模块，这个项目是使用Groovy开发的，需要配置Groovy SDK才可以编译成功。
在这里下载Groovy SDK：[http://groovy-lang.org/download.html](http://groovy-lang.org/download.html)，下载之后，配置项目user Library即可。

它里面提供了一个方法，用来向指定类的构造函数注入代码：

```
 /**
     * 植入代码
     * @param buildDir 是项目的build class目录，就是我们需要注入的class所在地
     * @param lib 这个是hackdex的目录，就是AntilazyLoad类的class文件所在地
     */
    public static void process(String buildDir, String lib) {
        println(lib);
        ClassPool classes = ClassPool.getDefault()
        classes.appendClassPath(buildDir)
        classes.appendClassPath(lib)

        // 将需要关联的类的构造方法中插入引用代码
        CtClass c = classes.getCtClass("com.devilwwj.hotfixdemo.BugClass")
        if (c.isFrozen()) {
            c.defrost()
        }
        println("====添加构造方法====")
        def constructor = c.getConstructors()[0];
        constructor.insertBefore("System.out.println(com.devilwwj.hackdex.AntilazyLoad.class);")
        c.writeFile(buildDir)

        CtClass c1 = classes.getCtClass("com.devilwwj.hotfixdemo.LoadBugClass")
        if (c1.isFrozen()) {
            c1.defrost()
        }
        println("====添加构造方法====")
        def constructor1 = c1.getConstructors()[0];
        constructor1.insertBefore("System.out.println(com.devilwwj.hackdex.AntilazyLoad.class);")
        c1.writeFile(buildDir)

    }
```


## 配置app项目的build.gradle

上一小节创建的module提供相应的方法来让我们对项目的类进行代码注入，我们需要在build.gradle来配置让它自动来做这件事：

```
apply plugin: 'com.android.application'

task('processWithJavassist') << {
    String classPath = file('build/intermediates/classes/debug')// 项目编译class所在目录
    com.devilwwj.patch.PatchClass.process(classPath, project(':hackdex').buildDir.absolutePath + "/intermediates/classes/debug") // 第二个参数是hackdex的class所在目录
}

android {
    compileSdkVersion 23
    buildToolsVersion "23.0.1"

    defaultConfig {
        applicationId "com.devilwwj.hotfixdemo"
        minSdkVersion 14
        targetSdkVersion 23
        versionCode 1
        versionName "1.0"
    }
    buildTypes {
        debug {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
    applicationVariants.all { variant ->
        variant.dex.dependsOn << processWithJavassist // 在执行dx命令之前将代码打入到class中
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    testCompile 'junit:junit:4.12'
    compile 'com.android.support:appcompat-v7:23.1.1'
    compile 'com.android.support:design:23.1.1'
    compile project(':hotfixlib')
}

```

这时候我们run项目，反编译build/output/apk下的app-debug.apk文件，你就可以看到代码已经成功植入了。

mac下的反编译工具：
[https://sourceforge.net/projects/jadx/?source=typ_redirect](https://sourceforge.net/projects/jadx/?source=typ_redirect)

反编译的结果如下图：

![反编译结果](http://img.blog.csdn.net/20160309174232088)

其实你也可以直接在项目中看：

![代码注入结果](http://img.blog.csdn.net/20160309174352039)


## 创建hotfixlib模块，并关联到项目中

这差不多是最后一步了，也是最核心的一步，提供将heck_dex.jar动态插入到dexElements的方法。

核心代码：
```
package com.devilwwj.hotfixlib;

import android.annotation.TargetApi;
import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * com.devilwwj.hotfixlib
 * Created by devilwwj on 16/3/9.
 */
public final class HotFix {
    public static void patch(Context context, String patchDexFile, String patchClassName) {
        if (patchDexFile != null && new File(patchDexFile).exists()) {
            try {
                if (hasLexClassLoader()) {
                    injectAliyunOs(context, patchDexFile, patchClassName);
                } else if (hasDexClassLoader()) {
                    injectAboveEqualApiLevel14(context, patchDexFile, patchClassName);
                } else {
                    injectBelowApiLevel14(context, patchDexFile, patchClassName);
                }
            } catch (Throwable th) {

            }
        }
    }

    private static boolean hasLexClassLoader() {
        try {
            Class.forName("dalvik.system.LexClassLoader");
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }

    private static boolean hasDexClassLoader() {
        try {
            Class.forName("dalvik.system.BaseDexClassLoader");
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void injectAliyunOs(Context context, String patchDexFile, String patchClassName) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        PathClassLoader obj = (PathClassLoader) context.getClassLoader();
        String replaceAll = new File(patchDexFile).getName().replaceAll("\\.[a-zA-Z0-9]+", ".lex");
        Class cls = Class.forName("dalvik.system.LexClassLoader");
        Object newInstance = cls.getConstructor(new Class[]{String.class, String.class, String.class, ClassLoader.class}).newInstance(
                new Object[]{context.getDir("dex", 0).getAbsolutePath()
                        + File.separator + replaceAll, context.getDir("dex", 0).getAbsolutePath(), patchDexFile, obj});
        cls.getMethod("loadClass", new Class[]{String.class}).invoke(newInstance, new Object[]{patchClassName});
        setField(obj, PathClassLoader.class, "mPaths", appendArray(getField(obj, PathClassLoader.class, "mPaths"), getField(newInstance, cls, "mRawDexPath")));
        setField(obj, PathClassLoader.class, "mFiles", combineArray(getField(obj, PathClassLoader.class, "mFiles"), getField(newInstance, cls, "mFiles")));
        setField(obj, PathClassLoader.class, "mZips", combineArray(getField(obj, PathClassLoader.class, "mZips"), getField(newInstance, cls, "mZips")));
        setField(obj, PathClassLoader.class, "mLexs", combineArray(getField(obj, PathClassLoader.class, "mLexs"), getField(newInstance, cls, "mDexs")));
    }

    @TargetApi(14)
    private static void injectBelowApiLevel14(Context context, String str, String str2) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        PathClassLoader obj = (PathClassLoader) context.getClassLoader();
        DexClassLoader dexClassLoader = new DexClassLoader(str, context.getDir("dex", 0).getAbsolutePath(), str, context.getClassLoader());
        dexClassLoader.loadClass(str2);
        setField(obj, PathClassLoader.class, "mPaths",
                appendArray(getField(obj, PathClassLoader.class, "mPaths"), getField(dexClassLoader, DexClassLoader.class, "mRawDexPath")));
        setField(obj, PathClassLoader.class, "mFiles",
                combineArray(getField(obj, PathClassLoader.class, "mFiles"), getField(dexClassLoader, DexClassLoader.class, "mFiles")));
        setField(obj, PathClassLoader.class, "mZips",
                combineArray(getField(obj, PathClassLoader.class, "mZips"), getField(dexClassLoader, DexClassLoader.class, "mZips")));
        setField(obj, PathClassLoader.class, "mDexs",
                combineArray(getField(obj, PathClassLoader.class, "mDexs"), getField(dexClassLoader, DexClassLoader.class, "mDexs")));
        obj.loadClass(str2);
    }

    /**
     * 将dex注入dexElements数组中
     * @param context
     * @param str
     * @param str2
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static void injectAboveEqualApiLevel14(Context context, String str, String str2) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        Object a = combineArray(getDexElements(getPathList(pathClassLoader)), getDexElements(getPathList(new DexClassLoader(str, context.getDir("dex", 0).getAbsolutePath(), str, context.getClassLoader()))));
        Object a2 = getPathList(pathClassLoader);
        setField(a2, a2.getClass(), "dexElements", a);
        pathClassLoader.loadClass(str2);
    }

    /**
     * 通过PathClassLoader拿到pathList
     * @param obj
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getPathList(Object obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return getField(obj, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }


    /**
     * 通过pathList取得dexElements对象
     * @param obj
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getDexElements(Object obj) throws NoSuchFieldException, IllegalAccessException {
        return getField(obj, obj.getClass(), "dexElements");
    }


    /**
     * 通过反射拿到指定对象
     * @param obj
     * @param cls
     * @param str
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getField(Object obj, Class cls, String str) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField.get(obj);
    }


    /**
     * 通过反射设置属性
     * @param obj
     * @param cls
     * @param str
     * @param obj2
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static void setField(Object obj, Class cls, String str, Object obj2) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        declaredField.set(obj, obj2);
    }

    /**
     * 合并数组
     * @param obj
     * @param obj2
     * @return
     */
    private static Object combineArray(Object obj, Object obj2) {
        Class componentType = obj2.getClass().getComponentType();
        int length = Array.getLength(obj2);
        int length2 = Array.getLength(obj) + length;
        Object newInstance = Array.newInstance(componentType, length2);
        for (int i = 0; i < length2; i++) {
            if (i < length) {
                Array.set(newInstance, i, Array.get(obj2, i));
            } else {
                Array.set(newInstance, i, Array.get(obj, i - length));
            }
        }
        return newInstance;
    }

    /**
     * 添加到数组
     * @param obj
     * @param obj2
     * @return
     */
    private static Object appendArray(Object obj, Object obj2) {
        Class componentType = obj.getClass().getComponentType();
        int length = Array.getLength(obj);
        Object newInstance = Array.newInstance(componentType, length + 1);
        Array.set(newInstance, 0, obj2);
        for (int i = 0; i < length + 1; i++) {
            Array.set(newInstance, i, Array.get(obj, i - 1));
        }
        return newInstance;
    }
}

```


## 准备补丁，最后测试结果

补丁是我们程序修复bug的包，如果我们已经上线的包出现了bug，你需要紧急修复，那你就找到有bug的那个类，将它修复，然后将这个修复的class文件打包成jar包，让服务端将这个补丁包放到指定位置，你的就程序就可以将这补丁包下载到sdcard，之后就是程序自动帮你打补丁把问题修复。

比如我们上面提到的BugClass：
未修复之前：

```
public class BugClass {

    public String bug() {
        return "bug class";
    }
}
```

修复之后：

```
public class BugClass {

    public String bug() {
        return "小巫将bug修复啦！！！";
    }
}

```

你要做的就是替换这个类，怎么做？

先打包：

![打包命令](http://img.blog.csdn.net/20160310125151109)

*记住：一定要经过dx工具转化，然后路径一定要对*

patch_dex.jar就是我们的补丁包，这里我们为了演示把它放到项目的assets目录下：

![补丁包](http://img.blog.csdn.net/20160310125453204)

之后，就是测试效果了，看动态图：

![打补丁过程](http://img.blog.csdn.net/20160310130014523)

好，到这里就大公告成了，我们的bug被修复了啦。


# 总结
本次实践过程是基于HotFix框架，在这里感谢开源的作者，因为不满足于拿作者的东西直接用，然后不知道为什么，所以笔者把整个过程都跑了一遍，正所谓实践出真知，原本以为很难的东西通过反复实践也会变得不那么难，期间实践自然不会那么顺利，笔者就遇到一个坑，比如Groovy编译，hack_dex包中的类找不到等等，但最后都一一解决了，研究完这个热更新框架，再去研究其他热更新框架也是一样的，因为原理都一样，所以就不纠结研究哪个了，之后笔者也会把这个技术用到项目中去，不用每次发包也是心情愉悦的，最后感谢各位看官耐心看，有啥问题就直接留言吧。




> 参考：
> http://blog.csdn.net/lmj623565791/article/details/49883661
> http://www.jianshu.com/p/56facb3732a7


![1元表真心](http://img.blog.csdn.net/20160310131859376)
