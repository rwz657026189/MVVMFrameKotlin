package com.rwz.mvvm_kotlin_demo.entity.response

import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.utils.system.DateUtil
import com.rwz.lib_comm.utils.system.StringHelp
import com.rwz.mvvm_kotlin_demo.R

/**
 * date： 2019/12/1 17:11
 * author： rwz
 * description：
 **/
data class CommentData(
    val Detail: Detail
)

data class Detail(
    val Article: Article,
    val HotCommentList: Any,
    val HotCount: Int,
    val NewCommentList: List<NewComment>,
    val NewCount: Int,
    val ShowCommentList: List<NewComment>,
    val openid: String
)

data class Article(
    val Arti_Audit: Int,
    val Arti_AutoCheckTime: Any,
    val Arti_BigImg: String,
    val Arti_ClassifyID: Int,
    val Arti_CommentCount: Int,
    val Arti_Content: String,
    val Arti_CreateTime: String,
    val Arti_DownCount: Int,
    val Arti_FavoriteCount: Int,
    val Arti_ID: Int,
    val Arti_IPCount: Int,
    val Arti_Img: String,
    val Arti_Income: Double,
    val Arti_MemberID: Int,
    val Arti_ShareCount: Int,
    val Arti_ShortTitle: Any,
    val Arti_TagID: Any,
    val Arti_Title: String,
    val Arti_UpCount: Int,
    val Arti_Url: Any,
    val Arti_iOSOpen: Int,
    val propAudit: String,
    val propIsComment: Boolean,
    val propIsDown: Boolean,
    val propIsFavorite: Boolean,
    val propIsShare: Boolean,
    val propIsUp: Boolean,
    val propMember: PropMember,
    val propMemberIcon: Any,
    val propMemberName: Any,
    val propTagList: List<Any>
)

data class PropMember(
    val Mem_ApplOpenID: Any,
    val Mem_AucID: Int,
    val Mem_Balace: Double,
    val Mem_By: Int,
    val Mem_ContributeIncome: Double,
    val Mem_CreateTime: String,
    val Mem_Desc: Any,
    val Mem_ID: Int,
    val Mem_Icon: String,
    val Mem_InviteCount: Int,
    val Mem_InviteIncome: Double,
    val Mem_IsGetHongbao: Int,
    val Mem_IsShowComment: Int,
    val Mem_LastLoginTime: String,
    val Mem_Name: String,
    val Mem_OSType: Int,
    val Mem_OpenID: Any,
    val Mem_QQ: Any,
    val Mem_SetupTime: String,
    val Mem_Sex: Int,
    val Mem_Status: Int,
    val Mem_TerminalID: Any,
    val Mem_TerminalType: Any,
    val Mem_Type: Int,
    val Mem_UnionID: Any,
    val Mem_WebOpenID: Any,
    val Mem_WechatNum: Any,
    val propByMember: Any,
    val propIsBlackMember: Boolean,
    val propMemberIcon: String,
    val propType: String
)

data class NewComment(
    val Comm_Content: String,
    val Comm_CreateTime: String,
    val propMemberIcon: String,
    val propMemberName: String
) : IBaseEntity {
    override fun itemLayoutId(): Int  = R.layout.item_recomment

    var timeStr:String = Comm_CreateTime
        get() {
            val realTime = Comm_CreateTime.filterNot { it > '9' || it < '0' }
            return DateUtil.formatDate(
                StringHelp.parseLong(realTime), DateUtil.LONG_LONG_AGO)
        }
}
