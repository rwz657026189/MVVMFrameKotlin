package com.rwz.lib_comm.config

/**
 * date： 2019/11/7 16:13
 * author： rwz
 * description：
 **/

//是否debug模式 很重要【发布一定置为false】否则影响性能
const val isDebug = true
//本地
const val LOCAL_HTTP = "https://www.baidu.com/"
//线上
const val ONLINE_HTTP = "https://www.baidu.com/"
//主机地址
const val MAIN_HOST = ONLINE_HTTP

//*****************************************************************************************************//
//是否显示log日志
const val showLog = isDebug
//是否允许开启开发者选框
const val ALLOW_OPEN_DEV_DIALOG = isDebug
//关闭/开启统计(所有统计及其相关都将关闭/开启， 新项目、新版本建议关闭)
const val OPEN_STATISTICS = true
//显示错误网络请求信息
const val SHOW_NET_ERROR = isDebug
//双击退出程序时间间隔
const val EXIT_APP_DOUBLE_CLICK_TIME = 2000
//欢迎界面展示时间
const val LAUNCH_TIME = 3000
//一键回顶,是否平滑滚动列表
const val isSmoothScrollList = false
//头信息
const val APP_ID = ""
//默认渠道
const val DEFAULT_CHANNEL = "play.google.com"
//头信息
const val APP_KEY = ""
//本地加密
const val SEED = "@!*&$123456789"
//TalkingData appID
const val TALKING_DATA_APP_ID = ""