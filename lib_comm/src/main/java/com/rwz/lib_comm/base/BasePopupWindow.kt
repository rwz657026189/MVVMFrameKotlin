package com.rwz.lib_comm.base

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * date： 2019/11/25 16:44
 * author： rwz
 * description：
 **/
abstract class BasePopupWindow<VB : ViewDataBinding> @JvmOverloads constructor(
    context: Context,
    width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
) :
    PopupWindow() {

    protected val TAG: String = javaClass.simpleName
    var context: Context
        protected set
    protected lateinit var mRootView: View
    var mBinding: VB? = null

    init {
        this.context = context
        initPopupWindow(width, height)
        init()
    }

    protected fun init() {}

    private fun initPopupWindow(width: Int, height: Int) {
        val inflater = LayoutInflater.from(context)
        mBinding = DataBindingUtil.inflate(inflater, setLayoutId(), null, false)
        mRootView = mBinding?.root ?: inflater.inflate(setLayoutId(), null)
        contentView = mRootView
        //设置弹出窗体的宽
        setWidth(width)
        //设置弹出窗体的高
        setHeight(height)
        isFocusable = true
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //        setAnimationStyle(R.style.wisdom_anim_style);
        // 设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(null)
    }

    /**
     * 设置资源布局
     *
     * @return
     */
    protected abstract fun setLayoutId(): Int

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int) {
        if (Build.VERSION.SDK_INT >= 24 && anchor != null) {
            val visibleFrame = Rect()
            anchor.getGlobalVisibleRect(visibleFrame)
            val height = anchor.resources.displayMetrics.heightPixels - visibleFrame.bottom
            setHeight(height)
        }
        super.showAsDropDown(anchor, xoff, yoff)
    }
}