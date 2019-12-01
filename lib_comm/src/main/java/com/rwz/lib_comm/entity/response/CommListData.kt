package com.rwz.lib_comm.entity.response

import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity

/**
 * date： 2019/11/29 15:34
 * author： rwz
 * description：
 **/
data class CommListData<T : IBaseEntity>(

    var count: String? = null,
    var arrayList: List<T>? = null

)