package com.rwz.lib_comm.base.proxy

import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.rwz.lib_comm.entity.turn.LoadingDialogTurnEntity
import com.rwz.lib_comm.entity.turn.MsgDialogTurnEntity
import com.rwz.lib_comm.ui.dialog.LoadingDialog
import com.rwz.lib_comm.ui.dialog.MsgDialog
import com.rwz.lib_comm.utils.app.DialogHelp
import com.rwz.lib_comm.utils.factory.FragmentFactory
import com.rwz.lib_comm.utils.app.ResourceUtil
import io.reactivex.functions.BiConsumer

/**
 * date： 2019/11/29 12:04
 * author： rwz
 * description：消息dialog、loading dialog
 **/
class DialogProxy(private var fm: FragmentManager,
                  onClickDialogEnter: (MsgDialogTurnEntity) -> Unit,
                  onClickDialogCancel: (MsgDialogTurnEntity) -> Unit){

    companion object{
        private const val DEF_MSG_REQUEST_CODE = -1//dialog标示符
    }

    //普通对话框
    private var mMsgDialog: MsgDialog? = null
    //加载中对话框
    private var mLoadDialog: LoadingDialog? = null

    private val mDialogListener =
        BiConsumer<MsgDialogTurnEntity, Boolean> { entity, isClickEnter ->
            if (isClickEnter) {
                onClickDialogEnter(entity)
            } else {
                onClickDialogCancel(entity)
            }
        }

    fun showMsgDialog(entity: MsgDialogTurnEntity) {
        mMsgDialog = MsgDialog.newInstance(entity)
        mMsgDialog?.listener = (entity.listener ?: mDialogListener)
        DialogHelp.show(fm, mMsgDialog, "mMsgDialog")
    }

    fun showLoadDialog(entity: LoadingDialogTurnEntity) {
        if (mLoadDialog == null) {
            mLoadDialog = FragmentFactory.newDialog(LoadingDialog::class.java, entity)
        } else {
            mLoadDialog!!.setEntity(entity)
        }
        DialogHelp.show(fm, mLoadDialog, "mLoadDialog")
    }

    fun dismissLoadDialog() = mLoadDialog?.dismissAllowingStateLoss()

    /**
     * 显示消息对话框
     */
    fun showMsgDialog(@StringRes title: Int, @StringRes msg: Int) {
        showMsgDialog(
            MsgDialogTurnEntity(
                ResourceUtil.getString(title),
                ResourceUtil.getString(msg), requestCode = DEF_MSG_REQUEST_CODE
            )
        )
    }

    fun showMsgDialog(title: String, msg: String) {
        showMsgDialog(MsgDialogTurnEntity(title, msg, requestCode = DEF_MSG_REQUEST_CODE))
    }

    fun showMsgDialog(@StringRes title: Int, @StringRes msg: Int, requestCode: Int) {
        showMsgDialog(
            MsgDialogTurnEntity(
                ResourceUtil.getString(title),
                ResourceUtil.getString(msg),
                requestCode = requestCode
            )
        )
    }
}
