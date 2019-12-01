package com.rwz.lib_comm.base.proxy

import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.rwz.lib_comm.R
import com.rwz.lib_comm.utils.app.ResourceUtil

/**
 * date： 2019/11/29 10:37
 * author： rwz
 * description：toolbar相关控件
 **/
class ToolbarProxy(rootView: View, onClickListener: View.OnClickListener) {

    var titleView: TextView? = null
    var leftView: TextView? = null
    var rightView: TextView? = null
    var toolbar: View? = null

    init {
        titleView = rootView.findViewById(R.id.title)
        leftView = rootView.findViewById(R.id.left)
        rightView = rootView.findViewById(R.id.right)
        toolbar = rootView.findViewById(R.id.toolbar)
        titleView?.setOnClickListener(onClickListener)
        leftView?.setOnClickListener(onClickListener)
        rightView?.setOnClickListener(onClickListener)
    }

    fun setLeftDrawable(@DrawableRes leftRes: Int) {
        leftView?.setCompoundDrawablesWithIntrinsicBounds(ResourceUtil.getDrawable(leftRes)
            , null, null, null)
    }

    fun setRightDrawable(@DrawableRes rightRes: Int) {
        rightView?.setCompoundDrawablesWithIntrinsicBounds(null, null,
            ResourceUtil.getDrawable(rightRes), null)
    }


}