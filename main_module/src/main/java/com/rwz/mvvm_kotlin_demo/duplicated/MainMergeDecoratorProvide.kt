package com.rwz.mvvm_kotlin_demo.duplicated

import com.rwz.lib_comm.ui.adapter.rv.mul.decorator.DataBindingDecorator
import com.rwz.lib_comm.ui.adapter.rv.mul.decorator.BaseDecoratorProvide
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.entity.response.JokeEntity

/**
 * date： 2019/12/16 15:23
 * author： rwz
 * description：混合布局使用
 **/
class MainMergeDecoratorProvide : BaseDecoratorProvide() {

    override fun getItemViewType(position: Int, data: Any): Int {
        when (data) {
            is JokeEntity -> return R.layout.item_main_duplicated
        }
        return getDecorator(DATA_BINDING_VIEW_TYPE).itemViewType(position, data)
    }

    init {
        putDecorator(MainDecorator())
        putDecorator(DataBindingDecorator(this))
    }


}