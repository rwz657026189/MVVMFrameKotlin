package com.rwz.lib_comm.entity.turn

import android.os.Bundle
import android.os.Parcelable
import com.rwz.lib_comm.R
import com.rwz.lib_comm.utils.app.ResourceUtil
import io.reactivex.functions.BiConsumer
import kotlinx.android.parcel.Parcelize

/**
 * date： 2019/11/15 17:01
 * author： rwz
 * description：消息对话框传递实体类
 **/
@Parcelize
class MsgDialogTurnEntity(
    var title: String? = null,
    var msg: String? = null,
    var requestCode: Int = 0,
    var enterText: String? = ResourceUtil.getString(R.string.enter),  //确认文字
    var cancelText: String? = ResourceUtil.getString(R.string.cancel), //取消文字(为空 则 只显示单按钮)
    var cancelable: Boolean = true,//设置外部区域是否可以取消
    var hint: String? = null,
    var params: Bundle? = null     //参数,
) : Parcelable{
    var listener: BiConsumer<MsgDialogTurnEntity, Boolean>? = null//dialog点击监听
}