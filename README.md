# Rabbit Log
[![license](https://img.shields.io/badge/license-Apache%202.0-brightgreen.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0) [![Download](https://api.bintray.com/packages/andy-home/maven/Rabbit-Log/images/download.svg) ](https://bintray.com/andy-home/maven/Rabbit-Log/_latestVersion)



**功能**
1. 在Android设备屏幕上全局显示日志内容
2. 文件保存使用LogManage.setText(String msg)方法显示的日志内容
3. 文件保存系统日志，能够按照级别保存对应内容
4. 程序崩溃时保存crash日志，生成文件
---
**使用范例**

在*自定义 Application* 中初始化 LogManage
```Java
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogManager.getInstance()
                .init(getApplicationContext())
                .setLine(10)                    //设置在屏幕上显示日志的行数
                .setCache(10 * 1024 * 1024)     //设置缓存的内存
                .enabledCrashHandler();         //启用自定义异常处理器
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogManager.getInstance()
                .stop()
                .finish();          //释放内存
    }
}
```
* 获取 *LogManager* 实例
```Java
LogManager mLogManager = LogManager.getInstance();
```
根据获取的实例引用 *LogManager* 类提供的功能
* 开启日志内容显示界面
```Java
 mLogManager.start();
```
* 关闭日志内容显示界面
```Java
mLogManager.stop();
```
* 显示对应日志内容
```Java
mLogManager.setText("显示日志内容");
```
* 保存日志内容
```Java
mLogManager.saveLog();
```
* 保存系统日志内容
```Java
mLogManager.saveLogCatInfo(LogCat.VERBOSE);
```
关于 *LogCat* 显示的日志级别包括：VERBOSE、DEBUG、INFO、WARN、ERROR、ASSERT

**注意:** 保存的日志内容的目录路径为:
```
/Android/data/应用包名/files/Document/
```
或者时
```
/Android/data/应用包名/files/Downloads/
```
**提醒**

如果看完该文档后还有问题，可以运行以下程序，了解代码，如果有什么问题，欢迎大家指正
