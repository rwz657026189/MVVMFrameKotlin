package com.rwz.mvvm_kotlin_demo.duplicated

import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.listener.OnItemClickListener
import com.rwz.lib_comm.base.BaseListViewModule
import com.rwz.lib_comm.bindingadapter.BannerViewAdapter
import com.rwz.lib_comm.entity.extension.wrap.WrapList
import com.rwz.lib_comm.ui.adapter.rv.mul.decorator.BaseDecorator
import com.rwz.lib_comm.ui.adapter.rv.mul.decorator.BaseViewHolder
import com.rwz.lib_comm.ui.widget.CommonBanner
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.entity.response.BannerEntity

/**
 * date： 2019/12/16 17:29
 * author： rwz
 * description：
 **/
class BannerDecorator(val viewModule: BaseListViewModule<*>) : BaseDecorator<WrapList<BannerEntity>>(),
    OnItemClickListener {

    private var wrapList: WrapList<BannerEntity>? = null

    override fun onItemClick(position: Int) {
        viewModule.onClickView(R.id.banner, wrapList?.list?.getOrNull(position))
    }

    override val viewType = R.layout.item_banner

    override fun onBindViewHolder(holder: BaseViewHolder, data: WrapList<BannerEntity>, position: Int) {
        this.wrapList = data
        val banner = holder.getView<CommonBanner>(R.id.banner)
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        banner.setPages({ BannerViewAdapter.LocalImageHolderView() }, data.list as List<Any>?)
            //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
            .setPageIndicator(
                intArrayOf(
                    com.rwz.lib_comm.R.drawable.radius_art,
                    com.rwz.lib_comm.R.drawable.radius_primary
                )
            )
            //设置指示器的方向
            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
            .setOnItemClickListener(this)
    }

}