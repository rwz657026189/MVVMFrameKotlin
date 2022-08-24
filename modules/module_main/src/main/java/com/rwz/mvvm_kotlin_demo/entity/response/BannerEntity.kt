package com.rwz.mvvm_kotlin_demo.entity.response

import com.rwz.lib_comm.entity.response.IBannerEntity

/**
 * date： 2019/12/1 19:13
 * author： rwz
 * description：
 **/
class BannerEntity(override var imgUrl: String,
                   val entity: JokeEntity) :IBannerEntity {
    override fun itemLayoutId(): Int {
        return 0
    }
}