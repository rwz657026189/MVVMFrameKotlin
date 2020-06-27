package com.rwz.lib_comm.entity.params

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable

/**
 * date： 2020/6/27 15:32
 * author： rwz
 * description：
 **/
data class ContainerEntity(

    //标题
    val title: String,
    //FragmentClassName
    val className: String,
    //参数
    val args: Bundle?

) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readParcelable<Bundle>(Bundle::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(className)
        writeParcelable(args, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ContainerEntity> =
            object : Parcelable.Creator<ContainerEntity> {
                override fun createFromParcel(source: Parcel): ContainerEntity =
                    ContainerEntity(source)

                override fun newArray(size: Int): Array<ContainerEntity?> = arrayOfNulls(size)
            }
    }
}