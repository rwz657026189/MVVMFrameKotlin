package com.rwz.lib_comm.entity.turn

import android.os.Parcel
import android.os.Parcelable

/**
 * date： 2019/11/18 11:17
 * author： rwz
 * description：消息对话框传递实体类
 **/
data class LoadingDialogTurnEntity(
    var tips: String? = null,
    var canDismissOutSide: Boolean = false//点击外面是否可以关闭
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(tips)
        writeInt((if (canDismissOutSide) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LoadingDialogTurnEntity> =
            object : Parcelable.Creator<LoadingDialogTurnEntity> {
                override fun createFromParcel(source: Parcel): LoadingDialogTurnEntity =
                    LoadingDialogTurnEntity(source)

                override fun newArray(size: Int): Array<LoadingDialogTurnEntity?> =
                    arrayOfNulls(size)
            }
    }
}