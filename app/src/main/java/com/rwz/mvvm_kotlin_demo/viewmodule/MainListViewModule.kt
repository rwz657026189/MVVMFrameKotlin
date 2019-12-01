package com.rwz.mvvm_kotlin_demo.viewmodule

import com.rwz.lib_comm.abs.IListView
import com.rwz.lib_comm.base.BaseListViewModule
import com.rwz.lib_comm.comm.CommonObserver
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.utils.show.ToastUtil
import com.rwz.mvvm_kotlin_demo.entity.TestEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * date： 2019/11/30 18:10
 * author： rwz
 * description：
 **/
class MainListViewModule(private val mType: Int) : BaseListViewModule<IListView>() {

    override fun requestData() {
        Observable.just(mPage)
            .delay(1000, TimeUnit.MILLISECONDS)
            .map {
                val list = ArrayList<TestEntity>()
                val curr = (mPage - FIRST_PAGE) * 20
                for (i in curr until curr + 20) {
                    val url = "https://i04piccdn.sogoucdn.com/578df9ac96a950ee"
                    val url2 = "https://p0.ssl.qhimgs4.com/t010963137bc82c6dbc.jpg"
                    val entity = TestEntity("测试$i", if (mType == 1) url2 else url)
                    entity.spanCount = if (mType == 1) 2 else 1
                    list.add(entity)
                }
                list
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CommonObserver<List<TestEntity>>() {
                override fun onNext(list: List<TestEntity>) {
                    onResponseSuccess("", list)
                }
            })
    }

    override fun onItemClick(position: Int, iEntity: IBaseEntity) {
        ToastUtil.showShortSingle("click me")
    }

}
