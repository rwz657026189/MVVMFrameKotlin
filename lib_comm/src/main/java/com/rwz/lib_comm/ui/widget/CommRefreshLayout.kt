package com.rwz.lib_comm.ui.widget

import android.content.Context
import android.util.AttributeSet
import com.rwz.lib_comm.manager.ContextManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.SmartRefreshLayout.setDefaultRefreshFooterCreator
import com.scwang.smartrefresh.layout.SmartRefreshLayout.setDefaultRefreshHeaderCreator
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader

/**
 * date： 2019/11/29 13:35
 * author： rwz
 * description： 参考： https://github.com/scwang90/SmartRefreshLayout/blob/master/art/md_property.md
 **/
class CommRefreshLayout @JvmOverloads constructor(context: Context,
                                                  attrs: AttributeSet? = null) :
    SmartRefreshLayout(context, attrs) {

    companion object {
        init {
            setDefaultRefreshFooterCreator { _, _ ->
                ClassicsFooter(ContextManager.context)
            }
            setDefaultRefreshHeaderCreator { _, _ ->
                ClassicsHeader(ContextManager.context)
            }
        }
    }

}