package com.rwz.lib_comm.ui.adapter.rv.mul.decorator

/**
 * date： 2019/11/29 16:50
 * author： rwz
 * description：采用DataBinding
 **/
class DataBindingDecoratorProvide : BaseDecoratorProvide() {

    init {
        putDecorator(DataBindingDecorator(this))
    }

    override fun getItemViewType(position: Int, data: Any): Int
            = getDecorator(DATA_BINDING_VIEW_TYPE).itemViewType(position, data)

    //用于设置Item的事件Presenter
    var viewModule : Any? = null
        set(value) {
            val decorator = getDecorator(DATA_BINDING_VIEW_TYPE)
            if(decorator is DataBindingDecorator)
                decorator.viewModule = value
            field = value
        }

}

