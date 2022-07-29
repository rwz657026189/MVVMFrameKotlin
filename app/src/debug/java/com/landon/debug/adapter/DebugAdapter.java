package com.landon.debug.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.landon.debug.DebugManager;
import com.landon.debug.inf.InfParse;
import com.landon.debug.utils.LogUtil;
import com.landon.debug.utils.ReflectionUtils;
import com.rwz.app.R;

import okhttp3.OkHttpClient;

/**
 * @Author Ren Wenzhang
 * @Date 2022/6/24/024 20:14
 * @Description
 */
public class DebugAdapter {

    public static void perform(Activity activity) {
//        DebugManager.getInstance()
//                .putUrl("debug", "http://gtxt.vrtbbs.com/")
//                .putUrl("pre", "https://api-master.cdqlkj.cn/")
//                .putUrl("release", "https://api-prod.cdqlkj.cn/")
//                .initUrl(NetConfig.INSTANCE::resetBaseUrl)
////                .registerMock(new CommMock(AppUrlConfig.GET_USER_IN_DEPT))
////                .registerTimeOut(ProUrlConfig.QUALITY_TASK_LIST)
////                .setTimeOutSecond(5)
//                .putMockImageUrl("file/2022/07/1656644381689.webp")
//                .putMockImageUrl("file/2022/07/1656644382354.jpg")
//                .putMockImageUrl("file/2022/07/16566443811038.webp")
//                .putMockImageUrl("file/2022/07/1656644381348.webp")
//                .putMockImageUrl("file/2022/07/1656644381236.jpg")
//                .parseOkHttpClient(DebugAdapter::parseOkHttpClient)
//                .observerActivity(
//                        "com.yupao.user_center.system_setting.view.UserCenterSystemSettingActivity",
//                        aty -> showChooseDomain(aty, aty.findViewById(R.id.tvTitle)))
//                .observerActivity("com.yupao.saas.login.login_by_authcode.view.LoginByAuthCodeActivity",
//                        aty -> {
//                            ViewGroup containerView = (ViewGroup) aty.findViewById(R.id.llRoot);
//                            showChooseDomain(aty, containerView.getChildAt(containerView.getChildCount() - 1));
//                        })
//                .turnLauncher(activity, LaunchActivity.class);
    }

    public static void parseOkHttpClient(InfParse<OkHttpClient> infParse) {
        try {
            Class apiCls = Class.forName("com.yupao.net.core.ApiFactory");
            ReflectionUtils.printAll(apiCls);
            Object companion = ReflectionUtils.getObj(apiCls, null, "Companion");
            Object apiFactory = ReflectionUtils.performMethod(companion, "getInstance");
            Object client = ReflectionUtils.performMethod(apiFactory, "getOkHttpClient");
            if (client instanceof OkHttpClient) {
                OkHttpClient okHttpClient = infParse.trans((OkHttpClient) client);
                Object lazy = ReflectionUtils.getObj(apiFactory, "okHttpClient$delegate");
                ReflectionUtils.setField(lazy, "_value", okHttpClient);
                LogUtil.debug("TAG", "perform succeed " + client);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("TAG", "inject fail");
        }
    }

    private static void showChooseDomain(Activity activity, View targetView) {
        targetView.setOnLongClickListener(view -> {
            DebugManager.getInstance().showChooseUrl(activity, "", null);
            return true;
        });
    }
}
