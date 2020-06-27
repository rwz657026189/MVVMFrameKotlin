package com.rwz.mvvm_kotlin_demo.duplicated

import android.os.Bundle
import android.view.View
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.abs.IViewModule
import com.rwz.lib_comm.base.BaseActivity
import com.rwz.lib_comm.config.EXIT_APP_DOUBLE_CLICK_TIME
import com.rwz.lib_comm.databinding.ActivityContainerBinding
import com.rwz.lib_comm.utils.app.CheckHelp
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.ui.activity.MainFragment

/**
 * date： 2020/6/27 15:54
 * author： rwz
 * description：
 **/
class MainDuplicatedActivity : BaseActivity<ActivityContainerBinding, IViewModule<IView>>() {

    override fun setViewModule(): IViewModule<IView>?  = null

    override fun setLayoutId(): Int = R.layout.activity_container

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        val visibility = window.decorView.systemUiVisibility
        val visible = if (false)
            visibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        else
            visibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        window.decorView.systemUiVisibility = visible
        supportFragmentManager.beginTransaction()
            .add(R.id.container, MainDuplicatedVpFragment(), "MainFragment")
            .commit()

    }

    override fun onBackPressed() {
        if (CheckHelp.onDoubleClickExit(EXIT_APP_DOUBLE_CLICK_TIME))
            super.onBackPressed()
    }

}