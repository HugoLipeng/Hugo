# Activity启动模式

关于Activity的LaunchMode大家一定知道有以下四种：
- standard(标准模式)
- singleTop(栈顶复用模式)
- singleTask(栈内复用模式)
- singleInsance(单实例模式)

但大家是否理解了这四种启动模式的区别在什么地方还有它们分别的应用场景是什么？

## standard
这个模式我们不用显示指定，因为它是Activity默认的启动模式，例如以下代码就是以standard模式启动的：

```
 Intent intent = new Intent(this, SecondActivty.class);
 startActvitiy(intent);
 
```

我们知道Activity是通过任务栈来管理的，使用standard模式会有以下特点：
?
?- 每启动一个Activity都会重新创建一个实例，然后会被压入栈里面
?- 后创建的Activity会先出栈，一般表现为按back键，就会有一个Activity出栈
?
一图胜千言：
![](https://mmbiz.qlogo.cn/mmbiz/VANkRlogRe05DibmMgG5P4d6ias4OFdI69rwnaWJUdUZF6jNYw9pPibCyGKLLa05E3OGrVicoXLnD4ibMHf026MZlicw/0?wx_fmt=png)

## singleTop
栈顶复用模式，顾名思义：如果在任务栈中的栈顶已经存在该Activity，再次启动Activity则不会重新创建实例，会直接复用栈顶的Activity。

一图胜千言：

![](https://mmbiz.qlogo.cn/mmbiz/VANkRlogRe05DibmMgG5P4d6ias4OFdI69uukdGR55Ja6FZ9VdRYiaZr9WEsWGTghVMMLNgsQSJibicGIqAkAAquE5A/0?wx_fmt=png)还有一点，如果复用栈顶的Activity，则这个Activity的onNewIntent方法会被回调，onCreate方法和onStart方法不会被回调。

## singleTask
栈内复用模式
这是个单实例模式，我们启动Activity一般会默认创建一个栈，属性于包名相同，这个是我们的默认栈，通过standard启动的Activity都会放入这个栈内。如果使用singleTask，可以指定Activity需要的栈，可以通过指定taskAffinity属性来指定，但这个属性不能跟包名相同，示例如下：

```
? <activity android:name=".SecondActivity" android:launchMode="singleTask"
? ? ? ? ? ? android:taskAffinity="com.devilwwj.task"

? ? ? ? ? ? />
```

再看一张图你就更清楚了：
![](https://mmbiz.qlogo.cn/mmbiz/VANkRlogRe05DibmMgG5P4d6ias4OFdI69Uw5mqKZ54s9yuKGj8ZBicLcplH0MIv3BC8iaB0v56mRJ5K2aZJY8SRsQ/0?wx_fmt=png)?

## singleInstance
单实例模式
这个启动模式跟singleTask有点类似，但它们之间的区别是，singleInstance指定的栈只能存放一个Activity，这个Activity是全局唯一的。

# 总结
通过上面的对启动的模式的讲解，相信大家已经对Activity的栈管理有了一定的认识，我们在实际开发过程中，就可以通过使用启动模式来满足我们特殊的场景，比如我们通过通知栏启动一个Activity，就可以指定为singleTask来启动。以后面试官问道关于Activity的启动模式，宝宝再也不怕啦。

![](https://mmbiz.qlogo.cn/mmbiz/VANkRlogRe05DibmMgG5P4d6ias4OFdI69s1ibb1rszAIx1tNK2rID3ehkqrN0RL25X3vgzlueQq0ALUUiaPIjLzlw/0?wx_fmt=png)