package com.rwz.mvvm_kotlin_demo

import com.rwz.lib_comm.base.BaseFragment
import com.rwz.lib_comm.base.BaseTabVpActivity
import com.rwz.lib_comm.databinding.ActivityTabVpBinding
import com.rwz.lib_comm.entity.extension.TabEntity
import com.rwz.lib_comm.utils.app.FragmentUtil
import com.rwz.lib_comm.utils.app.ResourceUtil
import com.rwz.mvvm_kotlin_demo.ui.fragment.MainListFragment
import com.rwz.mvvm_kotlin_demo.ui.fragment.MineFragment
import java.util.*

class MainActivity : BaseTabVpActivity<ActivityTabVpBinding>() {

    override fun setLayoutId(): Int {
        return R.layout.activity_main
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
        else FragmentUtil.newInstance(
            MainListFragment::class.java,
            position
        )
    }

}
