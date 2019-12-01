package com.rwz.lib_comm.entity.params

/**
 * date： 2019/11/29 14:27
 * author： rwz
 * description：
 **/
open class CommandEntity<T>(
    open var id: Int = 0,
    open var t: T? = null
)
