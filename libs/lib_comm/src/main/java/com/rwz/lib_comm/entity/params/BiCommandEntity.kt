package com.rwz.lib_comm.entity.params

/**
 * date： 2019/11/30 17:10
 * author： rwz
 * description：
 **/

class BiCommandEntity<A, B>(
    override var id: Int = 0,
    var a: A,
    override var t: B? = null
): CommandEntity<B>(id, t)