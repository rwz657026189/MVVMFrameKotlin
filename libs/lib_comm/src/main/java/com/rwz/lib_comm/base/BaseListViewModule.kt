package com.rwz.lib_comm.base

import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import com.rwz.lib_comm.R
import com.rwz.lib_comm.abs.IListView
import com.rwz.lib_comm.abs.ViewListUpdateType
import com.rwz.lib_comm.comm.CommonObserver
import com.rwz.lib_comm.entity.extension.TempEntity
import com.rwz.lib_comm.entity.extension.TempType
import com.rwz.lib_comm.entity.response.CommListData
import com.rwz.lib_comm.entity.response.SimpleResponse
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.utils.show.LogUtil
import com.rwz.lib_comm.utils.show.ToastUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.functions.Consumer

/**
 * date： 2019/11/29 15:04
 * author： rwz
 * description：
 **/
abstract class BaseListViewModule<V : IListView>(clickCommand: Consumer<*>? = null)
    : BaseViewModule<V>(clickCommand), OnRefreshLoadMoreListener, OnLoadMoreListener {

    companion object{
        //第一页下标
        const val FIRST_PAGE = 1
    }

    // 页数
    protected var mPage = FIRST_PAGE
    //该次请求是否刷新
    var isRefresh = true
        private set
    //第一次是否请求到数据（主要用于界面初始化）
    var hasRequestData: ObservableBoolean = ObservableBoolean(false)
    //数据源
    var mData: MutableList<IBaseEntity> = mutableListOf()
    //占位图
    open var mTempEntity: TempEntity? = null
    //分隔条位置
    var mDecorationList: MutableList<Int>? = null
    //标记需要采用空视图的请求;一般是第一条, 非特殊情况禁止修改（一般只用于刷新的适合会更改）
    open var mTempRequestCode: String? = null
    //加载完成后是否允许下拉刷新、上拉加载更多
    protected var isRefreshEnable = true
    protected var isLoadingMoreEnable = true

    override fun initCompleted() {
        mTempEntity = getTempEntity()
        //初始化完成开始显示占位图, 防止部分页面传递有列表数据
        if (mLoadStrategy != LoadStrategy.LOAD_NOT) {
            mData.add(mTempEntity!!)
            notifyDataSetChanged()
        }
        super.initCompleted()
    }

    /**
     * 设置某处位置的空视图
     * 在init()方法后调用,否则无效
     * @param type
     */
    protected fun setTemp(@TempType type: Int) {
        LogUtil.d(TAG, "setTemp", "type = $type")
        mTempEntity?.type?.set(type)
    }

    protected fun loadDataComplete() {
        mView?.loadDataComplete(isRefresh)
    }

    protected fun notifyDataSetChanged() {
        LogUtil.d(TAG, "MainListViewModule, notifyDataSetChanged", "mView = $mView")
        mView?.notifyDataSetChanged()
    }

    protected fun updateData(@ViewListUpdateType type: Int, position: Int) {
        mView?.updateData(type, position)
    }

    /** 移除数据  */
    @Synchronized
    protected fun removeData(iEntity: IBaseEntity?) {
        if (iEntity != null) {
            val position = mData.indexOf(iEntity)
            if (position >= 0) {
                mData.remove(iEntity)
                mView?.updateData(IListView.REMOVE, position)
            }
        }
    }

    /** 滚动到顶部  */
    protected fun scrollToTop() {
        mView?.updateData(IListView.SCROLL_TO_TOP, 0)
    }

    /** 滚动到指定位置  */
    protected fun scrollToPosition(position: Int) {
        mView?.updateData(IListView.SCROLL_TO_POSITION, position)
    }

    override fun getObserver(requestCode: String, isDispatch : Boolean): CommonObserver<Any> {
        //若未修改, 标记第一条请求, 会根据该请求的结果决定是否显示占位图
        if (mTempRequestCode == null)
            mTempRequestCode = requestCode
        return super.getObserver(requestCode, isDispatch)
    }

    override fun onResponseError(requestCode: String, e: Throwable) {
        LogUtil.d(
            TAG, "BaseListViewModule", "onResponseError",
            "requestCode = $requestCode", "mPage = $mPage", "isRefresh = $isRefresh")
        if (mTempRequestCode === requestCode && mPage == FIRST_PAGE && isRefresh) {
            //先清空一次数据(比如再次搜索的时候加载失败,就需要清空数据在显示)
            cleanDataOnRefresh(requestCode)
            if (mTempEntity != null && !mData.contains(mTempEntity!!))
                mData.add(mTempEntity!!)
            setTemp(TempEntity.STATUS_ERROR)
            notifyDataSetChanged()
            setRefreshLoadingMoreEnable(true, false)
        }
        hasRequestData.set(true)
        //加载完成，改变状态
        loadDataComplete()
    }

    /**
     * 多条请求逻辑：
     * 1. 第一条请求必然判断是否为空 是：空视图； 否 下一个请求：无论是否为空均不判断
     * 2. 刷新时会重新第一条请求
     * @param requestCode  请求码
     * @param data  实体类
     */
    @Synchronized
    override fun onResponseSuccess(requestCode: String, data: Any?) {
        LogUtil.d(
            TAG,
            "BaseListViewModule",
            "onResponseSuccess",
            "mTempRequestCode = $mTempRequestCode",
            "requestCode = $requestCode",
            "mPage = $mPage",
            "isRefresh = $isRefresh")
        if (mTempRequestCode === requestCode) {
            //判断是否为空集合
            if (isEmptyData(requestCode, data)) {
                if (mPage == FIRST_PAGE && isRefresh) {
                    //先清空一次数据(比如再次搜索的时候加载失败,就需要清空数据在显示)
                    cleanDataOnRefresh(requestCode)
                    assertNullData(null)
                    notifyDataSetChanged()
                    setRefreshLoadingMoreEnable(isRefreshEnable = true, isLoadingMoreEnable = false)
                }
            } else {
                if (mPage == FIRST_PAGE && isRefresh) {
                    cleanDataOnRefresh(requestCode)
                }
                performRequestData(requestCode, data)
            }
        } else {
            performRequestData(requestCode, data)
        }
        hasRequestData.set(true)
        //加载完成，改变状态
        loadDataComplete()
    }

    /** 判断请求的数据是否为空  */
    protected open fun isEmptyData(requestCode: String, data: Any?): Boolean {
        if (data == null || data is Collection<*> && data.isEmpty()) {
            return true
        } else if (data is CommListData<*>) {
            val list = data.arrayList
            return list == null || list.isEmpty()
        }
        return false
    }

    private fun performRequestData(requestCode: String, data: Any?) {
        mTempEntity?.let {
            mData.remove(it)
            mTempEntity = null
        }
        handlerData(requestCode, data)
        LogUtil.d(
            TAG, "performRequestData", "isRefreshEnable = $isRefreshEnable",
            "isLoadingMoreEnable = $isLoadingMoreEnable"
        )
        //启动上拉加载更多、下拉刷新
        setRefreshLoadingMoreEnable(isRefreshEnable, isLoadingMoreEnable)
    }

    /** 当刷新的时候清空数据  */
    protected open fun cleanDataOnRefresh(requestCode: String) {
        mData.clear()
    }

    /**
     * recyclerView item 点击事件
     */
    fun onItemClick(position: Int) {
        mData.getOrNull(position)
            ?.let { onItemClick(position, it) }
    }

    protected abstract fun onItemClick(position: Int, @NonNull iEntity: IBaseEntity)

    /**
     * 服务器请求到的实体类
     * @param requestCode 请求码
     * @param data  实体类
     */
    protected open fun handlerData(requestCode: String, data: Any?) {
        LogUtil.d(TAG, "handlerData, requestCode = $requestCode, data = $data")
        if (data != null && data is Collection<*> && !data.isEmpty()) {
            mData.addAll(data as Collection<IBaseEntity>)
            notifyDataSetChanged()
        } else if (data is SimpleResponse) {
            ToastUtil.showShort(data.message)
        } else if (data is CommListData<*>) {
            data.arrayList?.takeUnless { it.isEmpty() }
                ?.let { mData.addAll(it) }
            notifyDataSetChanged()
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        onRefresh()
    }

    fun onRefresh() {
        LogUtil.d(TAG, "onRefresh")
        isRefresh = true
        mPage = FIRST_PAGE
        requestData()
    }

    /** 主动刷新， 最终会修改状态 并 调用onRefresh() */
    fun autoRefresh() {
        mView?.autoRefresh()
    }

    /** 清空数据, 并展示空视图  */
    fun clearData() {
        LogUtil.d(TAG, "clearData")
        mData.clear()
        assertNullData(null)
        notifyDataSetChanged()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        isRefresh = false
        mPage++
        requestData()
        LogUtil.d("onLoadMore", "mPage = $mPage")
    }

    open fun setRefreshLoadingMoreEnable(
        isRefreshEnable: Boolean,
        isLoadingMoreEnable: Boolean
    ) {
        if (mPage == FIRST_PAGE) { //避免多处触发
            mView?.setPullRefreshEnabled(isRefreshEnable)
            mView?.setLoadingMoreEnabled(isLoadingMoreEnable)
        }
    }

    protected fun setLoadingMoreEnabled(enable: Boolean) {
        mView?.setLoadingMoreEnabled(enable)
    }

    protected fun setPullRefreshEnabled(enable: Boolean) {
        mView?.setPullRefreshEnabled(enable)
    }

    override fun onClickView(id: Int, iEntity: IBaseEntity?) {
        super.onClickView(id, iEntity)
        if (id == R.id.reload) {//空视图重新加载数据
            setTemp(TempEntity.STATUS_LOADING)
            onRefresh()
        }
    }

    fun getPage(): Int {
        return mPage
    }

    protected fun addDecorationPos(pos: Int) {
        if (pos >= 0) {
            if (mDecorationList != null) {
                if (!mDecorationList!!.contains(Integer.valueOf(pos))) {
                    mDecorationList!!.add(pos)
                }
            } else {
                LogUtil.e(TAG, "请在子类实例化")
            }
        }
    }

    /**
     * 是否请求到列表数据
     * @return
     */
    fun isResponseSuccess(): Boolean {
        return hasRequestData.get()
    }

    fun isResponseSuccess(@StringRes tips: Int): Boolean {
        return if (hasRequestData.get()) {
            true
        } else {
            ToastUtil.showShortSingle(tips)
            false
        }
    }

    protected open fun getTempEntity() = mTempEntity ?: TempEntity()

    /**
     * 判断数据是否为空，若是则显示空视图
     */
    protected open fun assertNullData(data: List<*>?) {
        if (data == null || data.isEmpty()) {
            if (mTempEntity == null)
                mTempEntity = getTempEntity()
            if (!mData.contains(mTempEntity!!))
                mData.add(mTempEntity!!)
            setTemp(TempEntity.STATUS_NULL)
        }
    }

    /**
     * 清空数据
     */
    fun cleanData() {
        mData.clear()
        mData.add(getTempEntity())
        setTemp(TempEntity.STATUS_NULL)
        notifyDataSetChanged()
        setPullRefreshEnabled(false)
    }

}