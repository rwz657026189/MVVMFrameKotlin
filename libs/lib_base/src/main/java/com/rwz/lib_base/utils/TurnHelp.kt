package com.rwz.lib_base.utils

import android.content.Context
import android.content.Intent
import com.rwz.lib_base.ui.activity.CommWebActivity
import com.rwz.lib_comm.config.PARCELABLE_ENTITY
import com.rwz.lib_comm.entity.params.WebEntity
import com.rwz.lib_comm.utils.app.CheckHelp

/**
 * date： 2019/12/6 16:07
 * author： rwz
 * description：
 **/
object TurnHelp {

    private fun checkParams(context: Context?): Boolean {
        return context != null && CheckHelp.checkTurnTime()
    }

    fun h5(context: Context, url: String) {
        if (!checkParams(context))
            return
        val intent = Intent(context, CommWebActivity::class.java)
        val entity = WebEntity(url)
        intent.putExtra(PARCELABLE_ENTITY, entity)
        context.startActivity(intent)
    }

}