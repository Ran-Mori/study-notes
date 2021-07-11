# Android开发探索艺术

## 第一章

> ### onPause()和onResume()执行顺序
>
> * `ActivityA.onPause()`先执行，`ActivityB.onResume()`后执行
> * 这是源码决定的
> * 因此`onPause()`中不能执行重量操作，因为这样会影响到下一个`Activity`的启动速度
>
> ### 异常情况
>
> * 资源相关的配置文件发生改变
> * 系统内存不足
>
> ### 资源配置改变
>
> * 横竖屏切换有可能加载不同的 **图片** 资源文件
>
> ### onSaveInstanceState()
>
> * 异常情况销毁才会调用
> * 正常退出是不会调用的
>
> ### View数据保存和恢复
>
> * 异常退出系统会自动保存和恢复一定的View数据
> * 如文本框用户输入的数据、LIstView滚动位置
> * 保存那些数据取决于每个View的`onSaveInstane()、onRestoreInstanceState()`方法的实现
> * 保存的逻辑是从`Activity`一步步委托
>
> ### 恢复位置
>
> * 一般都放在`onCreate()`中判断不为null进行恢复
> * 但有时候恢复放在`onRestoreInstanceState()`更方便
>
> ### 系统资源不足被异常退出
>
> * 如果一个进程中没有任何四大组件运行，那么这个进程很容易被杀死
> * 因此后台的耗时任务不能直接挂在后台，最好绑定一个`Service`，这样才不会被杀死
>
> ***

