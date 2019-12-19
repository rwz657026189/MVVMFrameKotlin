package com.rwz.mvvm_kotlin_demo.duplicated

import androidx.databinding.ViewDataBinding
import com.rwz.lib_comm.base.BaseListFragment
import com.rwz.lib_comm.ui.adapter.rv.mul.decorator.BaseDecoratorProvide
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.viewmodule.MainListViewModule

/**
 * date： 2019/12/16 14:54
 * author： rwz
 * description：
 **/
class MainDuplicatedFragment : BaseListFragment<ViewDataBinding, MainListViewModule>() {

    override fun setViewModule(): MainListViewModule  = MainListViewModule(0)

    override fun setLayoutId(): Int {
        return R.layout.fragment_main_duplicated
    }

    override fun setDecoratorProvide(): BaseDecoratorProvide = MainMergeDecoratorProvide()

}