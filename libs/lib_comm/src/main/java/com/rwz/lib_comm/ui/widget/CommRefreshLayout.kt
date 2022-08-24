package com.rwz.lib_comm.ui.widget

import android.content.Context
import android.util.AttributeSet
import com.rwz.lib_comm.manager.ContextManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

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