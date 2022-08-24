package com.rwz.lib_comm.entity.extension

/**
 * date： 2019/11/30 16:42
 * author： rwz
 * description：
 **/
data class TabEntity(
    //决定请求哪页数据（推荐、最热、评论最多或其他……）
    var id: Int = 0,
    //标题
    var title:String,
    //viewpager中的位置
    var position:Int = 0,
    //图标
    var imgUrl:String? = null,
    //类型
    var type:Int = 0
)