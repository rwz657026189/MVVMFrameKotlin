package com.rwz.lib_comm.abs

import androidx.annotation.IntDef

/**
 * date： 2019/11/15 16:29
 * author： rwz
 * description：
 **/
@IntDef(
    IView.START_ATY,
    IView.FINISH_ATY,
    IView.SHOW_DIALOG,
    IView.UPDATE_DATA,
    IView.SHOW_LOADING,
    IView.DISMISS_LOADING,
    IView.UPLOAD_EDIT_STATE,
    IView.FORBID_TOUCH_SCREEN
)
@kotlin.annotation.Retention
annotation class PostEventType