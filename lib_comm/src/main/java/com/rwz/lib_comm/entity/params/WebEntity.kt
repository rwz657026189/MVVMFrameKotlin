package com.rwz.lib_comm.entity.params

import android.os.Bundle
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * date： 2019/12/6 15:43
 * author： rwz
 * description：webView传递参数
 **/
@Parcelize
data class WebEntity(
    //链接
    val url: String,
    //标题
    val title: String? = null,
    //传递的参数
    val params: Bundle? = null,
    //类型（部分H5页面需要特殊处理）
    val type: Int = 0
) : Parcelable