package com.rwz.lib_comm.config

/**
 * date： 2019/11/30 17:48
 * author： rwz
 * description：
 **/

interface Orientation{

    companion object{
       const val LEFT = 0   //左
       const val TOP = 1    //顶
       const val RIGHT = 2  //右
       const val BOTTOM = 4 //底
       const val ALL = 8    //所有方向
       const val NONE = 16  //无方向
    }

}