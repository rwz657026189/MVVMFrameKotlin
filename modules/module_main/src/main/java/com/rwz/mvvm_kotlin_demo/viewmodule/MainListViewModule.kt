package com.rwz.mvvm_kotlin_demo.viewmodule

import android.content.Intent
import com.rwz.lib_comm.abs.IListView
import com.rwz.lib_comm.base.BaseListViewModule
import com.rwz.lib_comm.config.MAIN_HOST
import com.rwz.lib_comm.config.PARCELABLE_ENTITY
import com.rwz.lib_comm.entity.extension.wrap.WrapList
import com.rwz.lib_comm.manager.ContextManager
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.utils.show.LogUtil
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.entity.response.BannerEntity
import com.rwz.mvvm_kotlin_demo.entity.response.JokeEntity
import com.rwz.mvvm_kotlin_demo.net.module.MainModule
import com.rwz.mvvm_kotlin_demo.ui.activity.DetailActivity

/**
 * date： 2019/11/30 18:10
 * author： rwz
 * description：
 **/
class MainListViewModule(private val mType: Int) : BaseListViewModule<IListView>() {

    override fun requestData() {
        MainModule.mainList().subscribe(getObserver())
    }

    override fun onItemClick(position: Int, iEntity: IBaseEntity) {
        LogUtil.d(TAG, "onItemClick = $iEntity")
        when (iEntity) {
            is JokeEntity -> {
                val intent = Intent(ContextManager.context, DetailActivity::class.java)
                intent.putExtra(PARCELABLE_ENTITY, iEntity)
                startActivity(intent)
            }
        }
    }

    override fun onClickView(id: Int, iEntity: IBaseEntity?) {
        super.onClickView(id, iEntity)
        if (iEntity is BannerEntity) {
            onItemClick(id, iEntity.entity)
        }
    }

    override fun handlerData(requestCode: String, data: Any?) {
        if (mType == 0 && mPage == FIRST_PAGE) {
            if (data is List<*> && data.isNotEmpty()) {
                val list = mutableListOf<BannerEntity>()
                for (i in 0 until 3) {
                    val entity = data[(Math.random() * data.size).toInt()] as JokeEntity
                    list.add(BannerEntity(MAIN_HOST + entity.bigimg, entity))
                }
                val wrapList = WrapList.Build<BannerEntity>()
                    .create(R.layout.item_banner, list)
                mData.add(wrapList)
            }
        }
        super.handlerData(requestCode, data)
    }

}
