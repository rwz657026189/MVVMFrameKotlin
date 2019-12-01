package com.rwz.lib_comm.entity.response

/**
 * date： 2019/11/7 17:20
 * author： rwz
 * description：
 **/
open class Response<T>(
    open var data: T,
    open val msg: String,
    open val code : Int
)