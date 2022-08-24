package com.rwz.lib_comm.base

import android.os.Process
import android.content.Context
import android.os.Looper
import com.rwz.lib_comm.config.isDebug
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * date： 2020/6/28 20:15
 * author： rwz
 * description： 捕获全局异常信息
 *  adb pull /storage/emulated/0/Android/data/com.pingan.numeral/files/logs/ D:\\资料\log\
 **/

class MyUnCatchExceptionHandler internal constructor(mode: Int) : Thread.UncaughtExceptionHandler {

    init {
        if (mode != NO_MESSAGE && mode != NORMAL) {
            MyUnCatchExceptionHandler.mode = NORMAL
        } else {
            MyUnCatchExceptionHandler.mode = mode
        }
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        ex.printStackTrace()
        saveCrashToFile(ex)
        setCrash()
        if (mode == NO_MESSAGE && Looper.myLooper() == Looper.getMainLooper()) {
            Process.killProcess(Process.myPid())
        } else if (mode == NORMAL && mDefaultUncaughtExceptionHandler != null) {
            mDefaultUncaughtExceptionHandler!!.uncaughtException(thread, ex)
        }
    }

    private fun createDir() {
        val externalFiles = mContext!!.getExternalFilesDir(null)
        if (externalFiles != null && externalFiles!!.exists()) {
            dirPath = externalFiles!!.absolutePath + File.separator + dirName
        } else {
            dirPath = mContext!!.filesDir.absolutePath + File.separator + dirName
        }
        val dir = File(dirPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    private fun saveCrashToFile(ex: Throwable) {
        createDir()
        if (dirPath == null) {
            return
        }
        val format = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
        val name = "crash--" + format.format(Calendar.getInstance().time) + ".txt"
        var fileOutputStream: FileOutputStream? = null
        var fileInputStream: FileInputStream? = null
        val temp = File(dirPath + File.separator + "temp")
        if (temp.exists()) {
            temp.delete()
        }
        try {
            val file = File(dirPath + File.separator + name)
            fileOutputStream = FileOutputStream(temp)
            val printWriter = PrintWriter(fileOutputStream)
            ex.printStackTrace(printWriter)
            printWriter.close()
            try {
                fileOutputStream!!.flush()
                fileOutputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            fileInputStream = FileInputStream(temp)
            val bufferedReader = BufferedReader(InputStreamReader(fileInputStream))
            fileOutputStream = FileOutputStream(file)
            val bufferedWriter = BufferedWriter(OutputStreamWriter(fileOutputStream))
            val sb = StringBuffer()
            sb.append("PRODUCT: ").append(android.os.Build.PRODUCT)
                .append("\r\n").append("CPU_ABI：").append(android.os.Build.CPU_ABI)
                .append("\r\n").append("TAGS：").append(android.os.Build.TAGS)
                .append("\r\n").append("VERSION_CODES.BASE：")
                .append(android.os.Build.VERSION_CODES.BASE)
                .append("\r\n").append("MODEL：").append(android.os.Build.MODEL)
                .append("\r\n").append("SDK：").append(android.os.Build.VERSION.SDK)
                .append("\r\n").append("VERSION.RELEASE：").append(android.os.Build.VERSION.RELEASE)
                .append("\r\n").append("DEVICE：").append(android.os.Build.DEVICE)
                .append("\r\n").append("BRAND：").append(android.os.Build.DISPLAY)
                .append("\r\n").append("BOARD：").append(android.os.Build.BRAND)
                .append("\r\n").append("FINGERPRINT：").append(android.os.Build.FINGERPRINT)
                .append("\r\n").append("ID：").append(android.os.Build.ID)
                .append("\r\n").append("MANUFACTURER：").append(android.os.Build.MANUFACTURER)
                .append("\r\n").append("USER：").append(android.os.Build.USER)
                .append("\r\n")
                .append("-------------------------------------------------------------------------------------------------------")
            var bufferString: String? = bufferedReader.readLine()
            while (bufferString != null) {
                sb.append("\r\n").append(bufferString)
                bufferString = bufferedReader.readLine()
            }
            bufferedWriter.write(sb.toString())
            bufferedWriter.close()
            bufferedReader.close()
            fileInputStream!!.close()
            fileOutputStream!!.close()
        } catch (e1: FileNotFoundException) {
            e1.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                temp.delete()
            } catch (e2: Exception) {
            }

        }

    }

    companion object {

        /**
         * 是否开启全局异常捕获
         */
        private val TURN_ON = isDebug
        val NO_MESSAGE = 0
        val NORMAL = 1
        private val locker = "lock"
        private var mContext: Context? = null
        private val dirName = "logs"
        private var dirPath: String? = null

        private var mode: Int = 0

        private var mDefaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null

        fun init(mContext: Context) {
            init(NORMAL, mContext)
        }

        fun init(mode: Int, mContext: Context) {
            if (!TURN_ON) {
                return
            }
            MyUnCatchExceptionHandler.mContext = mContext.applicationContext
            cleanCrash()
            if (mDefaultUncaughtExceptionHandler == null) {
                mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
            }
            Thread.setDefaultUncaughtExceptionHandler(MyUnCatchExceptionHandler(mode))
        }

        private fun setCrash() {
            try {
                val fileOutputStream = mContext!!.openFileOutput(locker, Context.MODE_PRIVATE)
                fileOutputStream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        fun isCrashed(context: Context): Boolean {
            return context.getFileStreamPath(locker).exists()
        }

        private fun cleanCrash() {
            val file = mContext!!.getFileStreamPath(locker)
            file.delete()
        }
    }

}