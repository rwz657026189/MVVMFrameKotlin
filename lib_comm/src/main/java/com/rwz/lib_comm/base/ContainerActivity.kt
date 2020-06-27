package com.rwz.lib_comm.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rwz.lib_comm.R
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.abs.IViewModule
import com.rwz.lib_comm.config.PARCELABLE_ENTITY
import com.rwz.lib_comm.databinding.ActivityContainerBinding
import com.rwz.lib_comm.entity.params.ContainerEntity

/**
 * date： 2020/6/27 15:29
 * author： rwz
 * description：盛放Fragment的容器，适用单一简单页面
 **/
class ContainerActivity : BaseActivity<ActivityContainerBinding, IViewModule<IView>>() {

    override fun setViewModule(): IViewModule<IView>? = null

    override fun setLayoutId(): Int = R.layout.activity_container

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        val entity = intent.getParcelableExtra<ContainerEntity>(PARCELABLE_ENTITY)
        mToolbarProxy.titleView?.text = entity.title
        val cls  = Class.forName(entity.className)
        val instance = cls.newInstance()
        if (instance is Fragment) {
            instance.arguments = entity.args
            supportFragmentManager.beginTransaction()
                .add(R.id.container, instance, entity.className)
                .commit()
        } else{
            finish()
        }
    }


}