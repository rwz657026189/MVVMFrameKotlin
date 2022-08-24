package com.rwz.lib_comm.ui.adapter.rv

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * date： 2019/11/11 16:18
 * author： rwz
 * description：
 **/
class BaseBindingVH<T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)