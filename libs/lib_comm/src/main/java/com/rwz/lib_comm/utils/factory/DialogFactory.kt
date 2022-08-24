package com.rwz.lib_comm.utils.factory

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.FragmentManager
import com.rwz.lib_comm.R
import com.rwz.lib_comm.base.BaseDialog
import com.rwz.lib_comm.config.PARCELABLE_ENTITY
import com.rwz.lib_comm.entity.turn.MsgDialogTurnEntity
import com.rwz.lib_comm.ui.dialog.MsgDialog
import com.rwz.lib_comm.utils.app.DialogHelp
import com.rwz.lib_comm.utils.app.ResourceUtil
import io.reactivex.functions.BiConsumer

/**
 * date： 2020/4/23 15:02
 * author： rwz
 * description：
 **/
object DialogFactory {

    fun dialog(cls: Class<out BaseDialog<*>>, params: Parcelable) : BaseDialog<*> {
        val bundle = Bundle()
        bundle.putParcelable(PARCELABLE_ENTITY, params)
        val instance = cls.newInstance()
        instance.arguments = bundle
        return instance
    }

    /**
     * 简单的弹窗
     */
    fun simpleMsgDialog(fm: FragmentManager, msg: String, listener: BiConsumer<MsgDialogTurnEntity, Boolean>?) {
        val entity = MsgDialogTurnEntity(title = ResourceUtil.getString(R.string.dialog_def_title),
            msg = msg)
        entity.listener = listener
        val dialog = dialog(MsgDialog::class.java, entity)
        DialogHelp.show(fm, dialog, "MsgDialog")
    }

}