package com.rwz.lib_comm.entity.params

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * date： 2019/11/18 18:02
 * author： rwz
 * description：
 **/
data class CommBottomEntity(
    //内容
    var content: String? = null,
    //颜色
    var color: Int = 0,
    //是否可以点击
    var isClickEnable: Boolean = false
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readInt(),
        1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(content)
        writeInt(color)
        writeInt((if (isClickEnable) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CommBottomEntity> =
            object : Parcelable.Creator<CommBottomEntity> {
                override fun createFromParcel(source: Parcel): CommBottomEntity =
                    CommBottomEntity(source)

                override fun newArray(size: Int): Array<CommBottomEntity?> = arrayOfNulls(size)
            }
    }
}