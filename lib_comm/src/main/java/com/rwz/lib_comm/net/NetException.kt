package com.rwz.lib_comm.net

import java.lang.RuntimeException

/**
 * date： 2020/7/11 18:20
 * author： rwz
 * description：
 **/
class NetException(val code: Int, val msg: String) : RuntimeException(msg) {


}