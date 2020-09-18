package com.example.screentest.utils;

import android.widget.Toast;

import com.example.screentest.MyApplication;

public class ToastUtils {
    public static void toastShort(final String msg) {
        Toast.makeText(MyApplication.Companion.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
}
