package com.rwz.mvvm_kotlin_demo.entity.response

import android.os.Parcel
import android.os.Parcelable
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.utils.system.DateUtil
import com.rwz.lib_comm.utils.system.StringHelp
import com.rwz.mvvm_kotlin_demo.R
import kotlinx.android.parcel.IgnoredOnParcel

/**
 * date： 2019/12/1 11:11
 * author： rwz
 * description：
 **/
data class JokeEntity(
    val ExColumn: String?,
    val audit: Int,
    val bigimg: String?,
    val commentcount: Int,
    val downcount: Int,
    val height: Int,
    val id: Int,
    val ipcount: Int,
    val membericon: String?,
    val memberid: Int,
    val membername: String?,
    val propAudit: String?,
    val propDetailPageName: String?,
    val propFavID: Int,
    val propIsComment: Boolean,
    val propIsDown: Boolean,
    val propIsFavorite: Boolean,
    val propIsShare: Boolean,
    val propIsUp: Boolean,
    val propPageName: String?,
    val propType: String?,
    val sharecount: Int,
    val tagid: String?,
    val time: String?,
    val title: String?,
    val type: Int,
    val upcount: Int,
    val width: Int
) : IBaseEntity, Parcelable {

    @IgnoredOnParcel
    var timeStr:String? = time
        get() {
            val realTime = time?.filterNot { it > '9' || it < '0' }
            return DateUtil.formatDate(StringHelp.parseLong(realTime), DateUtil.LONG_LONG_AGO)
        }

    override fun itemLayoutId(): Int = R.layout.item_main

    constructor(source: Parcel) : this(
        source.readString(),
        source.readInt(),
        source.readString(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readString(),
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readInt(),
        1 == source.readInt(),
        1 == source.readInt(),
        1 == source.readInt(),
        1 == source.readInt(),
        1 == source.readInt(),
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readInt(),
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(ExColumn)
        writeInt(audit)
        writeString(bigimg)
        writeInt(commentcount)
        writeInt(downcount)
        writeInt(height)
        writeInt(id)
        writeInt(ipcount)
        writeString(membericon)
        writeInt(memberid)
        writeString(membername)
        writeString(propAudit)
        writeString(propDetailPageName)
        writeInt(propFavID)
        writeInt((if (propIsComment) 1 else 0))
        writeInt((if (propIsDown) 1 else 0))
        writeInt((if (propIsFavorite) 1 else 0))
        writeInt((if (propIsShare) 1 else 0))
        writeInt((if (propIsUp) 1 else 0))
        writeString(propPageName)
        writeString(propType)
        writeInt(sharecount)
        writeString(tagid)
        writeString(time)
        writeString(title)
        writeInt(type)
        writeInt(upcount)
        writeInt(width)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<JokeEntity> = object : Parcelable.Creator<JokeEntity> {
            override fun createFromParcel(source: Parcel): JokeEntity = JokeEntity(source)
            override fun newArray(size: Int): Array<JokeEntity?> = arrayOfNulls(size)
        }
    }
}