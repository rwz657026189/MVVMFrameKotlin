package com.rwz.lib_comm.entity.extension

import androidx.annotation.IntDef
import androidx.databinding.ObservableInt
import com.rwz.lib_comm.R
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.utils.app.ResourceUtil
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * date： 2019/11/29 15:08
 * author： rwz
 * description：
 **/
data class TempEntity(
    var nullTipsStr: String? = ResourceUtil.getString(R.string.null_data),
    var errorTipsStr: String? = ResourceUtil.getString(R.string.load_error),
    var nullBtnStr: String? = ResourceUtil.getString(R.string.click_retry),
    var errorBtnStr: String? = ResourceUtil.getString(R.string.click_retry),
    var isShowNullBtn: Boolean = false,
    var isShowErrorBtn: Boolean = true,
    var nullImgRes: Int? = 0,
    var errorImgRes: Int? = 0,
    var type: ObservableInt = ObservableInt(STATUS_LOADING),
    override val itemLayoutId: Int = R.layout.layout_temp
): IBaseEntity{

    fun getShowNullBtn(type: Int) = isShowNullBtn && type == STATUS_NULL

    fun getShowErrorBtn(type: Int) = isShowErrorBtn && type == STATUS_ERROR

    companion object{
        /**
         * 正在加载中
         */
        const val STATUS_LOADING = 3538
        /**
         * 显示空数据
         */
        const val STATUS_NULL = 3539
        /**
         * 显示错误视图
         */
        const val STATUS_ERROR = 3540
        /**
         * 移除空视图
         */
        const val STATUS_DISMISS = 3541
    }

}

@IntDef(
    TempEntity.STATUS_LOADING,
    TempEntity.STATUS_NULL,
    TempEntity.STATUS_ERROR,
    TempEntity.STATUS_DISMISS
)
@kotlin.annotation.Retention
annotation class TempType