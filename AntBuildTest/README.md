# Android自动化构建之Ant多渠道打包实践（上）

# 前言

Ant是历史比较悠久的一个自动化构建工具，Android开发者可以通过它来实现自动化构建，也可以实现多渠道打包，关于apk打包的方式一般有Ant、Python、Gradle三种，这三种打包方式都各自有优点和缺点，本篇博文先给大家介绍如何使用Ant来实现自动构建和多渠道发布。

# 开发环境
- Window7
- Ant
- jdk
- android sdk

mac系统下所需要的运行环境也是类似的，我们都需要配置Ant、jdk、sdk的环境变量，我们可以看一下window下是环境变量配了些什么：
**ANT_HOME** : D:\android\apache-ant-1.9.4
**JAVA_HOME** : C:\Program Files\Java\jdk1.6.0_45
**ANDROID_SDK_HOME** : D:\android\adt-bundle-windows-x86_64-20140321\sdk
**PATH**: %JAVA_HOME%/bin;%ANDROID_SDK_HOME%\platform-tools;%ANDROID_SDK_HOME%\tools;%ANT_HOME%\bin;
**CLASSPATH** : .;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\tools.jar

以上环境变量配好之后，你才可以进入下一步，不会配？回家吧，开发不适合你。


# 先说APK构建过程

下面来简单描述下apk构建的过程：
1.	使用aapt生成R.java类文件
2.	使用android SDK提供的aidl.exe把.aidl转成Java文件
3.	使用javac命令编译.java类文件生成class文件
4.	使用android SDK提供的dx.bat命令行脚本生成classes.dex文件
5.	使用android SDK提供的aapt.exe生成资源包文件
6.	使用apkBuilder.bat生成未签名的apk安装文件
7.	使用jdk的jarsigner对未签名的包进行apk签名
8.	使用Zipalign工具对apk进行优化

我们从上面的描述可以知道apk具体的步骤和使用到的工具，对应的工具在sdk中都可以找到，自己去翻翻吧，但你会发现新的sdk版本，aapt被放到了**build-tools**目录下，**apkBuilder.bat**文件在tools目录找不到了，你可以去网上去下一个，然后放到tools目录下。为了让大家更清楚apk构建的过程，放上官方的一张图：

![apk构建过程](http://upload-images.jianshu.io/upload_images/224008-7cc7ab08c8841ee5?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

有了这张图，相信大家已经清楚了apk到底是如何生成的吧，不多说了。

# 构建命令详解
-  aapt命令生成R.java文件

示例命令：
```
aapt package -m -J <R.java文件夹> -S <res路径> -I <android.jar路径> -M<AndroidManifest.xml路径>
```

命令解释：

-f    如果编译出来的文件已经存在，强制覆盖
-m  使生成的包的目录存放在-J参数指定的目录
-J    指定生成的R.java 的输出目录 
-S    res文件夹路径
-A    assert文件夹路径
-I     某个版本平台的android.jar的路径
-F    具体指定APK文件的输出


- aidl命令生成.aidl文件

示例命令：
```
aidl -p<framework.aidl路径> -I<src路径> -o<目标文件目录> .aidl文件
```
注意：**命令和路径是没有空格的**。


- javac命令生成.class文件

示例命令：
```
javac -d destdir srcFile
```
参数解释：
-d 指定存放类的文件夹
-bootclasspath  覆盖引导类文件的位置
-encoding 指定源文件使用的编码
-sourcepath 指定查找输入源文件位置

- dx命令生成classes.dex文件
示例命令：
```
dx --dex --output classes.dex  bin/classes/  libs/
```
命令解释：将bin/classes下的class文件和libs下的jar文件编译成classes.dex文件


- aapt生成资源包文件resources.ap_

命令示例：
```
aapt package -m -J <R.java文件夹> -S <res路径> -I <android.jar路径> -A <asset路径> -M<AndroidManifest.xml路径> -F <resources.ap_文件路径>
```

- apkbuilder.bat已经过时了，使用以下方法

示例命令：
```
java -cp <sdklib.jar路径> com.android.sdklib.build.ApkBuilderMain <未签名.apk>  -v -u -z bin\resources.ap_ -f bin\classes.dex -rf src
```


- 通过jarsigner来生成
示例命令：
```
jarsigner -verbose -keystore <keystore路径> -signedjar -<输出签名apk路径> <未签名apk路径> <keystore别名>
```

-  最后一步使用zipalign工具进行apk对齐优化

示例命令：
```
zipalign [-f] [-v] <alignment> infile.apk outfile.apk
```

上面的8个步骤就是实现apk构建的过程，都是通过命令来一步一步实现，要注意每一步生成的东西。


# 小结
本篇博文主要给大家介绍了Android中apk构建的过程，也详细的讲解了每一步具体的命令操作，由于不想一篇把所有东西堆在一起，我将会在下一篇来具体使用Ant脚本实现自动化构建和多渠道打包，大家可以继续关注。



# Android自动化构建之Ant多渠道打包实践（下）

# 前言

上一篇（[Android自动化构建之Ant多渠道打包实践（上）](http://blog.csdn.net/wwj_748/article/details/50961892)）已经介绍了Android的apk是如何构建的，本篇博客继续Ant打包的实践过程。


# 集成友盟统计SDK
这里以友盟统计为例，对各个渠道进行统计，我们需要先集成它的SDK

**配置权限**
```
    <!-- 权限 -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" >
    </uses-permission>
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.INTERNET" >
    </uses-permission>
    <uses-permission android:name="android.permission.READ_PHONE_STATE" >
    </uses-permission>

```

**渠道配置**
```
 <!-- 友盟统计配置 -->
        <meta-data
            android:name="UMENG_APPKEY"
            android:value="56f0b1ef67e58eded700015b" >
        </meta-data>
        <meta-data android:name="UMENG_CHANNEL" android:value="Umeng" />
```

使用Ant打包的时候替换的渠道号就是`<meta-data android:name="UMENG_CHANNEL" android:value="Umeng" />` 将Umeng替换为具体的渠道，比如将Umeng替换为xiaomi。

# 定义build.properties文件

这个文件定义了Ant脚本要用到的一些参数值，我们的渠道也是定义在这里，具体看代码：
```

#project name and version
    project.name=AntBuild
    project.version=4.1.4

#android platform version
    android-platform=android-19

#keysore file  
    ketstore.file=release.keystore
    key.alias=release.keystore
    key.alias.password=123456
    key.store.password=123456

#publish channel
    channelname=Umeng
    channelkey=360,QQ,xiaomi,liangxiang
    key=360,QQ,xiaomi,liangxiang
    
#library project
    library-dir=../Library
    library-dir2=../Library2
# generate R.java for libraries. Separate libraries with ':'.
    extra-library-packages=

#filnal out dir
    out.dir=publish
    
```

# 完整的Ant脚本
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project name="iReaderApp" default="deploy" >
	
	<!--打包配置 -->
	<property file="build.properties" />
	
     <!-- ANT环境变量 -->
	<property environment="env" />
	
	<!-- 版本 -->
	<property name="version" value="${project.version}" />
	
	<!-- 应用名称 -->
	<property name="appName" value="${project.name}" />
	
	<!-- SDK目录(获取操作系统环境变量ANDROID_SDK_HOME的值) -->
	<property name="sdk-folder" value="${env.ANDROID_SDK_HOME}" />
	
	<!-- SDK指定平台目录 -->
	<property name="sdk-platform-folder" value="${sdk-folder}/platforms/android-19"/>
	
	<!-- SDK中tools目录 -->
	<property name="sdk-tools" value="${sdk-folder}/tools" />
	
	<!-- SDK指定平台中tools目录 -->
	<property name="sdk-platform-tools" value="${sdk-folder}/build-tools/android-4.4.2" />
	
	<!-- 使用到的命令 -->
	<property name="aapt" value="${sdk-platform-tools}/aapt" />

	  <!-- 第三方library -->
    <property name="library-dir" value="${library-dir}" />
    <property name="library-dir2" value="${library-dir2}" />
    

	<!-- 使用到的命令(当前系统为windows,如果系统为linux,可将.bat文件替换成相对应的命令) -->
	<property name="aapt" value="${sdk-platform-tools}/aapt" />
	<property name="aidl" value="${sdk-platform-tools}/aidl" />
	<property name="dx" value="${sdk-platform-tools}/dx.bat" />
	<property name="apkbuilder" value="${sdk-tools}/apkbuilder.bat" />
	<property name="jarsigner" value="${env.JAVA_HOME}/bin/jarsigner" />
	<property name="zipalign" value="${sdk-tools}/zipalign" />


	<!-- 编译需要的jar; 如果项目使用到地图服务则需要maps.jar -->
	<property name="android-jar" value="${sdk-platform-folder}/android.jar" />
	<property name="proguard-home" value="${sdk-tools}/proguard/lib" />
	
	<!-- 编译aidl文件所需的预处理框架文件framework.aidl -->
	<property name="framework-aidl" value="${sdk-platform-folder}/framework.aidl" />
	
	<!-- 清单文件 -->
	<property name="manifest-xml" value="AndroidManifest.xml" />
	
	<!-- 源文件目录 -->
	<property name="resource-dir" value="res" />
	<property name="asset-dir" value="assets" />
	
	<!-- java源文件目录 -->
	<property name="srcdir" value="src" />
	<property name="srcdir-ospath" value="${basedir}/${srcdir}" />
	
	<!-- 外部类库所在目录 -->
	<property name="external-lib" value="libs" />
	<property name="external-compile-lib" value="compile-libs" />
	
	<property name="external-lib-ospath" value="${basedir}/${external-lib}" />
	<property name="external-compile-lib-ospath" value="${basedir}/${external-compile-lib}" />
	
	
	<property name="external-library-dir-lib-ospath" value="${library-dir}/${external-lib}" />
	<property name="external-library-dir2-lib-ospath" value="${library-dir2}/${external-lib}" />
	
	
	
	<!-- 使用第三方的ant包，使ant支持for循环-->
	<taskdef resource="net/sf/antcontrib/antcontrib.properties">
		<classpath>
			<pathelement location="${external-lib-ospath}/ant-contrib-1.0b3.jar" />
		</classpath>
	</taskdef>
	
	<property name="channelname" value="${channelname}" />
	<property name="channelkey" value="${channelkey}" />
	
	<!-- 渠道名:渠道号 -->
	<!-- <property name="key" value="UMENG_CHANNEL:goapk,UMENG_CHANNEL:QQ" /> -->
	<property name="key" value="${key}" />
	
	<!--循环打包 -->
	<target name="deploy">
		<foreach target="modify_manifest" list="${key}" param="nameandchannel" delimiter=",">
		</foreach>
	</target>
		
		
	<target name="modify_manifest">
		<!-- 获取渠道名字 -->
		<!-- <propertyregex override="true" property="channelname" input="${nameandchannel}" regexp="(.*):" select="\1" /> -->
		<!-- 获取渠道号码 -->
		<propertyregex override="true" property="channelkey" input="${nameandchannel}" regexp="(.*)" select="\1" />
		<!-- 正则匹配替换渠道号(这里pattern里的内容要与mainfest文件的内容一致,包括顺序,空格) -->
		<replaceregexp flags="g" byline="false" encoding="UTF-8">
			<regexp pattern='meta-data android:name="UMENG_CHANNEL" android:value="(.*)"' />
			<substitution expression='meta-data android:name="UMENG_CHANNEL" android:value="${channelkey}"' />
			<fileset dir="" includes="AndroidManifest.xml" />
		</replaceregexp>
		<antcall target="zipalign" />
	</target>
	
	<!-- 初始化工作 -->
	<target name="init">
		<echo>目录初始化....</echo>
		
		<!-- 生成R文件的相对目录 -->
		<var name="outdir-gen" value="gen" />
		
		<!-- 编译后的文件放置目录 -->
		<var name="outdir-bin" value="${out.dir}/${channelkey}" />
		
		<!-- 生成class目录 -->
		<var name="outdir-classes" value="${outdir-bin}/otherfile" />
		<var name="outdir-classes-ospath" value="${basedir}/${outdir-classes}" />


		<!-- classes.dex相关变量 -->
		<var name="dex-file" value="classes.dex" />
		<var name="dex-path" value="${outdir-bin}/${dex-file}" />
		<var name="dex-ospath" value="${basedir}/${dex-path}" />


		<!-- 经过aapt生成的资源包文件 -->
		<var name="resources-package" value="${outdir-bin}/resources.ap_" />
		<var name="resources-package-ospath" value="${basedir}/${resources-package}" />


		<!-- 未认证apk包 -->
		<var name="out-unsigned-package" value="${outdir-bin}/${appName}-unsigned.apk" />
		<var name="out-unsigned-package-ospath" value="${basedir}/${out-unsigned-package}" />


		<!-- 证书文件 -->
		<var name="keystore-file" value="${basedir}/${ketstore.file}" />
        <!--  <span style="white-space:pre">		</span> 当前时间 -->
        <!-- <span style="white-space:pre">		</span><tstamp> -->
        <!-- <span style="white-space:pre">		</span>    <format property="nowtime" pattern="yyyyMMdd"></format>-->
        <!-- <span style="white-space:pre">		</span></tstamp> -->

		<!-- 已认证apk包 -->
		<var name="out-signed-package" value="${outdir-bin}/${appName}_${channelkey}_${version}.apk" />
		<var name="out-signed-package-ospath" value="${basedir}/${out-signed-package}" />
		<delete dir="${outdir-bin}" />
		<mkdir dir="${outdir-bin}" />
		<mkdir dir="${outdir-classes}" />
	</target>


	<!-- 根据工程中的资源文件生成R.java文件  -->
	<target name="gen-R" depends="init">
		<echo>生成R.java文件....</echo>
		<exec executable="${aapt}" failonerror="true">
			<arg value="package" />
			<arg value="-m" />
		    <arg value="--auto-add-overlay" />  
			<arg value="-J" />
			
            <!--R.java文件的生成路径-->
			<arg value="${outdir-gen}" />
			
			<!-- 指定清单文件 -->
			<arg value="-M" />
			<arg value="${manifest-xml}" />
			
            <!-- 指定资源目录 -->
			<arg value="-S" />
			<arg value="${resource-dir}" />
			
			<arg value="-S" />
            <arg value="${library-dir}/res" /><!-- 注意点:同时需要调用Library的res-->
            
          
            <arg value="-S" />
            <arg value="${library-dir2}/res" /><!-- 注意点:同时需要调用Library的res-->
			
			<!-- 导入类库 -->
			<arg value="-I" />
			<arg value="${android-jar}" />
		</exec>
	</target>


	<!-- 编译aidl文件 -->
	<target name="aidl" depends="gen-R">
		<echo>编译aidl文件....</echo>
		<apply executable="${aidl}" failonerror="true">
			<!-- 指定预处理文件 -->
			<arg value="-p${framework-aidl}" />
			<!-- aidl声明的目录 -->
			<arg value="-I${srcdir}" />
			<!-- 目标文件目录 -->
			<arg value="-o${outdir-gen}" />
			<!-- 指定哪些文件需要编译 -->
			<fileset dir="${srcdir}">
				<include name="**/*.aidl" />
			</fileset>
		</apply>
	</target>


	<!-- 将工程中的java源文件编译成class文件 -->
	<target name="compile" depends="aidl">
		<echo>java源文件编译成class文件....</echo>
		
		<!-- 库应用1编译class 生成的class文件全部保存到outdir-classes目录下-->
        <javac encoding="UTF-8" destdir="${outdir-classes}" bootclasspath="${android-jar}">
            <src path="${library-dir}/src" /><!-- 库应用源码 -->
            <src path="${outdir-gen}" /><!--  R.java 资源类的导入 -->
            <classpath>
                <fileset dir="${external-library-dir-lib-ospath}" includes="*.jar" /><!-- 第三方jar包需要引用，用于辅助编译 -->
            </classpath>
        </javac>
		
        
          <!-- 库应用2编译class -->
        <javac encoding="UTF-8" destdir="${outdir-classes}" bootclasspath="${android-jar}">
            <src path="${library-dir2}/src" /><!-- 库应用源码 -->
            <src path="${outdir-gen}" /><!--生成的class文件全部保存到bin/classes目录下 -->
            <classpath>
                <fileset dir="${external-library-dir2-lib-ospath}" includes="*.jar" /><!-- 第三方jar包需要引用，用于辅助编译 -->
            </classpath>
        </javac>
        
         <!-- 主应用编译class -->
		<javac encoding="UTF-8" destdir="${outdir-classes}" bootclasspath="${android-jar}" >
			<compilerarg line="-encoding UTF-8 " />
			<!-- <compilerarg    line="-encoding UTF-8 "/> -->
			  <src path="${basedir}/src" /><!-- 工程源码-->
              <src path="${outdir-gen}" /><!--R.java 资源类的导入 -->
             
             <!-- 编译java文件依赖jar包的位置 -->
			<classpath>
				<fileset dir="${external-lib}" includes="*.jar" /><!-- 第三方jar包需要引用，用于辅助编译 -->
<!-- 				<fileset dir="${external-compile-lib}" includes="*.jar"/>第三方jar包需要引用，用于辅助编译
 -->				<fileset dir="${external-library-dir-lib-ospath}" includes="*.jar" /><!-- 第三方jar包需要引用，用于辅助编译 -->
			</classpath>
		</javac>
	</target>

	<!--执行代码混淆-->
	


	<!-- 将.class文件转化成.dex文件 -->
	<target name="dex" depends="compile"  unless="do.not.compile">
		<echo>将.class文件转化成.dex文件....</echo>
		<exec executable="${dx}" failonerror="true">
			<arg value="--dex" />
			
			<!-- 输出文件 -->
			<arg value="--output=${dex-ospath}" />
			
			<!-- 要生成.dex文件的源classes和libraries -->
			<arg value="${outdir-classes-ospath}" />		
			<arg value="${external-lib-ospath}" />
		<!-- 	<arg value="${external-library-dir-lib-ospath}" />
			<arg value="${external-library-dir2-lib-ospath}" /> -->
		</exec>
	</target> 
	
	
	<!-- 将资源文件放进输出目录 -->
	<target name="package-res-and-assets">
		<echo>将资源文件放进输出目录....</echo>
		<exec executable="${aapt}" failonerror="true">
			<arg value="package" />
			<arg value="-f" />
			<arg value="-M" />
			<arg value="${manifest-xml}" />
			
			<arg value="-S" />
			<arg value="${resource-dir}" />
			
			<arg value="-S"/>
            <arg value="${library-dir}/res"/>
            
            <arg value="-S"/>
            <arg value="${library-dir2}/res"/>
            
			<arg value="-A" />
			<arg value="${asset-dir}" />
			<arg value="-I" />
			<arg value="${android-jar}" />
			<arg value="-F" />
			<arg value="${resources-package}" />
			<arg value="--auto-add-overlay" />
		</exec>
	</target>
	
	<!-- 打包成未签证的apk -->
	<target name="package" depends="dex,package-res-and-assets">
		<echo>打包成未签证的apk....</echo>
		<java classpath="${sdk-tools}/lib/sdklib.jar" classname="com.android.sdklib.build.ApkBuilderMain">  
		    
		    <!-- 输出路径 -->
			<arg value="${out-unsigned-package-ospath}" />
			<arg value="-u" />
			<arg value="-z" />
			
			<!-- 资源压缩包 -->
			<arg value="${resources-package-ospath}" />
			<arg value="-f" />
			
			<!-- dex压缩文件 -->
			<arg value="${dex-ospath}" />
			
			<arg value="-rj" />
			<arg value="${external-lib-ospath}"/>
			
			<!-- 将主项目libs下面的so库打包 -->
			<arg value="-nf" />
			<arg value="${external-lib-ospath}" />
		</java>
	</target>


	<!-- 对apk进行签证 -->
	<target name="jarsigner" depends="package">
		<echo>Packaging signed apk for release...</echo>
		<exec executable="${jarsigner}" failonerror="true">
			<arg value="-keystore" />
			<arg value="${keystore-file}" />
			<arg value="-storepass" />
			<arg value="${key.store.password}" />
			<arg value="-keypass" />
			<arg value="${key.alias.password}" />
			<arg value="-signedjar" />
			<arg value="${out-signed-package-ospath}" />
			<arg value="${out-unsigned-package-ospath}" />
			<!-- 不要忘了证书的别名 -->
			<arg value="${key.alias}" />
		</exec>
	</target>


	<!-- 发布 -->
	<target name="release" depends="jarsigner">
		<!-- 删除未签证apk -->
		<delete file="${out-unsigned-package-ospath}" />
		<echo>APK is released. path:${out-signed-package-ospath}</echo>
		 <echo>删除其他文件，最后只保留apk</echo>  
		<delete dir="${outdir-classes}"/>
		<delete file="${dex-ospath}" />
		<delete file="${resources-package-ospath}" />
		<echo>生成apk完成</echo>
	</target>
	
	<!-- 打包的应用程序进行优化 -->
	<target name="zipalign" depends="release">
		<exec executable="${zipalign}" failonerror="true">
			<arg value="-v" />
			<arg value="4" />
			<arg value="${out-signed-package-ospath}" />
			<arg value="${out-signed-package-ospath}-zipaligned.apk" />
		</exec>
	</target>
	 
</project>
```

上面就是完整的Ant脚本，实现了自动化构建和多渠道的打包，笔者在实践的过程踩过不少坑才最终把apk包成功打出。

这里总结下可能遇到的坑：
- 生成R.java文件，一定要注意先后顺序，主项目之后才到关联项目
- 编译生成class文件，可能会遇到找不到类，一定要按照添加库的顺序来编译class文件
- 替换渠道号的时候，Ant中pattern里的内容要与mainfest文件的内容一致,包括顺序,空格)，笔者试过格式化后代码之后就不能写入成功



# build.bat脚本
```

@echo off
call ant -buildfile "build.xml" deploy
echo done
pause
exit
```

# 测试结果

我们可以在项目中的publish目录下生成不同渠道的apk文件：

![这里写图片描述](http://upload-images.jianshu.io/upload_images/224008-fc446300eea94a0f?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

安装apk到设备，启动之后在友盟后台集成测试，看app发布的渠道：
![历史纪录](http://upload-images.jianshu.io/upload_images/224008-0c02c17bb0934c46?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# Demo例子欢迎大家star

https://github.com/devilWwj/Android-Tech/tree/master/AntBuildTest



# 总结
实现Ant多渠道打包整个过程还是比较繁琐的，主要在Ant脚本上，比较容易出错，需要对命令比较了解，但确实能够缩短我们打渠道包的时间，基于本次实践是基于Eclipse，目前Android Studio使用gradle来实现多渠道打包，以后会把gradle进行多渠道打包的实现分享给大家，大家可以对比下这两种打包方式的区别，主要目的是更加深入的了解apk的构建过程。

----------
欢迎关注我的公众号：wwjblog

![ wwjblog](http://upload-images.jianshu.io/upload_images/224008-64a02b56a0ac46d8?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


