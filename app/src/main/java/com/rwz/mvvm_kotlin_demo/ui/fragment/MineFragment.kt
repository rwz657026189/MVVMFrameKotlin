package com.rwz.mvvm_kotlin_demo.ui.fragment

import android.os.Bundle
import android.view.View
import com.rwz.lib_comm.base.BaseFragment
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.databinding.FragmentMineBinding
import com.rwz.mvvm_kotlin_demo.viewmodule.MineViewModule

/**
 * date： 2019/11/30 18:32
 * author： rwz
 * description：
 **/

class MineFragment : BaseFragment<FragmentMineBinding, MineViewModule>() {

    override fun setViewModule(): MineViewModule {
        return MineViewModule()
    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mToolbarProxy.titleView?.text = getString(R.string.mine)
        mToolbarProxy.leftView?.visibility = View.GONE
    }
}