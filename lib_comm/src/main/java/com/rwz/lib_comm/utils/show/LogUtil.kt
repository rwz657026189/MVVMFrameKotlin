package com.rwz.lib_comm.utils.show

import android.text.TextUtils
import android.util.Log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.rwz.lib_comm.config.showLog

/**
 * date： 2019/11/7 16:28
 * author： rwz
 * description：
 **/
object LogUtil {

    private var isDebug = showLog

    private const val TAG = "rwz"
    private const val OkHttp = "OkHttp"
    private const val SPACE = "   "

    init {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
            .methodCount(1)         // (Optional) How many method line to show. Default 2
            .methodOffset(1)        // (Optional) Hides internal method calls up to offset. Default 5
            .tag(TAG)               // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    /**
     * 打印网络请求日志
     */
    fun ok(vararg msg: Any?) {
        if (isDebug) {
            Logger.t(OkHttp).d(getText(msg))
        }
    }

    /**
     * 打印普通日志
     */
    fun d(vararg msg: Any?) {
        if (isDebug) {
            Logger.t(TAG).d(getText(msg))
        }
    }

    /**
     * 打印错误日志
     */
    fun e(vararg msg: Any?) {
        if (isDebug) {
            Logger.t(TAG).e(getText(msg))
        }
    }

    /**
     * 打印json
     */
    fun j(tag: String = OkHttp, jsonStr: String) {
        if (isDebug) {
            if (TextUtils.isEmpty(jsonStr))
                return
            Logger.t(tag).json(jsonStr)
        }
    }

    private fun getText(vararg msg: Any): String {
        val sb = StringBuffer()
        for (o in msg) {
            sb.append(",").append(o)
        }
        return sb.toString().substring(1)
    }

    fun stackTraces(methodCount: Int = 15, methodOffset: Int = 3) {
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