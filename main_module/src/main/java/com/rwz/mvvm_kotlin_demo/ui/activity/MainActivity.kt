package com.rwz.mvvm_kotlin_demo.ui.activity

import android.os.Bundle
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.abs.IViewModule
import com.rwz.lib_comm.base.BaseActivity
import com.rwz.lib_comm.config.EXIT_APP_DOUBLE_CLICK_TIME
import com.rwz.lib_comm.databinding.ActivityContainerBinding
import com.rwz.lib_comm.utils.app.CheckHelp
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
    }

    override fun onBackPressed() {
        if (CheckHelp.onDoubleClickExit(EXIT_APP_DOUBLE_CLICK_TIME))
            super.onBackPressed()
    }

}