package com.rwz.lib_comm.utils.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.rwz.lib_comm.manager.ContextManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;


/**
 * Created by rwz on 2017/7/25.
 */

public class PermissionHelp {

    //范围只能在0 ~ 65536
    public static int REQUEST_WRITE_SETTING_PERMISSION = 1000;

    public static boolean hasM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * 是否有读写权限
     * @return
     */
    public static boolean hasWritePermission() {
        return !hasM() || ContextCompat.checkSelfPermission(ContextManager.context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 是否有安装app的权限
     */
    public static boolean hasInstallApp() {
        boolean result = true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            result = ContextManager.context.getPackageManager().canRequestPackageInstalls();
        }
        return result;
    }

    /**
     * 是否有录音权限
     * @return
     */
    public static boolean hasRecordPermission() {
        return  !hasM() || ContextCompat.checkSelfPermission(ContextManager.context, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 拍照权限
     * @return
     */
    public static boolean hasCameraPermission() {
        return  !hasM() || ContextCompat.checkSelfPermission(ContextManager.context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 是否用系统设置权限
     */
    public static boolean hasSettingPermission(FragmentActivity aty) {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(aty));
    }

    public static boolean hasMulPermission(String... permission) {
        if (permission == null) {
            return true;
        }
        int length = permission.length;
        for (String aPermission : permission) {
            if (ContextCompat.checkSelfPermission(ContextManager.context, aPermission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static Observable<Boolean> getRequestObservable(Boolean result){
        return Observable.just(result);
    }

    /**
     * 读写权限
     * @param aty
     * @return
     */
    public static Observable<Boolean> requestWrite(FragmentActivity aty) {
        if (aty == null) {
            return getRequestObservable(false);
        } else {
            return new RxPermissions(aty).request(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    /****
     * 申请文件读写、安装app权限
     */
    public static Observable<Boolean> requestWriteAndInstallApp(FragmentActivity aty){
        return requestMulPermissions(aty, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES);
    }

    /**
     * 录音权限
     * @param aty
     * @return
     */
    public static Observable<Boolean> requestRecord(FragmentActivity aty) {
        if (aty == null) {
            return getRequestObservable(false);
        } else {
            return new RxPermissions(aty).request(Manifest.permission.RECORD_AUDIO);
        }
    }

    /**
     * 系统设置（屏幕亮度权限）
     */
    public static void requestSetting(FragmentActivity aty) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + aty.getPackageName()));
        aty.startActivityForResult(intent, REQUEST_WRITE_SETTING_PERMISSION);
    }

    /**
     * 请求多个
     * @param aty
     * @param allPermissions
     * @return
     */
    public static Observable<Boolean> requestMulPermissions(FragmentActivity aty, final String... allPermissions) {
        if (aty == null) {
            return getRequestObservable(false);
        } else {
            return new RxPermissions(aty).request(allPermissions);
        }
    }



    /**
     * 访问照相机
     * @param aty
     * @return
     */
    public static Observable<Boolean> requestCamera(FragmentActivity aty) {
        if (hasM()) {
            if (aty == null) {
                return getRequestObservable(false);
            } else {
                RxPermissions permissions = new RxPermissions(aty);
                return permissions.request(Manifest.permission.CAMERA);
            }
        } else {
            return getRequestObservable(true);
        }
    }

    /**
     * 请求安装app
     */
    public static Observable<Boolean> requestInstallApp(FragmentActivity aty) {
        if (hasO()) {
            if (aty == null) {
                return getRequestObservable(false);
            } else {
                RxPermissions permissions = new RxPermissions(aty);
                return permissions.request(Manifest.permission.REQUEST_INSTALL_PACKAGES);
            }
        } else {
            return getRequestObservable(true);
        }
    }

    /**
     * 获取相机和文件读写权限
     * @param aty
     * @return
     */
    public static Observable<Boolean> requestCameraAndWrite(FragmentActivity aty) {
        return requestMulPermissions(aty, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    /** 检查是否开启了通知栏权限 **/
    public static boolean hasNotificationPermission() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(ContextManager.context);
        return manager.areNotificationsEnabled();
    }


}
