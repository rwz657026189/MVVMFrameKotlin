package com.rwz.lib_comm.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rwz.lib_comm.R
import com.rwz.lib_comm.abs.IListView
import com.rwz.lib_comm.abs.ViewListUpdateType
import com.rwz.lib_comm.config.isSmoothScrollList
import com.rwz.lib_comm.entity.response.BaseListEntity
import com.rwz.lib_comm.extension.TAG
import com.rwz.lib_comm.ui.adapter.rv.BaseBindingAdapter
import com.rwz.lib_comm.ui.adapter.rv.mul.decorator.BaseDecoratorProvide
import com.rwz.lib_comm.ui.adapter.rv.mul.decorator.DataBindingDecoratorProvide
import com.rwz.lib_comm.ui.widget.CommRefreshLayout
import com.rwz.lib_comm.ui.widget.CommonRecyclerView
import com.rwz.lib_comm.ui.widget.SafeLinearLayoutManager
import com.rwz.lib_comm.ui.widget.decoration.GridItemSpDecoration
import com.rwz.lib_comm.utils.ThreadUtil
import com.rwz.lib_comm.utils.app.ResourceUtil
import com.rwz.lib_comm.utils.show.LogUtil
import io.reactivex.functions.Consumer

/**
 * date： 2019/11/29 16:26
 * author： rwz
 * description：
 **/
abstract class BaseListFragment<VB : ViewDataBinding,
        VM : BaseListViewModule<IListView>> :
    BaseFragment<VB, VM>(), IListView {
    lateinit var mList: CommonRecyclerView
    protected var mRefreshLayout: CommRefreshLayout? = null

    open var mSpanCount = 1 //每行条目数
    lateinit var mAdapter: BaseBindingAdapter
    protected var isMulSpanCount = false//列数是否固定

    protected val itemDecoration: RecyclerView.ItemDecoration?
        get() =
            GridItemSpDecoration(
                ResourceUtil.getDimen(R.dimen.activity_horizontal_margin),
                mViewModule?.mDecorationList,0)

    private val itemClickCommand = Consumer<Int> { position ->
        mViewModule?.onItemClick(position)
    }

    private val itemLongClickCommand = Consumer<Int> { position ->
        mViewModule?.onItemClick(position)
    }

    /**
     * item 采用的viewModule
     * @return
     */
    protected open val itemViewModule: VM? = mViewModule

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mList = mRootView.findViewById(R.id.list)
        mRefreshLayout = mRootView.findViewById(R.id.refresh_layout)
        if (mSpanCount > 1) {
            val manager = GridLayoutManager(context, mSpanCount)
            setSpanCount(manager)
            mList.layoutManager = manager
            val itemDecoration = itemDecoration
            if (itemDecoration != null)
                mList.addItemDecoration(itemDecoration)
        } else if (mSpanCount == 1) {
            mList.layoutManager = SafeLinearLayoutManager(context = context!!)
        } else
            return
        if (mViewModule == null) {
            return
        }
        val decoratorProvide = setDecoratorProvide()
        mAdapter = BaseBindingAdapter(context!!, mViewModule?.mData as MutableList<Any>, decoratorProvide)
        decoratorProvide.mAdapter = mAdapter
        mAdapter.onClickCommand = itemClickCommand
        mList.adapter = mAdapter
        mRefreshLayout?.let {
            it.setPrimaryColorsId(R.color.page_bg, R.color.text_art_color)
            it.setDragRate(0.5f)//显示下拉高度/手指真实下拉高度=阻尼效果
            it.setReboundDuration(300)//回弹动画时长（毫秒）
            it.setOnRefreshLoadMoreListener(mViewModule)
            it.setOnLoadMoreListener(mViewModule)
        }
        setPullRefreshEnabled(false)
        setLoadingMoreEnabled(false)
    }

    override fun setLayoutId(): Int = R.layout.layout_recyclerview

    open fun setDecoratorProvide(): BaseDecoratorProvide {
        val decoratorProvide = DataBindingDecoratorProvide()
        decoratorProvide.viewModule = itemViewModule ?: mViewModule
        return decoratorProvide
    }

    private fun setSpanCount(manager: GridLayoutManager) {
        val spanCount = manager.spanCount
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val mData = mViewModule!!.mData
                if (mData.size > position) {
                    val entity = mData[position]
                    if (entity is BaseListEntity) {
                        return if (!isMulSpanCount) {
                            val count = entity.spanCount
                            if (count == 0) spanCount else spanCount / count
                        } else 1
                    }
                }
                return 1
            }
        }
    }

    override fun loadDataComplete(isRefresh: Boolean) {
        if (mRefreshLayout != null)
            if (isRefresh)
                mRefreshLayout!!.finishRefresh()
            else
                mRefreshLayout!!.finishLoadMore()
    }

    override fun setLoadingMoreEnabled(enabled: Boolean) {
        LogUtil.d(TAG, "setLoadingMoreEnabled", "enabled = $enabled")
        if (mRefreshLayout != null)
            mRefreshLayout!!.setEnableLoadMore(enabled)
    }

    override fun setPullRefreshEnabled(enabled: Boolean) {
        if (mRefreshLayout != null)
            mRefreshLayout!!.setEnableRefresh(enabled)
    }

    override fun autoRefresh() {
        if (mRefreshLayout != null) {
            mRefreshLayout!!.setEnableRefresh(true)
            mRefreshLayout!!.autoRefresh()
        }
    }

    /** 清空数据, 并展示空视图  */
    fun clearData() {
        mViewModule?.clearData()
    }

    /** 是否能够刷新数据  */
    private fun canNotifyData(): Boolean {
        //解决滑动过程刷新数据crash, 参考：https://www.jianshu.com/p/be89ebfb215e
        return !mList.isComputingLayout ||
                RecyclerView.SCROLL_STATE_IDLE == mList.scrollState
    }

    override fun notifyDataSetChanged() {
        val canNotifyData = canNotifyData()
        if (!canNotifyData) return
        if (ThreadUtil.isMainThread()) {
            //必须在主线程调用才有效
            mAdapter.notifyDataSetChanged()
        } else {
            Handler(Looper.getMainLooper()).post {
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun updateData(@ViewListUpdateType type: Int, position: Int) {
        val canNotifyData = canNotifyData()
        if (!canNotifyData) return
        if (ThreadUtil.isMainThread()) {
            updateDataOnMainThread(type, position)
        } else {
            Handler(Looper.getMainLooper()).post { updateDataOnMainThread(type, position) }
        }
    }

    private fun updateDataOnMainThread(type: Int, position: Int) {
        val count = mAdapter.itemCount
        val itemCount =
            if (type == IListView.REMOVE) count + 1 else if (type == IListView.INSERTED) count - 1 else count
        if (position in 0 until itemCount) {
            when (type) {
                IListView.INSERTED -> mAdapter.notifyItemInserted(position)
                IListView.CHANGED -> mAdapter.notifyItemChanged(position)
                IListView.REMOVE -> mAdapter.notifyItemRemoved(position)
                IListView.SCROLL_TO_TOP -> scrollToTop()
                IListView.SCROLL_TO_BOTTOM -> scrollToPosition(mAdapter.itemCount - 1)
                IListView.SCROLL_TO_POSITION -> scrollToPosition(position)
            }
        }
    }

    fun scrollToPosition(position: Int) {
        if (position >= 0 && mAdapter.itemCount > position) {
            val layoutManager = mList.layoutManager
            if (layoutManager is LinearLayoutManager) {
                layoutManager.scrollToPositionWithOffset(position, 0)
            }
        }
    }

    /**
     * 滚动到顶部
     */
    override fun scrollToTop() {
        if (mAdapter.itemCount > 0) {
            if (isSmoothScrollList) {
                mList.smoothScrollToPosition(0)
            } else {
                mList.scrollToPosition(0)
            }
        }
    }

    override fun setTempType(type: Int) {
        //非必须
    }

    override fun addHeaderView(@LayoutRes layoutId: Int) {
        //非必须
    }

    /**
     * 清空数据
     */
    fun cleanData() {
        mViewModule?.cleanData()
    }

}
