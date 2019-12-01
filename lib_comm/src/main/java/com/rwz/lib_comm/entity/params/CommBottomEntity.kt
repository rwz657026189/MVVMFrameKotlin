package com.rwz.lib_comm.entity.params

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * date： 2019/11/18 18:02
 * author： rwz
 * description：
 **/
@Parcelize
data class CommBottomEntity(
    //内容
    var content: String? = null,
    //颜色
    var color: Int = 0,
    //是否可以点击
    var isClickEnable: Boolean = false
) : Parcelable