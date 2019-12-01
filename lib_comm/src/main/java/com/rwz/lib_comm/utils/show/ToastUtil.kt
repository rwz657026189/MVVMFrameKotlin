package com.rwz.lib_comm.utils.show

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.StringRes
import com.rwz.lib_comm.manager.ContextManager
import com.rwz.lib_comm.utils.ThreadUtil
import com.rwz.lib_comm.utils.app.ResourceUtil

/**
 * date： 2019/11/7 16:32
 * author： rwz
 * description：
 **/
object ToastUtil {

    private val mToast: Toast by lazy { Toast.makeText(ContextManager.context, "", Toast.LENGTH_SHORT) }

    fun showShortSingle(@StringRes stringRes: Int) {
        showShortSingle(ResourceUtil.getString(stringRes))
    }

    fun showShortSingle(string: String) {
        if (TextUtils.isEmpty(string)) {
            return
        }
        if (ThreadUtil.isMainThread()) {
            mToast.setText(string)
            mToast.show()
        } else {
            Handler(Looper.getMainLooper()).post {
                mToast.setText(string)
                //已在主线程中，可以更新UI
                mToast.show()
            }
        }
    }

    /**
     * 一定在主线程显示
     * @param stringRes
     */
    fun showShort(@StringRes stringRes: Int) {
        if (ThreadUtil.isMainThread()) {
            showText(ResourceUtil.getString(stringRes), false)
        } else {
            showTextOnBackgroundThread(ResourceUtil.getString(stringRes), false)
        }
    }

    fun showLong(@StringRes stringRes: Int) {
        if (ThreadUtil.isMainThread()) {
            showText(ResourceUtil.getString(stringRes), true)
        } else {
            showTextOnBackgroundThread(ResourceUtil.getString(stringRes), true)
        }
    }

    fun showShort(text: String?) {
        if (TextUtils.isEmpty(text)) {
            return
        }
        if (ThreadUtil.isMainThread()) {
            showText(text!!, false)
        } else {
            showTextOnBackgroundThread(text!!, false)
        }
    }

    fun showLong(text: String) {
        if (TextUtils.isEmpty(text)) {
            return
        }
        if (ThreadUtil.isMainThread()) {
            showText(text, true)
        } else {
            showTextOnBackgroundThread(text, true)
        }
    }


    private fun showTextOnBackgroundThread(text: String, isLong: Boolean) {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post {
            //已在主线程中，可以更新UI
            showText(text, isLong)
        }
    }

    private fun showText(text: String, isLong: Boolean) {
        if (isLong) {
            Toast.makeText(ContextManager.context, text, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(ContextManager.context, text, Toast.LENGTH_SHORT).show()
        }
    }

}