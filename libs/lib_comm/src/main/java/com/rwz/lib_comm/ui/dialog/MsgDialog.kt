package com.rwz.lib_comm.ui.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.rwz.lib_comm.R
import com.rwz.lib_comm.base.BaseDialog
import com.rwz.lib_comm.config.PARCELABLE_ENTITY
import com.rwz.lib_comm.databinding.DialogMsgBinding
import com.rwz.lib_comm.entity.turn.MsgDialogTurnEntity
import io.reactivex.functions.BiConsumer

/**
 * date： 2019/11/18 16:07
 * author： rwz
 * description：
 **/
class MsgDialog : BaseDialog<DialogMsgBinding>(), View.OnClickListener {

    private var mEntity: MsgDialogTurnEntity? = null

    var listener: BiConsumer<MsgDialogTurnEntity, Boolean>? = null

    fun setRequestCode(requestCode: Int) {
        mEntity?.requestCode = requestCode
    }

    fun setEntity(mEntity: MsgDialogTurnEntity) {
        this.mEntity = mEntity
    }

    override fun init(savedInstanceState: Bundle?) {
        mEntity = arguments?.getParcelable(PARCELABLE_ENTITY)
        val cancel = mBinding?.cancel
        val enter = mBinding?.enter
        mEntity?.let {
            mBinding?.entity = it
            if(this.listener == null)
                this.listener = it.listener
            mBinding?.isSingleBtn = TextUtils.isEmpty(it.cancelText)
            isCancelable = it.cancelable

        }
        cancel?.setOnClickListener(this)
        enter?.setOnClickListener(this)
    }

    override fun setLayoutId(): Int {
        return R.layout.dialog_msg
    }

    override fun onClick(v: View) {
        if (v.id == R.id.cancel) {
            onClickDialog(false)
        } else if (v.id == R.id.enter) {
            onClickDialog(true)
        }
        dismiss()
    }

    private fun onClickDialog(isOk: Boolean) {
        try {
            listener?.accept(mEntity, isOk)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        fun newInstance(entity: MsgDialogTurnEntity): MsgDialog {
            val bundle = Bundle()
            bundle.putParcelable(PARCELABLE_ENTITY, entity)
            val dialog = MsgDialog()
            dialog.arguments = bundle
            return dialog
        }
    }
}
