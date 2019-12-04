package com.rwz.lib_comm.config

import android.os.Environment
import com.rwz.lib_comm.manager.ContextManager
import com.rwz.lib_comm.utils.system.AndroidUtils

/**
 * date： 2019/12/4 15:55
 * author： rwz
 * description：
 **/
interface Path {

    /** 外置目录  */
    interface External {
        companion object {
            //外置SD卡目录
            val BASE_DIR =
                Environment.getExternalStorageDirectory().path + SEPARATOR + AndroidUtils.getPackageName(
                    ContextManager.context
                ) + SEPARATOR
            //外置SD卡临时目录
            val TEMP_DIR = BASE_DIR + "temp" + SEPARATOR
            //外置SD卡缓存目录
            val CACHE_DIR = BASE_DIR + "cache" + SEPARATOR
            //拍照的图片
            val TEMP_TAKE_PHOTO_AVATAR = TEMP_DIR + "take_photo_avatar.png"
            //头像待上传缓存文件
            val TEMP_HEAD_IMG_PATH = TEMP_DIR + "avatar.png"
            //外置SD卡下载目录
            val DOWNLOAD_DIR = BASE_DIR + "download" + SEPARATOR
            //apk下载外置目录
            val APK_DIR = DOWNLOAD_DIR
            //外置SD卡下载图片目录
            val IMG_DIR = DOWNLOAD_DIR + "图片" + SEPARATOR
            //apk下载文件名、版本升级apk name
            val NEW_APK_NAME = AndroidUtils.getAppName(ContextManager.context) + ".apk"
            //知乎拍照路径
            val PICTURES =
                Environment.getExternalStorageDirectory().path + SEPARATOR + "Pictures" + SEPARATOR
        }
    }

    /** 内置目录  */
    interface Inner {
        companion object {
            //内置文件目录
            val BASE_DIR = ContextManager.context.getFilesDir().getAbsolutePath()
            //内置缓存目录
            val CACHE_DIR = ContextManager.context.getCacheDir().getAbsolutePath()
            //内置下载目录
            val DOWNLOAD_DIR = BASE_DIR + SEPARATOR + "download" + SEPARATOR
        }
    }

    companion object {
        const val SEPARATOR = "/"
    }

}