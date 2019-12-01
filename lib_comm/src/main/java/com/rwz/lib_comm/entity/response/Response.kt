package com.rwz.lib_comm.entity.response

/**
 * date： 2019/11/7 17:20
 * author： rwz
 * description：
 **/
data class Response<T>(
    val data: T,
    val msg: String,
    val code : Int
)