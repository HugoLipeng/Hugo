# Activity����ģʽ

����Activity��LaunchMode���һ��֪�����������֣�
- standard(��׼ģʽ)
- singleTop(ջ������ģʽ)
- singleTask(ջ�ڸ���ģʽ)
- singleInsance(��ʵ��ģʽ)

������Ƿ����������������ģʽ��������ʲô�ط��������Ƿֱ��Ӧ�ó�����ʲô��

## standard
���ģʽ���ǲ�����ʾָ������Ϊ����ActivityĬ�ϵ�����ģʽ���������´��������standardģʽ�����ģ�

```
 Intent intent = new Intent(this, SecondActivty.class);
 startActvitiy(intent);
 
```

����֪��Activity��ͨ������ջ������ģ�ʹ��standardģʽ���������ص㣺
?
?- ÿ����һ��Activity�������´���һ��ʵ����Ȼ��ᱻѹ��ջ����
?- �󴴽���Activity���ȳ�ջ��һ�����Ϊ��back�����ͻ���һ��Activity��ջ
?
һͼʤǧ�ԣ�
![](https://mmbiz.qlogo.cn/mmbiz/VANkRlogRe05DibmMgG5P4d6ias4OFdI69rwnaWJUdUZF6jNYw9pPibCyGKLLa05E3OGrVicoXLnD4ibMHf026MZlicw/0?wx_fmt=png)

## singleTop
ջ������ģʽ������˼�壺���������ջ�е�ջ���Ѿ����ڸ�Activity���ٴ�����Activity�򲻻����´���ʵ������ֱ�Ӹ���ջ����Activity��

һͼʤǧ�ԣ�

![](https://mmbiz.qlogo.cn/mmbiz/VANkRlogRe05DibmMgG5P4d6ias4OFdI69uukdGR55Ja6FZ9VdRYiaZr9WEsWGTghVMMLNgsQSJibicGIqAkAAquE5A/0?wx_fmt=png)����һ�㣬�������ջ����Activity�������Activity��onNewIntent�����ᱻ�ص���onCreate������onStart�������ᱻ�ص���

## singleTask
ջ�ڸ���ģʽ
���Ǹ���ʵ��ģʽ����������Activityһ���Ĭ�ϴ���һ��ջ�������ڰ�����ͬ����������ǵ�Ĭ��ջ��ͨ��standard������Activity����������ջ�ڡ����ʹ��singleTask������ָ��Activity��Ҫ��ջ������ͨ��ָ��taskAffinity������ָ������������Բ��ܸ�������ͬ��ʾ�����£�

```
? <activity android:name=".SecondActivity" android:launchMode="singleTask"
? ? ? ? ? ? android:taskAffinity="com.devilwwj.task"

? ? ? ? ? ? />
```

�ٿ�һ��ͼ��͸�����ˣ�
![](https://mmbiz.qlogo.cn/mmbiz/VANkRlogRe05DibmMgG5P4d6ias4OFdI69Uw5mqKZ54s9yuKGj8ZBicLcplH0MIv3BC8iaB0v56mRJ5K2aZJY8SRsQ/0?wx_fmt=png)?

## singleInstance
��ʵ��ģʽ
�������ģʽ��singleTask�е����ƣ�������֮��������ǣ�singleInstanceָ����ջֻ�ܴ��һ��Activity�����Activity��ȫ��Ψһ�ġ�

# �ܽ�
ͨ������Ķ�������ģʽ�Ľ��⣬���Ŵ���Ѿ���Activity��ջ��������һ������ʶ��������ʵ�ʿ��������У��Ϳ���ͨ��ʹ������ģʽ��������������ĳ�������������ͨ��֪ͨ������һ��Activity���Ϳ���ָ��ΪsingleTask���������Ժ����Թ��ʵ�����Activity������ģʽ��������Ҳ��������

![](https://mmbiz.qlogo.cn/mmbiz/VANkRlogRe05DibmMgG5P4d6ias4OFdI69s1ibb1rszAIx1tNK2rID3ehkqrN0RL25X3vgzlueQq0ALUUiaPIjLzlw/0?wx_fmt=png)