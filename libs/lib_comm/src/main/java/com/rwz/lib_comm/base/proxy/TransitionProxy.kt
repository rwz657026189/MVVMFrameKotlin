package com.rwz.lib_comm.base.proxy

import android.app.Activity
import androidx.annotation.AnimRes
import com.rwz.lib_comm.R

/**
 * date： 2019/11/29 10:57
 * author： rwz
 * description：转场效果
 **/
class TransitionProxy (private var activity: Activity) {

    companion object{
        val TRANS_ANIM_SLIDE = 0 //侧滑转场动画
        val TRANS_ANIM_ALPHA = 1 //alpha转场动画
    }

    @AnimRes
    protected var mInNextAnimResId = R.anim.slide_right_in   // 进场动画id
    @AnimRes
    protected var mInCurrAnimResId = R.anim.slide_right_out  // 进场动画id
    @AnimRes
    protected var mOutNextAnimResId = R.anim.slide_left_in   // 出场动画id
    @AnimRes
    protected var mOutCurrAnimResId = R.anim.slide_left_out  // 出场动画id
    var isForbidTransitionAnim: Boolean = false//是否禁用出场入场动画
    var isForbidOutTransitionAnim: Boolean = false//是否禁用出场动画

    fun setTransitionAnim(animType: Int) {
        if (animType == TRANS_ANIM_ALPHA) { //alpha
            mInNextAnimResId = R.anim.slide_alpha_in
            mInCurrAnimResId = R.anim.slide_alpha_out
            mOutNextAnimResId = R.anim.slide_alpha_in
            mOutCurrAnimResId = R.anim.slide_alpha_out
        } else {//侧滑
            mInNextAnimResId = R.anim.slide_right_in
            mInCurrAnimResId = R.anim.slide_right_out
            mOutNextAnimResId = R.anim.slide_left_in
            mOutCurrAnimResId = R.anim.slide_left_out
        }
    }

    /**执行转场动画 */
    fun startPlayTransition(isEnter: Boolean) {
        if (isEnter && !isForbidTransitionAnim) {
            activity.overridePendingTransition(mInNextAnimResId, mInCurrAnimResId)
        } else if (!isForbidOutTransitionAnim) {
            activity.overridePendingTransition(mOutNextAnimResId, mOutCurrAnimResId)
        }
    }

    /**
     * @param transType 转场动画 e.g. TRANS_ANIM_ALPHA
     */
    fun startActivity(transType: Int) {
        //开始进入时的转场动画
        if (transType == TRANS_ANIM_ALPHA)
            activity.overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out)
        else
            activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out)
    }

}