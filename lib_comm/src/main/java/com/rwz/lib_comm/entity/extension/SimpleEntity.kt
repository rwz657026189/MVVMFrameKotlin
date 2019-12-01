package com.rwz.lib_comm.entity.extension

import com.rwz.lib_comm.entity.response.BaseListEntity

/**
 * date： 2019/12/1 17:58
 * author： rwz
 * description：
 **/

class SimpleEntity(
    private val itemLayoutId: Int,
    val data: Any
) : BaseListEntity() {
    override fun itemLayoutId(): Int {
        return itemLayoutId
    }
}