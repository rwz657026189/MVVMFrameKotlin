package com.landon.debug.adapter;

import android.app.Activity;

import com.landon.debug.DebugManager;
import com.rwz.mvvm_kotlin_demo.ui.activity.MainActivity;

/**
 * @Author Ren Wenzhang
 * @Date 2022/6/24/024 20:14
 * @Description
 */
public class DebugAdapter {

    public static void perform(Activity activity) {
        DebugManager.getInstance()
                .putUrl("debug", "http://gtxt.vrtbbs.com/")
                .putUrl("pre", "https://api-master.cdqlkj.cn/")
                .putUrl("release", "https://api-prod.cdqlkj.cn/")
//                .initUrl(NetConfig.INSTANCE::resetBaseUrl)
//                .setTimeOutSecond(2)
//                .registerTimeOut("jlist/resumes/resumesFilterList")
//                .registerMock(new CommMock("user/prime/getPrimeItem"))
//                .registerTimeOut(ProUrlConfig.QUALITY_TASK_LIST)
//                .setTimeOutSecond(5)
                .putMockImageUrl("file/2022/07/1656644381689.webp")
                .putMockImageUrl("file/2022/07/1656644382354.jpg")
                .putMockImageUrl("file/2022/07/16566443811038.webp")
                .putMockImageUrl("file/2022/07/1656644381348.webp")
                .putMockImageUrl("file/2022/07/1656644381236.jpg")
//                .parseOkHttpClient(DebugAdapter::parseOkHttpClient)
//                .parseOkHttpClient(DebugAdapter::parseOkHttpClient2)
                .turnLauncher(activity, MainActivity.class);
    }

//    public static void parseOkHttpClient(InfParse<OkHttpClient> infParse) {
//        try {
//            Class apiCls = Class.forName("com.yupao.net.core.ApiFactory");
//            ReflectionUtils.printAll(apiCls);
//            Object companion = ReflectionUtils.getObj(apiCls, null, "Companion");
//            Object apiFactory = ReflectionUtils.performMethod(companion, "getInstance");
//            Object client = ReflectionUtils.performMethod(apiFactory, "getOkHttpClient");
//            if (client instanceof OkHttpClient) {
//                OkHttpClient okHttpClient = infParse.trans((OkHttpClient) client);
//                Object lazy = ReflectionUtils.getObj(apiFactory, "okHttpClient$delegate");
//                ReflectionUtils.setField(lazy, "_value", okHttpClient);
//                LogUtil.debug("TAG", "perform succeed " + client);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.error("TAG", "inject fail");
//        }
//    }
//    public static void parseOkHttpClient2(InfParse<OkHttpClient> infParse) {
//        try {
//            RecruitmentClient recruitmentClient = RecruitmentClient.INSTANCE;
//            Object client = ReflectionUtils.performMethod(recruitmentClient, "getOkHttpClient");
//            if (client instanceof OkHttpClient) {
//                OkHttpClient okHttpClient = infParse.trans((OkHttpClient) client);
//                Object lazy = ReflectionUtils.getObj(recruitmentClient, "okHttpClient$delegate");
//                ReflectionUtils.setField(lazy, "_value", okHttpClient);
//                LogUtil.debug("TAG", "perform succeed " + client);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.error("TAG", "inject fail");
//        }
//    }
//
//    private static void showChooseDomain(Activity activity, View targetView) {
//        targetView.setOnLongClickListener(view -> {
//            DebugManager.getInstance().showChooseUrl(activity, "", null);
//            return true;
//        });
//    }
}
