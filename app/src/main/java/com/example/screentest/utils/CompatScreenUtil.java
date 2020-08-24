package com.example.screentest.utils;

import android.app.Application;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *  @author cy
 *  基本概念：
 *  屏幕尺寸：我们说的5英寸屏幕就是斜对角长度为5英寸
 *  dpi：屏幕斜对角每英寸有多少像素
 *  所以斜对角总共有多少像素计算方法可以利用勾股定理来计算，也就是屏幕宽像素的平方与高像素的平方之和进行开根
 *  因此dpi等于斜对角总共有多少像素除以屏幕尺寸
 *  density计算方法就是dpi/160，160是谷歌规定的一个标准
 *  dp的计算方法就是px/density
 *
 *  我们在AS设计界面选择预览设备时，后面标记的dpi跟我们实际计算出来的有些许差距，实际以AS中的为准，上面的计算方法
 *  适用于我们自定义预览设备的情况
 *
 *  根据物理屏幕属性，将px转换成dp的方法
 *  px/(sqrt(宽*宽 + 高*高)/屏幕物理尺寸/160)
 *  注：sqrt是开根号
 *  例如：宽1080px，高1920px，尺寸为5英寸的屏幕
 *  那么对应的宽的dp是 1080/(sqrt(1080* 1080 + 1920*1920)/5/160) = 392.72727272...
 *  sqrt(1080* 1080 + 1920*1920)/5 约等于 440，也就是dpi大约为440
 *  density = 440/160 = 2.75
 *  对应的高的dp是 1920/2.75 = 698.181818...
 */
public class CompatScreenUtil {

    private static List<Field> sMetricsFields;
    private static Application application;

    public static void init(Application application) {
        CompatScreenUtil.application = application;
    }

    /**
     *  以宽度这一维度适配
     * @param res 未修改过的resources
     * @param designWidth 设计图上的宽度，如果适配单位为dp，此宽度也需要转换成dp，否则传入px单位宽度即可
     * @param unit 要适配的单位，如果以dp适配也会适配sp
     * @return 适配后的resources
     */
    public static Resources compatWidth(final Resources res, final int designWidth, final Unit unit) {
        convertToUnitHandler(res, res.getDisplayMetrics().widthPixels, designWidth, unit, false);
        return res;
    }

    /**
     *  以高度这一维度适配
     * @param res 未修改过的resources
     * @param designHeight 设计图上的高度，如果适配单位为dp，此高度也需要转换成dp，否则传入px单位高度即可
     * @param unit 要适配的单位，如果以dp适配也会适配sp
     * @return 适配后的resources
     */
    public static Resources compatHeight(final Resources res, final int designHeight, final Unit unit) {
        return compatHeight(res, designHeight, unit,false);
    }

    /**
     *  以高度这一维度适配
     * @param res 未修改过的resources
     * @param designHeight 设计图上的高度，如果适配单位为dp，此高度也需要转换成dp，否则传入px单位高度即可
     * @param unit 要适配的单位，如果以dp适配也会适配sp
     * @param includeNavBar 传入的高度的值是否包含了底部导航栏，如果包含了，那么将加上导航栏高度再进行适配
     * @return 适配后的resources
     */
    public static Resources compatHeight(final Resources res, final int designHeight, final Unit unit, boolean includeNavBar) {
        // 如果包含了导航栏，那么获取的heightPixels是减去了导航栏高度的，所以要得到真实设备高度就需要加上导航栏高度
        final int height = res.getDisplayMetrics().heightPixels + (includeNavBar ? getNavBarHeight(res) : 0);
        convertToUnitHandler(res, height, designHeight, unit, false);
        return res;
    }

    /**
     * @param resources The resources.
     * @return the resource
     */
    public static Resources closeCompat(final Resources resources) {
        convertToUnitHandler(resources, 0, 0, null, true);
        return resources;
    }


    // -------------------------------------- 私有封装，工具方法 --------------------------------------
    /**
     *  屏幕适配核心方法，后续添加适配单位只需要修改这里
     * @param resources 未修改过的resources
     * @param origin 原始宽或高，适配dp的话需要转换成对应的dp的值
     * @param design 设计图上相同维度的像素
     * @param unit 要适配的单位
     */
    private static void convertToUnitHandler(final Resources resources, final int origin,
                                          final int design, final Unit unit, boolean isClose) {
        if (isClose) {
            applyDisplayMetrics(resources,
                    new UnitHandler.DP(0, 0, true),
                    new UnitHandler.PT(0, 0, true));
            return;
        }
        switch (unit) {
            case DP:
                applyDisplayMetrics(resources, new UnitHandler.DP(origin, design, false));
                break;
            case PT:
                applyDisplayMetrics(resources, new UnitHandler.PT(origin, design, false));
                break;
        }
    }

    private static void applyDisplayMetrics(final Resources resources, final UnitHandler... handlers) {
        for (UnitHandler handler : handlers) {
            handler.apply(resources.getDisplayMetrics());
            handler.apply(application.getResources().getDisplayMetrics());
        }
        applyOtherDisplayMetrics(resources, handlers);
    }


    // 返回导航栏高度，单位px
    public static int getNavBarHeight(final Resources resources) {
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return resources.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    private static void applyOtherDisplayMetrics(final Resources resources, UnitHandler... handlers) {
        if (sMetricsFields == null) {
            sMetricsFields = new ArrayList<>();
            Class<?> resCls = resources.getClass();
            Field[] declaredFields = resCls.getDeclaredFields();
            while (declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.getType().isAssignableFrom(DisplayMetrics.class)) {
                        field.setAccessible(true);
                        DisplayMetrics tmpDm = getMetricsFromField(resources, field);
                        if (tmpDm != null) {
                            sMetricsFields.add(field);
                            for (UnitHandler handler : handlers) {
                                handler.apply(tmpDm);
                            }
                        }
                    }
                }
                resCls = resCls.getSuperclass();
                if (resCls != null && resCls != Object.class) {
                    declaredFields = resCls.getDeclaredFields();
                } else {
                    break;
                }
            }
        } else {
            applyMetricsFields(resources, handlers);
        }
    }

    private static void applyMetricsFields(final Resources resources, UnitHandler... handlers) {
        for (Field metricsField : sMetricsFields) {
            try {
                DisplayMetrics dm = (DisplayMetrics) metricsField.get(resources);
                if (dm != null) {
                    for (UnitHandler handler : handlers) {
                        handler.apply(dm);
                    }
                }
            } catch (Exception e) {
                Log.e("CompatScreenUtil", "applyMetricsFields: " + e);
            }
        }
    }

    private static DisplayMetrics getMetricsFromField(final Resources resources, final Field field) {
        try {
            return (DisplayMetrics) field.get(resources);
        } catch (Exception ignore) {
            return null;
        }
    }

    // 暂未发现有啥用
    /*private static Runnable getPreLoadRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                preLoad();
            }
        };
    }

    private static void preLoad() {
        applyDisplayMetrics(Resources.getSystem(), Resources.getSystem().getDisplayMetrics().xdpi);
    }*/
}
