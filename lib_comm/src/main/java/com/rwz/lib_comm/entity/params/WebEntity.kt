package com.rwz.lib_comm.entity.params

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * date： 2019/12/6 15:43
 * author： rwz
 * description：webView传递参数
 **/
data class WebEntity(
    //链接
    val url: String,
    //标题
    val title: String? = null,
    //传递的参数
    val params: Bundle? = null,
    //类型（部分H5页面需要特殊处理）
    val type: Int = 0
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readParcelable<Bundle>(Bundle::class.java.classLoader),
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(url)
        writeString(title)
        writeParcelable(params, 0)
        writeInt(type)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<WebEntity> = object : Parcelable.Creator<WebEntity> {
            override fun createFromParcel(source: Parcel): WebEntity = WebEntity(source)
            override fun newArray(size: Int): Array<WebEntity?> = arrayOfNulls(size)
        }
    }
}