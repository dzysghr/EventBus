# EventBus
一个eventBus的简单实现， 灵感来自https://github.com/greenrobot/EventBus

# 如何使用

## 获得EventBus实例

```
EventBus.getInstant()
```
> 注：通过此方法获取一个默认全局的实例，但也可以自己new一个

## 订阅者编写响应方法
```
public class testobject
{
    //在主线程调用
    public void onMainEvent(String msg)
    {
        Log.i("tag",msg);
    }
    
    //在后台线程调用
    public void oBackgroundEvent(String msg)
    {
        Log.i("tag",msg);
    }

    //在事件发布线程调用
    public void onPostEvent(String msg)
    {
        Log.i("tag",msg);
    }
}

```
>注：根据线程情况需求编写方法，响应方法名一定要严格一致，方法参数只能有一个，***参数类型即为事件的类型，不支持参数协变***

## 注册事件
```
EventBus.getInstant().registerOnEvent(object observer);
EventBus.getInstant().registerOnEvent(object observer,int priority);
```

## 发布事件
```
EventBus.getInstant().post("this is a msg");
```
## 注销注册
```
EventBus.getInstant().unRegister(object observer);
```
