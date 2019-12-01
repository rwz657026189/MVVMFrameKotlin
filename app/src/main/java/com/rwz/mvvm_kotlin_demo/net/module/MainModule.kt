package com.rwz.mvvm_kotlin_demo.net.module

import com.rwz.lib_comm.base.BaseModule
import com.rwz.lib_comm.config.MAIN_HOST
import com.rwz.mvvm_kotlin_demo.entity.response.CommentData
import com.rwz.mvvm_kotlin_demo.entity.response.JokeEntity
import com.rwz.mvvm_kotlin_demo.entity.response.NewComment
import com.rwz.mvvm_kotlin_demo.net.api.MainApi
import io.reactivex.Observable

/**
 * date： 2019/12/1 11:15
 * author： rwz
 * description：
 **/
object MainModule : BaseModule() {

    fun mainList(): Observable<List<JokeEntity>> {
        return getService(MainApi::class.java)
            .mainList(MAIN_HOST)
            .compose(toMainThread())
            .map { it.List }
    }

    fun detail(id: String):Observable<List<NewComment>> =
        getService(MainApi::class.java)
            .detail(id)
            .compose(toMainThread())
            .map { it.Detail.NewCommentList }

}