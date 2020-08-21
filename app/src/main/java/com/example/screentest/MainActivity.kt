package com.example.screentest

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.screentest.utils.CompatScreenUtil
import com.example.screentest.utils.Unit
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val widthPx = resources.displayMetrics.widthPixels
        val heightPx = resources.displayMetrics.heightPixels
        val info = """屏幕宽度：${widthPx}px；屏幕高度：${heightPx}px;
            |屏幕宽：${px2dp(widthPx)}dp；屏幕高：${px2dp(heightPx)}dp。
        """.trimMargin()
        screenInfo.text = info
    }

    private fun px2dp(px: Int): Int {
        return (px / resources.displayMetrics.density).toInt()
    }

    override fun getResources(): Resources {
        return CompatScreenUtil.compatWidth(super.getResources(), 411, Unit.DP)
    }
}