package com.example.screentest;

import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import top.cyixlq.compat_screen.CompatScreenUtil;
import top.cyixlq.compat_screen.Unit;

public abstract class BaseCompatScreenActivity extends AppCompatActivity {
    @Override
    public Resources getResources() {
        return CompatScreenUtil.compatWidth(super.getResources(), 411, Unit.DP);
    }
}
