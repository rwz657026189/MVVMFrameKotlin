package com.rwz.lib_comm.ui.adapter.rv.mul.decorator

import android.util.SparseArray
import androidx.core.util.contains
import androidx.recyclerview.widget.RecyclerView
import com.rwz.lib_comm.extension.TAG
import com.rwz.lib_comm.ui.adapter.rv.BaseBindingAdapter
import com.rwz.lib_comm.utils.show.LogUtil
import kotlin.concurrent.thread

/**
 * date： 2019/11/12 15:44
 * author： rwz
 * description：
 **/
abstract class DecoratorProvide {

    var mAdapter: BaseBindingAdapter? = null

    private val decoratorData = SparseArray<IDecorator<out Any, out RecyclerView.ViewHolder>>()

    companion object{
        //DataBinding类型专用
        const val DATA_BINDING_VIEW_TYPE = 0
    }

    fun putDecorator(decorator: IDecorator<out Any, out RecyclerView.ViewHolder>,
                     type: Int = decorator.viewType) = apply {
        decoratorData.takeUnless { it.contains(type) }?.put(type, decorator)
        LogUtil.d(TAG, "decoratorData = $decoratorData")
    }

    fun getDecorator(viewType: Int): IDecorator<Any, RecyclerView.ViewHolder> {
        return decoratorData.get(viewType) as IDecorator<Any, RecyclerView.ViewHolder>
    }


    /**
     * 若自定义type，需要重写该方法
     */
    abstract fun  getItemViewType(position: Int,  data: Any): Int

}