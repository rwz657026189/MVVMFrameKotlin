package com.rwz.lib_comm.ui.adapter.rv.mul.decorator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rwz.lib_comm.BR
import com.rwz.lib_comm.ui.adapter.rv.BaseBindingVH
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.utils.show.LogUtil

/**
 * date： 2019/11/12 15:13
 * author： rwz
 * description：
 **/
class DataBindingDecorator<D : IBaseEntity>(var provide :DecoratorProvide)
    : IDecorator<D, BaseBindingVH<ViewDataBinding>>{

    override val viewType: Int = DecoratorProvide.DATA_BINDING_VIEW_TYPE

    override fun itemViewType(position: Int, data: D): Int {
        LogUtil.d("itemViewType type = ${data.itemLayoutId()}")
        provide.putDecorator(this, data.itemLayoutId())
        return data.itemLayoutId()
    }

    //用于设置Item的事件Presenter
    var viewModule : Any? = null

    override fun onBindViewHolder(holder: BaseBindingVH<ViewDataBinding>, data: D, position: Int) {
        with(holder.binding){
            setVariable(BR.entity, data)
            viewModule?.let { setVariable(BR.viewModule, it) }
            setVariable(BR.position, position)
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
        inflater: LayoutInflater
    ): BaseBindingVH<ViewDataBinding> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, parent, false)
        return BaseBindingVH(binding)
    }

}