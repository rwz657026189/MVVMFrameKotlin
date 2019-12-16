package com.rwz.mvvm_kotlin_demo.net.module

import com.rwz.lib_comm.base.BaseModule
import com.rwz.lib_comm.config.MAIN_HOST
import com.rwz.lib_comm.utils.system.AndroidUtils
import com.rwz.mvvm_kotlin_demo.entity.response.CommentData
import com.rwz.mvvm_kotlin_demo.entity.response.JokeEntity
import com.rwz.mvvm_kotlin_demo.entity.response.NewComment
import com.rwz.mvvm_kotlin_demo.net.api.MainApi
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * date： 2019/12/1 11:15
 * author： rwz
 * description：
 **/
object MainModule : BaseModule() {

    fun mainList(): Observable<List<JokeEntity>> {
        //需要过滤部分数据，请求两次，避免数据不足
        return Observable.concat(
            getService(MainApi::class.java).mainList(MAIN_HOST),
            getService(MainApi::class.java).mainList(MAIN_HOST))
            .subscribeOn(Schedulers.newThread())
            .flatMap { Observable.fromIterable(it.List) }
            .filter{it.type == 3}
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
    }

    fun detail(id: String):Observable<List<NewComment>> =
        getService(MainApi::class.java)
            .detail(id)
            .compose(toMainThread())
            .map { it.Detail.NewCommentList }

}