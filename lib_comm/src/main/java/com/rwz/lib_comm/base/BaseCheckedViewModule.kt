package com.rwz.lib_comm.base

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.rwz.lib_comm.R
import com.rwz.lib_comm.abs.IListView
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.entity.params.BiCommandEntity
import com.rwz.lib_comm.entity.turn.MsgDialogTurnEntity
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.utils.show.LogUtil
import com.rwz.lib_comm.utils.show.ToastUtil
import io.reactivex.functions.Consumer

/**
 * date： 2019/11/30 17:02
 * author： rwz
 * description：具有选择的item 列表
 * 注意：
 * 1、数据请求成功后，更新总条目数 {@link BaseCheckedViewModule#setTotalCount(int)}
 * 2、执行onHandleBatch()后 更新条目数 {@link BaseCheckedViewModule#setTotalCount(int)}
 * 3、如果部分item不可选，请求数据后(包含刷新)设置可选集合 {@link BaseCheckedViewModule#setCheckableData(java.util.Set)}
 * 4、全选与否： {@link BaseCheckedViewModule#toggleSelectAll()}
 * 5、需要分页并且全选时， 分页数据请求成功后需要调用 {@link BaseCheckedViewModule#addAllOnSelectAll()}
 **/

abstract class BaseCheckedViewModule<V : IListView> : BaseListViewModule<V>() {

    var isDelStatus = ObservableBoolean()//是否删除状态
    var isSelectAll = ObservableBoolean()//是否全选状态
    var checkSize = ObservableInt() //选中的条数
    protected val mCheckSet: MutableSet<IBaseEntity> = mutableSetOf()
    //可选择集合(默认全部)
    protected var mCheckableData: MutableSet<IBaseEntity>? = null
    /**
     * 设置条目总数
     * @param totalCount 条目总数
     */
    var totalCount: Int = 0
        set(totalCount) {
            field = totalCount
            isSelectAll.set(checkSize.get() === totalCount)
        }//所有条目数

    /**
     * 选择按钮改变的监听的监听
     */
    val onCheckedChangeCommand: Consumer<BiCommandEntity<Boolean, IBaseEntity>> =
        Consumer { biEntity ->
            if (isDelStatus.get() && biEntity != null) {
                val isChecked = biEntity.a
                val entity = biEntity.t
                val contains = mCheckSet.contains(entity)
                if (isChecked && !contains) {
                    mCheckSet.add(entity!!)
                } else if (!isChecked && contains) {
                    mCheckSet.remove(entity)
                }
                LogUtil.d(
                    TAG,
                    "onItemClick2_onCheckedChangeCommand",
                    mCheckSet.contains(entity),
                    entity
                )
                checkSize.set(mCheckSet.size)
                isSelectAll.set(mCheckSet.size == totalCount)
                onCheckedChanged(mCheckSet)
            }
        }

    /**
     * @return 可选集合
     */
    val checkableData: Collection<IBaseEntity>
        get() = mCheckableData ?: mData

    fun toggleStatus() {
        mCheckSet.clear()
        isSelectAll.set(false)
        checkSize.set(0)
        val isDel = isDelStatus.get()
        isDelStatus.set(!isDel)
        setPullRefreshEnabled(isDel && totalCount != 0)
        postEvent(IView.UPLOAD_EDIT_STATE, isDelStatus.get())
        notifyDataSetChanged()
    }

    fun isChecked(iEntity: IBaseEntity?): Boolean {
        if (!isDelStatus.get())
            return false
        if (isSelectAll.get())
            return true
        var isChecked = false
        if (iEntity != null) {
            isChecked = mCheckSet.contains(iEntity)
        }
        LogUtil.d(
            TAG,
            "isChecked = $isChecked",
            "checkSize = " + checkSize.get(),
            "mTotalCount = $totalCount",
            "mCheckSet = " + mCheckSet.size
        )
        return isChecked
    }

    protected override fun onItemClick(position: Int, @NonNull iEntity: IBaseEntity) {
        if (isDelStatus.get()) {
            val contains = mCheckSet.contains(iEntity)
            val result: Boolean
            if (contains) {
                result = mCheckSet.remove(iEntity)
            } else {
                result = mCheckSet.add(iEntity)
            }
            LogUtil.d(
                TAG,
                "onItemClick",
                "position = $position",
                "contains = $contains",
                "result = $result"
            )
            checkSize.set(mCheckSet.size)
            isSelectAll.set(mCheckSet.size == totalCount)
            updateData(IListView.CHANGED, position)
        }
    }

    /** 选中状态改变时  */
    protected fun onCheckedChanged(set: Set<IBaseEntity>?) {}

    /**
     * 反置全选
     */
    fun toggleSelectAll() {
        LogUtil.d(
            TAG,
            "toggleSelectAll",
            "isDelStatus = " + isDelStatus.get(),
            "isSelectAll = " + isSelectAll.get()
        )
        if (isDelStatus.get()) {
            if (isSelectAll.get()) {
                isSelectAll.set(false)
                mCheckSet.clear()
                checkSize.set(0)
            } else {
                isSelectAll.set(true)
                mCheckSet.addAll(checkableData)
                checkSize.set(totalCount)
            }
            notifyDataSetChanged()
            onCheckedChanged(mCheckSet)
        }
    }

    /**
     * 当全选的时候，需要手动加入下一页实体类
     */
    protected fun addAllOnSelectAll() {
        if (isDelStatus.get() && isSelectAll.get()) {
            mCheckSet?.addAll(checkableData)
        }
    }

    /**
     * 设置可选集合
     */
    fun setCheckableData(checkableData: MutableSet<IBaseEntity>) {
        this.mCheckableData = checkableData
    }

    override fun cleanDataOnRefresh(requestCode: String) {
        super.cleanDataOnRefresh(requestCode)
        mCheckableData?.clear()
    }

    override fun onClickView(id: Int, @Nullable entity: IBaseEntity?) {
        super.onClickView(id, entity)
        LogUtil.d(TAG, "onClickView", "id = $id")
        when (id) {
            R.id.enter -> onShowBatchDialog(R.string.dialog_enter_batch)
            R.id.all -> toggleSelectAll()
            R.id.cancel -> toggleStatus()
        }
    }

    protected fun onShowBatchDialog(@StringRes res: Int) {
        if (checkSize.get() === 0) {
            ToastUtil.showShortSingle(R.string.checked_one_at_least)
        } else {
            showDialog(res, DIALOG_ENTER_HANDLE_BATCH)
        }
    }

    protected override fun onClickDialogEnter(entity: MsgDialogTurnEntity) {
        super.onClickDialogEnter(entity)
        if (entity.requestCode === DIALOG_ENTER_HANDLE_BATCH) {
            onHandleBatch(mCheckSet)
            toggleStatus()
        }
    }

    fun getRightBottomText(type: Int, checkNum: Int): String {
        return ""
    }

    /**
     * 执行批量操作
     */
    protected abstract fun onHandleBatch(checkSet: Set<IBaseEntity>?)

    companion object {
        protected val DIALOG_ENTER_HANDLE_BATCH = 100 //批量操作dialog标识符
    }
}