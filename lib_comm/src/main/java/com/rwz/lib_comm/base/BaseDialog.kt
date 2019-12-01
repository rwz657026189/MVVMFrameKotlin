package com.rwz.lib_comm.base

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.rwz.lib_comm.R
import io.reactivex.functions.Action

/**
 * date： 2019/11/18 11:20
 * author： rwz
 * description：
 **/
open abstract class BaseDialog<VB : ViewDataBinding> : DialogFragment() {
    protected var TAG = ""
        private set
    //获取binding对象
    protected lateinit var mBind: VB
    protected lateinit var mRootView: View
        private set
    protected lateinit var mActivity: BaseActivity<*, *>
        private set
    var dismissListener: Action? = null

    init {
        setStyle(STYLE_NO_TITLE, R.style.CommonDialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
            dismissListener?.run()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBind = DataBindingUtil.inflate(inflater, setLayoutId(), container, false)
        mRootView = mBind.root
        initFragment()
        return mRootView
    }

    private fun initFragment() {
        TAG = javaClass.simpleName
        if (activity is BaseActivity<*, *>) {
            mActivity = activity as BaseActivity<*, *>
        }
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init(savedInstanceState)
    }

    protected open fun init(savedInstanceState: Bundle?) {
        dialog?.window?.let {
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    /**
     * 设置资源布局
     *
     * @return
     */
    protected abstract fun setLayoutId(): Int

}
