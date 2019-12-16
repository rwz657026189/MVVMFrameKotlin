package com.rwz.mvvm_kotlin_demo.duplicated

import com.rwz.lib_comm.ui.adapter.rv.mul.decorator.DataBindingDecorator
import com.rwz.lib_comm.ui.adapter.rv.mul.decorator.DecoratorProvide
import com.rwz.lib_comm.utils.show.LogUtil
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.entity.response.JokeEntity

/**
 * date： 2019/12/16 15:23
 * author： rwz
 * description：混合使用
 **/
class MainMergeDecoratorProvide : DecoratorProvide() {

    override fun getItemViewType(position: Int, data: Any): Int {
        LogUtil.d("getItemViewType, position = $position, data = $data")
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