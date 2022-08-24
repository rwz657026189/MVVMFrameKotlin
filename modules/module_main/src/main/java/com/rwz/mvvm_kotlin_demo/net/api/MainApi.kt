package com.rwz.mvvm_kotlin_demo.net.api

import com.rwz.mvvm_kotlin_demo.entity.SuccessResponse
import com.rwz.mvvm_kotlin_demo.entity.response.CommentData
import com.rwz.mvvm_kotlin_demo.entity.response.JokeEntity
import io.reactivex.Observable
import retrofit2.http.*

/**
 * date： 2019/12/1 10:39
 * author： rwz
 * description：
 **/
interface MainApi {

    //key=[您申请的APPKEY]&page=2&rows=10
    @GET
    fun mainList(@Url url: String): Observable<SuccessResponse<List<JokeEntity>>>

    @POST("/Article/Detail")
    @FormUrlEncoded
    fun detail(@Field("id") id: String): Observable<CommentData>

}