# Rabbit Log
**功能**
1. 在Android设备屏幕上全局显示日志内容
2. 文件保存使用LogManage.setText(String msg)方法显示的日志内容
3. 文件保存系统日志，能够按照级别保存对应内容
---
**使用范例**

在*自定义 Application* 中初始化 LogManage
```Java
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogManager.getInstance(this.getApplicationContext())
                .setLine(10)
                .setTextColor(R.color.logText);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogManager.getInstance(this.getApplicationContext())
                .stop()
                .finish();
    }
}
```
初始化后，可以通过使用 *LogManager* 提供的静态方法使用相应功能：
* 开启日志内容显示界面
```Java
 LogManager.getInstance(this).start();
```
* 关闭日志内容显示界面
```Java
LogManager.getInstance(this).stop();
```
* 显示对应日志内容
```Java
LogManager.setText("显示日志内容");
```
* 保存日志内容
```Java
LogManager.saveLog();
```
* 保存系统日志内容
```Java
LogManager.saveLogCatInfo(LogCat.VERBOSE);
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
