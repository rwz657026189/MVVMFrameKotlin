package com.rwz.lib_comm.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager
import com.flyco.tablayout.SlidingTabLayout

/**
 * date： 2019/11/30 16:32
 * author： rwz
 * description：https://github.com/H07000223/FlycoTabLayout/blob/master/README_CN.md
 **/
class CommTabLayout(context: Context?, attrs: AttributeSet?) :
    SlidingTabLayout(context, attrs) {

    fun setupWithViewPager(viewPager: ViewPager) {
        setViewPager(viewPager)
    }

    fun setCanScroll(canScroll: Boolean) {
        //setTabMode(tabs.size() <= 5 ? TabLayout.MODE_FIXED : TabLayout.MODE_SCROLLABLE);

    }
}