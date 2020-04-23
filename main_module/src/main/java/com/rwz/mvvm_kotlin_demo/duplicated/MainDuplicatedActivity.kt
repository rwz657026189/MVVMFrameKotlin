package com.rwz.mvvm_kotlin_demo.duplicated

import android.os.Bundle
import android.view.View
import com.rwz.lib_comm.base.BaseFragment
import com.rwz.lib_comm.base.BaseTabVpActivity
import com.rwz.lib_comm.config.EXIT_APP_DOUBLE_CLICK_TIME
import com.rwz.lib_comm.entity.extension.TabEntity
import com.rwz.lib_comm.utils.app.CheckHelp
import com.rwz.lib_comm.utils.app.ResourceUtil
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.databinding.ActivityMainDuplicatedBinding
import com.rwz.mvvm_kotlin_demo.temp.MainTestFragment

class MainDuplicatedActivity : BaseTabVpActivity<ActivityMainDuplicatedBinding>() {

    override fun setLayoutId(): Int {
        return R.layout.activity_main_duplicated
    }

    override fun requestTabData() {
        val list = ArrayList<TabEntity>()
        list.add(TabEntity(title = ResourceUtil.getString(R.string.main)))
        list.add(TabEntity(title = ResourceUtil.getString(R.string.recommend)))
        list.add(TabEntity(title = ResourceUtil.getString(R.string.mine)))
        setupContentViewPager(list)
    }

    override fun initFragment(tab: TabEntity, position: Int): BaseFragment<*, *> {
        return MainTestFragment()
    }

    override fun onBackPressed() {
        if (CheckHelp.onDoubleClickExit(EXIT_APP_DOUBLE_CLICK_TIME))
            super.onBackPressed()
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        val visibility = window.decorView.systemUiVisibility
        val visible = if (false)
            visibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        else
            visibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        window.decorView.systemUiVisibility = visible
    }

}
