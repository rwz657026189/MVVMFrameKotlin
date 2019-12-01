package com.rwz.lib_comm.utils.show

import android.text.TextUtils
import android.util.Log
import com.rwz.lib_comm.config.showLog
import java.util.*

/**
 * date： 2019/11/7 16:28
 * author： rwz
 * description：
 **/
object LogUtil {

    var isDebug = showLog

    private val TAG = "rwz"
    private val OkHttp = "OkHttp"
    private val SPACE = "   "

    /**
     * 打印网络请求日志
     */
    fun ok(vararg msg: Any?) {
        if (isDebug) {
            Log.d(OkHttp, msg.contentToString())
        }
    }

    /**
     * 打印普通日志
     */
    fun d(vararg msg: Any?) {
        if (isDebug) {
            Log.d(TAG, msg.contentToString())
        }
    }

    /**
     * 打印错误日志
     */
    fun e(vararg msg: Any?) {
        if (isDebug) {
            Log.e(TAG, msg.contentToString())
        }
    }

    /**
     * 打印json
     */
    fun j(jsonStr: String) {
        if (isDebug) {
            if (TextUtils.isEmpty(jsonStr))
                return

        }
    }

    private fun getText(vararg msg: Any): String {
        if (msg == null)
            return ""
        val sb = StringBuffer()
        for (o in msg) {
            sb.append(",").append(o)
        }
        return sb.toString().substring(1)
    }

    fun stackTraces() {
        stackTraces(15, 3)
    }

    fun stackTraces(methodCount: Int, methodOffset: Int) {
        val trace = Thread.currentThread().stackTrace
        var level = ""
        Log.d(TAG, "$SPACE--------- logStackTraces start ----------")
        for (i in methodCount downTo 1) {
            val stackIndex = i + methodOffset
            if (stackIndex >= trace.size) {
                continue
            }
            val builder = StringBuilder()
            builder.append("|")
                .append(' ')
                .append(level)
                .append(trace[stackIndex].className)
                .append(".")
                .append(trace[stackIndex].methodName)
                .append(" ")
                .append(" (")
                .append(trace[stackIndex].fileName)
                .append(":")
                .append(trace[stackIndex].lineNumber)
                .append(")")
            level += "   "
            Log.d(TAG, SPACE + builder.toString())
        }
        Log.d(TAG, "$SPACE--------- logStackTraces end ----------")
    }

}