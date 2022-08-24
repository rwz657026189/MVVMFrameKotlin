package com.rwz.mvvm_kotlin_demo.ui.activity

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.abs.IViewModule
import com.rwz.lib_comm.base.BaseActivity
import com.rwz.lib_comm.config.EXIT_APP_DOUBLE_CLICK_TIME
import com.rwz.lib_comm.databinding.ActivityContainerBinding
import com.rwz.lib_comm.utils.app.CheckHelp
import com.rwz.lib_comm.utils.app.DensityUtils
import com.rwz.mvvm_kotlin_demo.R


/**
 * date： 2020/6/27 15:54
 * author： rwz
 * description：
 **/
class MainActivity : BaseActivity<ActivityContainerBinding, IViewModule<IView>>() {

    override fun setViewModule(): IViewModule<IView>?  = null

    override fun setLayoutId(): Int = R.layout.activity_container

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .add(R.id.container, MainFragment(), "MainFragment")
            .commit()
        (findViewById<TextView>(R.id.one)).text = System.currentTimeMillis().toString()
        (findViewById<TextView>(R.id.two)).text = System.currentTimeMillis().toString()
        startAnim(findViewById(R.id.one), findViewById(R.id.two))
        Log.d(
            "TAG",
            "init: ${(findViewById<View>(R.id.one).layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin}"
        )
    }

    private fun startAnim(oneView: TextView, twoView: TextView) {
        val height = DensityUtils.dp2px(64)
        var isReset = false
        ValueAnimator.ofFloat(0f, -height.toFloat()).apply {
            addUpdateListener {
                val value = (it.animatedValue as? Float)?.toInt() ?: 0
                if (isReset) {
                    (twoView.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = value
                    (oneView.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = value + height
                } else {
                    (oneView.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = value
                    (twoView.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = value + height
                }
                oneView.requestLayout()
                twoView.requestLayout()
            }
            addListener(object: Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    Log.d(
                        "TAG",
                        "onAnimationEnd: ${(oneView.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin}" +
                                "  ${(twoView.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin}\""
                    )
                    oneView.postDelayed({
                        isReset = !isReset
                        oneView.text = twoView.text
                        twoView.text = System.currentTimeMillis().toString()
                        startAnim(oneView, twoView)
                    }, 1500)
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                    Log.d("TAG", "onAnimationRepeat: ")
                }
            })
            duration = 500
            interpolator = LinearInterpolator()
            start()
        }
    }



    override fun onBackPressed() {
        if (CheckHelp.onDoubleClickExit(EXIT_APP_DOUBLE_CLICK_TIME))
            super.onBackPressed()
    }

}