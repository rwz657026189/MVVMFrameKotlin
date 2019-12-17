package com.rwz.lib_comm.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.bigkoo.convenientbanner.ConvenientBanner

/**
 * date： 2019/11/30 16:33
 * author： rwz
 * description：
 **/

class CommonBanner @JvmOverloads constructor(context: Context,
                                             attrs: AttributeSet? = null) :
    ConvenientBanner<Any>(context, attrs) {

    override fun onAttachedToWindow() {
        startTurning(AUTO_TURNING_TIME.toLong())
        isCanLoop = true
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        stopTurning()
        super.onDetachedFromWindow()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        if (visibility != View.VISIBLE) {
            stopTurning()
        }
        super.onVisibilityChanged(changedView, visibility)
    }

    companion object {
        //开始轮询间隔
        private const val AUTO_TURNING_TIME = 3000
    }
}
