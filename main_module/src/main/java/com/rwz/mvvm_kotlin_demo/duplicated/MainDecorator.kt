package com.rwz.mvvm_kotlin_demo.duplicated

import com.rwz.lib_comm.config.MAIN_HOST
import com.rwz.lib_comm.ui.adapter.rv.mul.decorator.BaseDecorator
import com.rwz.lib_comm.ui.adapter.rv.mul.decorator.BaseViewHolder
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.entity.response.JokeEntity

/**
 * date： 2019/12/16 15:51
 * author： rwz
 * description：
 **/
class MainDecorator : BaseDecorator<JokeEntity>() {

    override val viewType: Int = R.layout.item_main_duplicated

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        data: JokeEntity,
        position: Int
    ) {
        holder.setText(R.id.title, data.title)
            .setText(R.id.time, data.timeStr)
            .setImageUrl(R.id.avatar, MAIN_HOST + data.membericon)
            .setImageUrl(R.id.img, MAIN_HOST + data.bigimg)
    }

}