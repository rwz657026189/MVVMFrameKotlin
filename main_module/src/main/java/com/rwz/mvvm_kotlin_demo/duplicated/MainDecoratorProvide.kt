package com.rwz.mvvm_kotlin_demo.duplicated

import com.rwz.lib_comm.base.BaseListViewModule
import com.rwz.lib_comm.entity.extension.TempEntity
import com.rwz.lib_comm.entity.extension.wrap.WrapList
import com.rwz.lib_comm.ui.adapter.rv.mul.decorator.DecoratorProvide
import com.rwz.lib_comm.ui.adapter.rv.mul.decorator.TempDecorator
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.entity.response.JokeEntity

/**
 * date： 2019/12/16 15:23
 * author： rwz
 * description：不用DataBinding实现
 **/
class MainDecoratorProvide(val viewModule: BaseListViewModule<*>) : DecoratorProvide() {

    override fun getItemViewType(position: Int, data: Any): Int {
        when (data) {
            is JokeEntity -> return R.layout.item_main_duplicated
            is TempEntity -> return R.layout.layout_temp
            is WrapList<*> -> return R.layout.item_banner
        }
        return 0
    }

    init {
        putDecorator(MainDecorator())
        putDecorator(BannerDecorator(viewModule))
        putDecorator(TempDecorator(viewModule, this))
    }


}