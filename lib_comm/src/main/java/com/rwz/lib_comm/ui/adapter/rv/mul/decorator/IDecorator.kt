package com.rwz.lib_comm.ui.adapter.rv.mul.decorator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * date： 2019/11/12 15:11
 * author： rwz
 * description：
 **/
interface IDecorator<D, VH : RecyclerView.ViewHolder>{

    //Decorator类型， 跟Decorator绑定
    val viewType: Int
    //item类型，Decorator可以对应多种itemViewType
    fun itemViewType(position: Int, data: D): Int

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int, inflater: LayoutInflater): VH

    fun onBindViewHolder(holder: VH, data: D, position: Int)


}