package com.rwz.lib_comm.utils.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.rwz.lib_comm.manager.ContextManager
import com.rwz.lib_comm.utils.system.AndroidUtils

/**
 * date： 2020/4/23 15:25
 * author： rwz
 * description：
 **/
object CommTurnHelp {

    private fun checkParams(context: Context?): Boolean {
        return context != null && CheckHelp.checkTurnTime()
    }

    /**
     * 设置-应用详情
     */
    fun setting() {
        val context = ContextManager.context
        if (!checkParams(context))
            return
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package",
            AndroidUtils.getPackageName(context), null)
        intent.data = uri
        context.takeIf { CommUtils.canTurn(context, intent) }?.startActivity(intent)
    }

}