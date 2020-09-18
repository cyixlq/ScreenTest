## CompatScreenUtil屏幕适配工具，基于Blankj的库修改而来
### About
基于Blankj的AdaptScreenUtils改造，增加dp适配功能。
可以选择针对pt适配还是dp适配。
原库地址：https://github.com/Blankj/AndroidUtilCode/blob/1079a0392d/lib/utilcode/src/main/java/com/blankj/utilcode/util/AdaptScreenUtils.java
### 使用方法
0. 将compat_screen作为library导入到主工程的依赖中
1. 使用方法
1.1按照宽度这一维度适配，在Activity中重写getResources方法，如下所示
```
@Override
public Resources getResources() {
    // 此处的411是将设计图上的宽度转换成DP后的结果，单位dp->411dp
    return CompatScreenUtil.compatWidth(super.getResources(), 411, Unit.DP);
}
```
或者
```
@Override
public Resources getResources() {
    // 此处的1080是将设计图上的宽度转换成PT后的结果，单位pt->1080pt
    return CompatScreenUtil.compatWidth(super.getResources(), 1080, Unit.PT);
}
```
现在蓝湖上基本都可以切换单位显示，直接按照上面转换后的单位填，只要不是设计师标注错误，一般都没问题。

1.2如果需要以高度这一维度适配那么请使用CompatScreenUtil.compatHeight(super.getResources(), 411, Unit.DP);

2. 关闭某个页面的适配
2.1全部适配单位都关闭：
```
@Override
public Resources getResources() {
    // 此处的1080是将设计图上的宽度转换成PT后的结果，单位pt->1080pt
    return CompatScreenUtil.closeCompat();
}
```
2.2关闭某一单位的适配，例如关闭PT适配:
```
@Override
public Resources getResources() {
    return CompatScreenUtil.closeCompat(super.getResources(), Unit.PT);
}
```