package com.rwz.lib_comm.ui.adapter.rv.mul.decorator

import android.view.View
import com.rwz.lib_comm.R
import com.rwz.lib_comm.base.BaseListViewModule
import com.rwz.lib_comm.entity.extension.TempEntity
import com.rwz.lib_comm.utils.show.LogUtil

/**
 * date： 2019/12/16 17:02
 * author： rwz
 * description：空视图
 **/
class TempDecorator(private val viewModule: BaseListViewModule<*>,
                    private val dp: DecoratorProvide) : BaseDecorator<TempEntity>(), View.OnClickListener {

    override val viewType: Int = R.layout.layout_temp
    var entity: TempEntity? = null
    var position: Int = -1

    override fun onBindViewHolder(holder: BaseViewHolder, data: TempEntity, position: Int) {
        this.entity = data
        this.position = position
        val type = data.type.get()
        val isNullState = type == TempEntity.STATUS_NULL
        holder.setVisible(R.id.layout_temp, type != TempEntity.STATUS_DISMISS)
            .setVisible(R.id.loading_container, type == TempEntity.STATUS_LOADING)
            .setVisible(R.id.error_container, type != TempEntity.STATUS_LOADING)
            .setText(R.id.text, if (isNullState) data.nullTipsStr else data.errorTipsStr)
            .setImageResource(R.id.img, if(isNullState) data.nullImgRes else data.errorImgRes)
            .setText(R.id.reload, if(isNullState) data.nullBtnStr else data.errorBtnStr)
            .setVisible(R.id.reload, data.getShowErrorBtn(type) || data.getShowNullBtn(type))
        holder.getView<View>(R.id.reload).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        viewModule.onClickView(v.id, entity)
        dp.mAdapter?.notifyDataSetChanged()
    }

}