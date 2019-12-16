package com.rwz.mvvm_kotlin_demo.duplicated

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.rwz.lib_comm.base.BaseActivity
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.viewmodule.MainDuplicatedViewModule

class MainDuplicatedActivity : BaseActivity<ViewDataBinding, MainDuplicatedViewModule>() {

    override fun setViewModule(): MainDuplicatedViewModule?  = MainDuplicatedViewModule()

    override fun setLayoutId(): Int = R.layout.activity_main_duplicated

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, MainDuplicatedFragment(), "MainDuplicatedFragment")
            .commit()
    }

}
