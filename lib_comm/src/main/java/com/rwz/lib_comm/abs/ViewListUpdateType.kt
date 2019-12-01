package com.rwz.lib_comm.abs

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * date： 2019/11/15 16:36
 * author： rwz
 * description：
 **/
@IntDef(
    IListView.INSERTED,
    IListView.CHANGED,
    IListView.REMOVE,
    IListView.SCROLL_TO_BOTTOM,
    IListView.SCROLL_TO_TOP,
    IListView.SCROLL_TO_POSITION
)
@kotlin.annotation.Retention
annotation class ViewListUpdateType