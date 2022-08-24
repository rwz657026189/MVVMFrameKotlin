package com.rwz.lib_comm.extension

/**
 * date： 2019/11/29 13:21
 * author： rwz
 * description：
 **/

//为所有类扩展一个TAG属性，用于打印日志
val Any.TAG: String
    get() = javaClass.simpleName

