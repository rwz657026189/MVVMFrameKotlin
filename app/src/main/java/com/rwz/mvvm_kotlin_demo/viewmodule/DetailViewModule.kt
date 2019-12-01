package com.rwz.mvvm_kotlin_demo.viewmodule

import com.rwz.lib_comm.abs.IListView
import com.rwz.lib_comm.base.BaseListViewModule
import com.rwz.lib_comm.entity.extension.SimpleEntity
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.utils.show.ToastUtil
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.entity.response.JokeEntity
import com.rwz.mvvm_kotlin_demo.entity.response.NewComment
import com.rwz.mvvm_kotlin_demo.net.module.MainModule

/**
 * date： 2019/12/1 13:36
 * author： rwz
 * description：
 **/
class DetailViewModule(val jokeEntity: JokeEntity) : BaseListViewModule<IListView>() {

    init {
        isLoadingMoreEnable = false
        isRefreshEnable = false
    }

    override fun requestData() {
        MainModule.detail(jokeEntity.id.toString()).subscribe(getObserver())
    }


    override fun onItemClick(position: Int, iEntity: IBaseEntity) {
    }

    override fun handlerData(requestCode: String, data: Any?) {
        mData.add(jokeEntity)
        mData.add(SimpleEntity(R.layout.item_bar, "热评"))
        super.handlerData(requestCode, data)
    }

    fun addComment(content: String) {
        if (mData.size > 2) {
            var newEntity = NewComment(content, System.currentTimeMillis().toString(), "", "游客用户")
            mData.add(2, newEntity)
        }
        notifyDataSetChanged()
        ToastUtil.showShortSingle("评论成功")
    }

}