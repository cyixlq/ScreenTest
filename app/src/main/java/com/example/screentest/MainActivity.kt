package com.example.screentest

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import com.example.screentest.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import top.cyixlq.compat_screen.CompatScreenUtil

class MainActivity : BaseCompatScreenActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val widthPx = resources.displayMetrics.widthPixels
        val heightPx = resources.displayMetrics.heightPixels
        val navBarHeight = CompatScreenUtil.getNavBarHeight(resources)
        val info = """屏幕宽度：${widthPx}px；屏幕高度：${heightPx + navBarHeight}px;
            |屏幕宽：${px2dp(widthPx)}dp；屏幕高：${px2dp(heightPx + navBarHeight)}dp。
        """.trimMargin()
        screenInfo.text = info
        screenInfo.setOnClickListener {
            startActivity(Intent(this, TestTextSizeActivity::class.java))
        }
        val msg = """
            ${resources.displayMetrics.density} : ${resources.displayMetrics.scaledDensity}
            ${Resources.getSystem().displayMetrics.density} : ${Resources.getSystem().displayMetrics.scaledDensity}
        """.trimIndent()
        ToastUtils.toastShort(msg)
    }

    private fun px2dp(px: Int): Int {
        return (px / resources.displayMetrics.density).toInt()
    }
}