package com.rwz.lib_comm.entity.turn

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * date： 2019/11/18 11:17
 * author： rwz
 * description：消息对话框传递实体类
 **/
@Parcelize
data class LoadingDialogTurnEntity(
    var tips: String? = null,
    var canDismissOutSide: Boolean = false//点击外面是否可以关闭
) : Parcelable