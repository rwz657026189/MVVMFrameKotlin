package com.rwz.lib_comm.utils.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.rwz.lib_comm.R;
import com.rwz.lib_comm.manager.ContextManager;
import com.rwz.lib_comm.utils.factory.DialogFactory;
import com.rwz.lib_comm.utils.show.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;


/**
 * Created by rwz on 2017/7/25.
 */

public class PermissionHelp {

    //范围只能在0 ~ 65536
    public static int REQUEST_WRITE_SETTING_PERMISSION = 1000;

    private static final Map<String, String> PERMISSIONS = new HashMap<>();

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            PERMISSIONS.put(Manifest.permission.READ_EXTERNAL_STORAGE, ResourceUtil.getString(R.string.pm_wr_external_storage));
        }
        PERMISSIONS.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, ResourceUtil.getString(R.string.pm_wr_external_storage));
        PERMISSIONS.put(Manifest.permission.CAMERA, ResourceUtil.getString(R.string.pm_camera));
        PERMISSIONS.put(Manifest.permission.ACCESS_FINE_LOCATION, ResourceUtil.getString(R.string.pm_location));
        PERMISSIONS.put(Manifest.permission.ACCESS_COARSE_LOCATION, ResourceUtil.getString(R.string.pm_location));
        PERMISSIONS.put(Manifest.permission.READ_PHONE_STATE, ResourceUtil.getString(R.string.pm_read_contacts));
        PERMISSIONS.put(Manifest.permission.READ_CONTACTS, ResourceUtil.getString(R.string.pm_read_contacts));
        PERMISSIONS.put(Manifest.permission.RECORD_AUDIO, ResourceUtil.getString(R.string.pm_record_audio));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PERMISSIONS.put(Manifest.permission.REQUEST_INSTALL_PACKAGES, ResourceUtil.getString(R.string.pm_request_install_packages));
        }
    }

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

    /**
     * 读写权限
     * @param aty
     * @return
     */
    public static Single<Boolean> requestWrite(FragmentActivity aty) {
        return requestMulPermissions(aty, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /****
     * 申请文件读写、安装app权限
     */
    public static Single<Boolean> requestWriteAndInstallApp(FragmentActivity aty){
        return requestMulPermissions(aty, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES);
    }

    /**
     * 录音权限
     * @param aty
     * @return
     */
    public static Single<Boolean> requestRecord(FragmentActivity aty) {
        return requestMulPermissions(aty, Manifest.permission.RECORD_AUDIO);
    }

    /**
     * 系统设置（屏幕亮度权限）
     */
    public static void requestSetting(FragmentActivity aty) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + aty.getPackageName()));
            if(CommUtils.canTurn(aty, intent))
                aty.startActivityForResult(intent, REQUEST_WRITE_SETTING_PERMISSION);
        }
    }

    /**
     * 请求多个
     */
    public static Single<Boolean> requestMulPermissions(FragmentActivity aty, final String... allPermissions) {
        if (hasM()) {
            return Single.just(true);
        } else if (aty == null) {
            return Single.just(false);
        } else {
            return new RxPermissions(aty).requestEach(allPermissions)
                    .toList()
                    .map(permissions -> {
                        List<String> allowRequestAgain = new ArrayList<>();
                        List<String> forbidRequest = new ArrayList<>();
                        for (Permission permission : permissions) {
                            if (permission.granted) continue;
                            if (permission.shouldShowRequestPermissionRationale)
                                allowRequestAgain.add(permission.name);
                            else
                                forbidRequest.add(permission.name);
                        }
                        if (!allowRequestAgain.isEmpty()) {
                            ToastUtil.INSTANCE.showShort(getTips(allowRequestAgain, false));
                        } else if (!forbidRequest.isEmpty()) {
                            DialogFactory.INSTANCE.simpleMsgDialog(aty.getSupportFragmentManager(),
                                    getTips(forbidRequest, true),
                                    (msgDialogTurnEntity, result) -> {
                                        if (result) {
                                            CommTurnHelp.INSTANCE.setting();
                                        }
                                    });
                        }
                        return allowRequestAgain.isEmpty() && forbidRequest.isEmpty();
                    });
        }
    }

    private static String getTips(List<String> permissions, boolean isForbid) {
        String tips = ResourceUtil.getString(isForbid ? R.string.pm_forbid_tips : R.string.pm_refuse_tips);
        String permissionName = Observable.fromIterable(permissions)
                .map(s -> PERMISSIONS.get(s) == null ? "" : PERMISSIONS.get(s))
                .distinct()
                .collectInto(new StringBuffer(), (stringBuffer, s) -> {
                    if (!TextUtils.isEmpty(s))
                        stringBuffer.append("、").append(s);
                })
                .map(s -> s.length() > 0 ? s.substring(1) : "")
                .blockingGet();
        return String.format(tips, permissionName);

    }

    /**
     * 访问照相机
     * @param aty
     * @return
     */
    public static Single<Boolean> requestCamera(FragmentActivity aty) {
        return requestMulPermissions(aty, Manifest.permission.CAMERA);
    }

    /**
     * 请求安装app
     */
    public static Single<Boolean> requestInstallApp(FragmentActivity aty) {
        if (hasO()) {
            return requestMulPermissions(aty, Manifest.permission.REQUEST_INSTALL_PACKAGES);
        } else {
            return Single.just(true);
        }
    }

    /**
     * 获取相机和文件读写权限
     * @param aty
     * @return
     */
    public static Single<Boolean> requestCameraAndWrite(FragmentActivity aty) {
        return requestMulPermissions(aty, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    /** 检查是否开启了通知栏权限 **/
    public static boolean hasNotificationPermission() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(ContextManager.context);
        return manager.areNotificationsEnabled();
    }


}
