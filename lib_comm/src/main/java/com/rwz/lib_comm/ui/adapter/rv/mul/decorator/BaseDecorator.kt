package com.rwz.lib_comm.ui.adapter.rv.mul.decorator

import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * date： 2019/11/12 18:30
 * author： rwz
 * description：
 **/
abstract class BaseDecorator<D>: IDecorator<D, BaseViewHolder>{

    override fun itemViewType(position: Int, data: D): Int = viewType

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
        inflater: LayoutInflater
    ): BaseViewHolder = BaseViewHolder(inflater.inflate(viewType, parent, false))

}