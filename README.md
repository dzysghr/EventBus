# EventBus
一个eventBus的简单实现， 灵感来自https://github.com/greenrobot/EventBus

# 如何使用

## 编写事件类
```
public class BaseEvent
{
    public String msg;
    public BaseEvent(String m)
    {
        msg = m;
    }
}
```

## 获得EventBus实例

```
EventBus.getInstant()
```
> 注：通过此方法获取一个默认全局的实例，但也可以自己new一个

## 订阅者编写响应方法
### 方法一：使用方法命名说明响应线程
```
public class testobject
{
    //在主线程调用
    public void onMainEvent(BaseEvent msg)
    {
        Log.i("tag",msg);
    }
    
    //在后台线程调用
    public void oBackgroundEvent(BaseEvent msg)
    {
        Log.i("tag",msg);
    }

    //在事件发布线程调用
    public void onPostEvent(BaseEvent msg)
    {
        Log.i("tag",msg);
    }
}
```
>注：根据线程情况需求编写方法，响应方法名一定要严格一致，方法参数只能有一个，***参数类型即为事件的类型***

### 方法二:使用注解标注（注解会影响事件注册的速度）
```
    @Observers(ThreadMode.MainThread)
    public void onMainAnnotation(String msg)
    {
        Log.i("tag",msg);
    }

    @Observers(ThreadMode.BackgroudThread)
    public void onBackgroudAnnotation(BaseEvent msg)
    {
        Log.i("tag",msg);
    }

    @Observers(ThreadMode.PostThread)
    public void onPostAnnotation(BaseEvent msg)
    {
        Log.i("tag",msg);
    }
```

## 注册事件
```
//方法名注册
EventBus.getInstant().registerOnEvent(object observer);
EventBus.getInstant().registerOnEvent(object observer,int priority);

//注解注册
EventBus.getInstant().registerByAnnotation(object observer);
EventBus.getInstant().registerByAnnotation(object observer,int priority);
```

## 发布事件
```
EventBus.getInstant().post(new BaseEvent("this is a new event"));
```
>支持事件继承

## 注销注册
```
EventBus.getInstant().unRegister(object observer);
```
