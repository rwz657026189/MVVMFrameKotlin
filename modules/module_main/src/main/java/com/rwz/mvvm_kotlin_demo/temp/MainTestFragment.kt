package com.rwz.mvvm_kotlin_demo.temp

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.rwz.lib_comm.base.BaseFragment
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.viewmodule.MainDuplicatedViewModule
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * date： 2019/12/21 16:59
 * author： rwz
 * description：
 **/
class MainTestFragment : BaseFragment<ViewDataBinding, MainDuplicatedViewModule>() {

    override fun setViewModule(): MainDuplicatedViewModule = MainDuplicatedViewModule()

    override fun setLayoutId(): Int  = R.layout.fragment_main

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        toolbar.title = "toolbar"
    }

}