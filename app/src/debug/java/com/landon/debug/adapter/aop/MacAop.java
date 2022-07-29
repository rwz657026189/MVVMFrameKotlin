package com.landon.debug.adapter.aop;//package com.landon.debug.adapter.aop;
//
//import com.landon.debug.utils.LogUtil;
//
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//
///**
// * @Author Ren Wenzhang
// * @Date 2022/7/7/007 10:57
// * @Description
// */
//@Aspect
//public class MacAop {
//
//    @After("execution(* android.net.wifi.WifiInfo.getMacAddress())")
//    public void after() {
//        LogUtil.debug("MacAop, getMacAddress - after");
//    }
//
//
//    @After("execution(* com.yupao.saas.main.MainTabActivity.initJPush())")
//    public void afterInitJPush() {
//        LogUtil.debug("MacAop, initJPush - after");
//    }
//}
