package com.rwz.mvvm_kotlin_demo.entity.response

import android.os.Parcelable
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.utils.app.CommUtils
import com.rwz.lib_comm.utils.system.DateUtil
import com.rwz.lib_comm.utils.system.StringHelp
import com.rwz.mvvm_kotlin_demo.R
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 * date： 2019/12/1 11:11
 * author： rwz
 * description：
 **/
@Parcelize
 data class JokeEntity(
    val ExColumn: String,
    val audit: Int,
    val bigimg: String,
    val commentcount: Int,
    val downcount: Int,
    val height: Int,
    val id: Int,
    val ipcount: Int,
    val membericon: String,
    val memberid: Int,
    val membername: String,
    val propAudit: String,
    val propDetailPageName: String,
    val propFavID: Int,
    val propIsComment: Boolean,
    val propIsDown: Boolean,
    val propIsFavorite: Boolean,
    val propIsShare: Boolean,
    val propIsUp: Boolean,
    val propPageName: String,
    val propType: String,
    val sharecount: Int,
    val tagid: String,
    val time: String,
    val title: String,
    val type: Int,
    val upcount: Int,
    val width: Int
) : IBaseEntity, Parcelable {
    @IgnoredOnParcel
    var timeStr:String = time
    get() {
        val realTime = time.filterNot { it > '9' || it < '0' }
        return DateUtil.formatDate(StringHelp.parseLong(realTime), DateUtil.LONG_LONG_AGO)
    }


    override fun itemLayoutId(): Int  = R.layout.item_main
}