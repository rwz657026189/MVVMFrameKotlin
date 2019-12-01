package com.rwz.lib_comm.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Created by rwz on 2017/7/19.
 * 可禁止滑动的ViewPager
 */

class CommonViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    var isCanScroll = true//是否可以滑动

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        try {
            return isCanScroll && super.onTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            return isCanScroll && super.onInterceptTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }

}