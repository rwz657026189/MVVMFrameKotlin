package com.rwz.mvvm_kotlin_demo.entity

import com.rwz.lib_comm.entity.response.BaseListEntity
import com.rwz.mvvm_kotlin_demo.R

/**
 * date： 2019/11/30 18:12
 * author： rwz
 * description：
 **/

data class TestEntity(
    var title: String,
    var url: String
): BaseListEntity() {
    override val itemLayoutId: Int = R.layout.item_main_test
}