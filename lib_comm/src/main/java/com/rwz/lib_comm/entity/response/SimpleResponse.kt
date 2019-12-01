package com.rwz.lib_comm.entity.response

/**
 * date： 2019/11/29 14:35
 * author： rwz
 * description：简单的响应类
 **/
data class SimpleResponse(
    var result: Boolean = false, //结果
    var message: String? = null //信息
)