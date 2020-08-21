package com.example.screentest.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public abstract class UnitHandler {

    int origin, design;
    boolean isClose; // 是否关闭适配

    public UnitHandler(int origin, int design, boolean isClose) {
        this.origin = origin;
        this.design = design;
        this.isClose = isClose;
    }

    abstract void apply(DisplayMetrics dm);

    static class PT extends UnitHandler {

        public PT(int origin, int design, boolean isClose) {
            super(origin, design, isClose);
        }

        @Override
        public void apply(DisplayMetrics dm) {
            if (isClose)
                dm.xdpi = Resources.getSystem().getDisplayMetrics().xdpi;
            else
                dm.xdpi = (float) origin / design / 72;
        }
    }

    static class DP extends UnitHandler {

        public DP(int origin, int design, boolean isClose) {
            super(origin, design, isClose);
        }

        @Override
        public void apply(DisplayMetrics dm) {
            if (isClose)
                dm.density = Resources.getSystem().getDisplayMetrics().density;
            else
                dm.density = (float) origin / design;
        }
    }
}
