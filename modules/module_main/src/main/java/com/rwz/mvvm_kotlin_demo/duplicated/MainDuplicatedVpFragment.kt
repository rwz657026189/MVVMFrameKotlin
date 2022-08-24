package com.rwz.mvvm_kotlin_demo.duplicated

import com.rwz.lib_comm.base.BaseFragment
import com.rwz.lib_comm.base.BaseTabVpFragment
import com.rwz.lib_comm.entity.extension.TabEntity
import com.rwz.lib_comm.utils.app.ResourceUtil
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.databinding.ActivityMainDuplicatedBinding
import com.rwz.mvvm_kotlin_demo.temp.MainTestFragment

class MainDuplicatedVpFragment : BaseTabVpFragment<ActivityMainDuplicatedBinding>() {

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

}
