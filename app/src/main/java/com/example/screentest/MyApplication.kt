package com.example.screentest

import android.app.Application
import com.example.screentest.utils.CompatScreenUtil

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CompatScreenUtil.init(this)
    }
}