package com.rwz.mvvm_kotlin_demo.ui.fragment

import android.Manifest
import android.os.Bundle
import android.view.View
import com.rwz.lib_comm.base.BaseFragment
import com.rwz.lib_comm.utils.app.PermissionHelp
import com.rwz.lib_comm.utils.show.ToastUtil
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.databinding.FragmentMineBinding
import com.rwz.mvvm_kotlin_demo.viewmodule.MineViewModule
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * date： 2019/11/30 18:32
 * author： rwz
 * description：
 **/

class MineFragment : BaseFragment<FragmentMineBinding, MineViewModule>() {

    override fun setViewModule(): MineViewModule {
        return MineViewModule()
    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mToolbarProxy.titleView?.text = getString(R.string.mine)
        mToolbarProxy.leftView?.visibility = View.GONE
        container.bindBesselImageView(head)
        container.bindScrollerView(content)
        container.bindThrobView(avatar)
        pull.setOnClickListener{
            container.startScrollAnim(true)
        }
        container.startScrollAnim(true)
    }

    override fun onTitleClick() {
//        super.onTitleClick()
        //ToastUtil.showShort(t1 ? "成功" : "")
        PermissionHelp.requestMulPermissions(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            .subscribe { t1, t2 ->
                ToastUtil.takeIf { t1 }?.showShort("成功")
            }
    }

}