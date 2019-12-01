package com.rwz.lib_comm.comm

/**
 * date： 2019/11/29 14:17
 * author： rwz
 * description：
 **/
open class SimpleObserver<T> : CommonObserver<T>() {

    override fun onNext(value: T) {
        //do nothing
    }

}