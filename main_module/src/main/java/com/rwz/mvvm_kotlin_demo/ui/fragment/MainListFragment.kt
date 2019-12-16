package com.rwz.mvvm_kotlin_demo.ui.fragment

import android.os.Bundle
import com.rwz.lib_comm.base.BaseListFragment
import com.rwz.lib_comm.config.INT
import com.rwz.lib_comm.databinding.LayoutRecyclerviewBinding
import com.rwz.lib_comm.ui.dialog.CommBottomDialog
import com.rwz.lib_comm.utils.app.DialogHelp
import com.rwz.lib_comm.utils.show.ToastUtil
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.viewmodule.MainListViewModule

/**
 * date： 2019/11/30 18:09
 * author： rwz
 * description：
 **/
class MainListFragment : BaseListFragment<LayoutRecyclerviewBinding, MainListViewModule>() {

    private var mType: Int = 0

    override fun config() {
        super.config()
        mType = arguments?.getInt(INT)!!
    }

    override fun setLayoutId(): Int {
        return R.layout.layout_toolbar_recyclerview
    }

    override fun setViewModule(): MainListViewModule {
        return MainListViewModule(mType)
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        when (mType) {
            0 -> {
                mToolbarProxy.titleView?.text = getString(R.string.main)
//                mToolbarProxy.setRightDrawable(R.drawable.ic_more)
                mToolbarProxy.setLeftDrawable(0)
            }
            1 -> {
                mToolbarProxy.titleView?.text = getString(R.string.recommend)
                mToolbarProxy.setLeftDrawable(0)
//                mToolbarProxy.rightView?.text = "连续点击3次有惊喜"
            }
        }
    }

    override fun onRightClick() {
        super.onRightClick()
        when (mType) {
            0 -> onClickMain()
            1 -> onClickRecommend()
        }
    }

    private fun onClickMain() {
        val dialog = CommBottomDialog.Build()
            .setTitle("首页更多")
            .addItem("版本升级")
            .addItem("跳转h5")
            .create()
        dialog.setOnClickItemListener { _, position, _, _ ->
            if (position == 0) {
                ToastUtil.showShortSingle("暂时没有新版本")
            } else {
            }
        }
        DialogHelp.show(context, dialog, "CommBottomDialog")
    }

    private fun onClickRecommend() {
    }

}
