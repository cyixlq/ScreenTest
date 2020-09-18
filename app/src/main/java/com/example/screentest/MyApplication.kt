package com.example.screentest

import android.app.Application
import top.cyixlq.compat_screen.CompatScreenUtil

class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        CompatScreenUtil.init(this)
        instance = this
    }
}