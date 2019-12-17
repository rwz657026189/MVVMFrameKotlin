package com.rwz.lib_comm.ui.adapter.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rwz.lib_comm.ui.adapter.rv.mul.decorator.DecoratorProvide
import com.rwz.lib_comm.utils.show.LogUtil
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * date： 2019/11/11 15:19
 * author： rwz
 * description：
 **/
open class BaseBindingAdapter (
    context: Context,
    private val mData: MutableList<Any>,
    private val decoratorProvide: DecoratorProvide
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        decoratorProvide.mAdapter = this
    }

    //单击监听
    var onClickCommand: Consumer<Int>? = null
    //长按监听
    var onLongClickCommand: Consumer<Int>? = null

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {
        LogUtil.d("viewType = $viewType")
        val holder =
            decoratorProvide.getDecorator(viewType).onCreateViewHolder(parent, viewType, inflater)
        //单击事件
        onClickCommand?.let {command ->
            holder.itemView.setOnClickListener{ Observable.just(holder.layoutPosition).filter{ pos -> pos >= 0}.subscribe(command)}
        }
        //长按事件
        onLongClickCommand?.let {command ->
            holder.itemView.setOnLongClickListener{
                Observable.just(holder.layoutPosition).filter{pos -> pos >= 0}.subscribe(command)
                true
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemType = getItemViewType(position)
        decoratorProvide.getDecorator(itemType)
            .onBindViewHolder(holder, mData[position], position)
    }

    /**
     * 删除一条数据
     */
    fun remove(index: Int) {
        mData.takeIf { mData.size > index && index > -1 }?.let {
            it.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    /**
     * 添加一条数据
     */
    fun add(data: Any?) = add(mData.size, data)

    /**
     * 在指定位置添加一条数据
     */
    fun add(position: Int, data: Any?) {
        data?.takeIf {
            mData.size >= position && position >= 0
        }?.let {
            mData.add(position, it)
            notifyItemInserted(position)
        }
    }


    /**
     * 加载更多数据
     */
    fun addData(list: List<Any>?) {
        list?.takeIf { list.isNotEmpty() }?.let {
            mData.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = mData.size

    override fun getItemViewType(position: Int): Int {
        return decoratorProvide.getItemViewType(position, mData[position])
    }

}

