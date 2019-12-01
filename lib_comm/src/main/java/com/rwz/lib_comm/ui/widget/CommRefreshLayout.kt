package com.rwz.lib_comm.ui.widget

import android.content.Context
import android.util.AttributeSet
import com.rwz.lib_comm.manager.ContextManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader

/**
 * date： 2019/11/29 13:35
 * author： rwz
 * description： 参考： https://github.com/scwang90/SmartRefreshLayout/blob/master/art/md_property.md
 **/

class CommRefreshLayout(context: Context?, attrs: AttributeSet?) :
    SmartRefreshLayout(context, attrs) {

    companion object {
        init {
            SmartRefreshLayout.setDefaultRefreshFooterCreator { _, _ ->
                ClassicsFooter(ContextManager.context)
            }
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { _, _ ->
                ClassicsHeader(ContextManager.context)
            }
        }
    }

}