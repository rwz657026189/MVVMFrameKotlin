package com.rwz.mvvm_kotlin_demo.ui.activity

import android.os.Bundle
import com.rwz.lib_comm.base.BaseFragment
import com.rwz.lib_comm.base.BaseTabVpActivity
import com.rwz.lib_comm.config.EXIT_APP_DOUBLE_CLICK_TIME
import com.rwz.lib_comm.databinding.ActivityTabVpBinding
import com.rwz.lib_comm.entity.extension.TabEntity
import com.rwz.lib_comm.utils.app.CheckHelp
import com.rwz.lib_comm.utils.app.ResourceUtil
import com.rwz.lib_comm.utils.factory.FragmentFactory
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.ui.fragment.MainListFragment
import com.rwz.mvvm_kotlin_demo.ui.fragment.MineFragment
import java.util.*

class MainActivity : BaseTabVpActivity<ActivityTabVpBinding>() {

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
//        startActivity(Intent(this, MainDuplicatedActivity::class.java))
    }

    override fun requestTabData() {
        val list = ArrayList<TabEntity>()
        list.add(TabEntity(title = ResourceUtil.getString(R.string.main)))
        list.add(TabEntity(title = ResourceUtil.getString(R.string.recommend)))
        list.add(TabEntity(title = ResourceUtil.getString(R.string.mine)))
        setupContentViewPager(list)
    }

    override fun initFragment(tab: TabEntity, position: Int): BaseFragment<*, *> {
        return if (position == 2)
            MineFragment()
        else FragmentFactory.newInstance(
            MainListFragment::class.java,
            position
        )
    }

    override fun onBackPressed() {
        if (CheckHelp.onDoubleClickExit(EXIT_APP_DOUBLE_CLICK_TIME))
            super.onBackPressed()
    }

}
