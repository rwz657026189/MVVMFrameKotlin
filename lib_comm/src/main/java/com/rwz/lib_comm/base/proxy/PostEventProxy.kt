package com.rwz.lib_comm.base.proxy

import android.app.Activity
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.entity.turn.LoadingDialogTurnEntity
import com.rwz.lib_comm.entity.turn.MsgDialogTurnEntity

/**
 * date： 2019/11/29 12:39
 * author： rwz
 * description：
 **/
class PostEventProxy(private var activity: Activity,
                     private var mDialogProxy: DialogProxy) : IView{

    //是否禁用触摸屏幕（禁用一切事件）
    var isForbidTouchScreen: Boolean = false

    override fun onPostEvent(type: Int, params: Any?) {
        when (type) {
            //结束当前activity
            IView.FINISH_ATY -> activity.finish()
            //显示消息对话框
            IView.SHOW_DIALOG -> if (params != null && params is MsgDialogTurnEntity) {
                mDialogProxy.showMsgDialog(params)
            }
            //显示正在加载对话框
            IView.SHOW_LOADING -> if (params != null && params is LoadingDialogTurnEntity) {
                mDialogProxy.showLoadDialog(params)
            }
            //隐藏正在加载对话框
            IView.DISMISS_LOADING -> mDialogProxy.dismissLoadDialog()
            //是否禁用触摸屏幕
            IView.FORBID_TOUCH_SCREEN -> if (params != null && params is Boolean) {
                isForbidTouchScreen = params
            }
        }
    }

}