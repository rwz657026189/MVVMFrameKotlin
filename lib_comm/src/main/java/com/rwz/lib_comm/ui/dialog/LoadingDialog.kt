package com.rwz.lib_comm.ui.dialog

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.rwz.lib_comm.R
import com.rwz.lib_comm.base.BaseDialog
import com.rwz.lib_comm.config.PARCELABLE_ENTITY
import com.rwz.lib_comm.databinding.DialogLoadingBinding
import com.rwz.lib_comm.entity.turn.LoadingDialogTurnEntity
import com.rwz.lib_comm.utils.app.ResourceUtil

/**
 * date： 2019/11/18 17:04
 * author： rwz
 * description：加载中对话框
 **/
class LoadingDialog : BaseDialog<DialogLoadingBinding>(), View.OnClickListener {

    var mEntity: LoadingDialogTurnEntity? = null

    private var mTipsView: TextView? = null

    protected override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        isCancelable = false
        mTipsView = mBind.tips
        arguments?.let {
            mEntity = it.getParcelable(PARCELABLE_ENTITY)
            if (mEntity != null)
                setTips(mEntity!!.tips)
        }
        mRootView.setOnClickListener(this)
    }

    /**
     * 设置提示语
     * @param tips
     */
    fun setTips(tips: String?) {
        mTipsView?.text = tips
    }

    fun setEntity(mEntity: LoadingDialogTurnEntity?) {
        this.mEntity = mEntity
        if (mEntity != null) {
            setTips(mEntity.tips)
        }
    }

    fun setTips(@StringRes tips: Int) {
        setTips(ResourceUtil.getString(tips))
    }

    protected override fun setLayoutId(): Int {
        return R.layout.dialog_loading
    }

    override fun onClick(v: View) {
        if (v === mRootView && mEntity != null && mEntity!!.canDismissOutSide) {
            dismissAllowingStateLoss()
        }
    }

    companion object {

        fun newInstance(entity: LoadingDialogTurnEntity): LoadingDialog {
            val bundle = Bundle()
            bundle.putParcelable(PARCELABLE_ENTITY, entity)
            val dialog = LoadingDialog()
            dialog.arguments = bundle
            return dialog
        }
    }
}
