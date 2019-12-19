package com.rwz.lib_comm.base

import android.content.Intent
import androidx.annotation.StringRes
import com.rwz.lib_comm.R
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.abs.RxViewModule
import com.rwz.lib_comm.comm.CommonObserver
import com.rwz.lib_comm.entity.params.CommandEntity
import com.rwz.lib_comm.entity.response.SimpleResponse
import com.rwz.lib_comm.entity.turn.LoadingDialogTurnEntity
import com.rwz.lib_comm.entity.turn.MsgDialogTurnEntity
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.utils.app.ResourceUtil
import com.rwz.lib_comm.utils.show.LogUtil
import com.rwz.lib_comm.utils.show.ToastUtil
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer

/**
 * date： 2019/11/29 14:06
 * author： rwz
 * description：
 **/
enum class LoadStrategy{
    //不主动加载、自动加载、懒加载数据
    LOAD_NOT, LOAD_AUTO, LOAD_LAZY
}
open class BaseViewModule<V : IView>(clickCommand: Consumer<*>? = null) : RxViewModule<V>() {

    companion object{
        const val SINGLE_REQUEST_CODE = "single"
    }
    var mLoadStrategy = LoadStrategy.LOAD_LAZY
    //页面类型，xml中可能需要
    open var pageType:Int = 0
    private var isInitLoad = false

    override fun initCompleted() {
        if (mLoadStrategy == LoadStrategy.LOAD_AUTO)
            requestData()
    }

    override fun onResume() {
        super.onResume()
        //懒加载
        mLoadStrategy.takeIf { !isInitLoad && it == LoadStrategy.LOAD_LAZY }
            ?.let {
                isInitLoad = true
                requestData()
            }
    }

    open fun requestData() {
        //非必须
    }

    protected open fun getObserver(requestCode: String = SINGLE_REQUEST_CODE): CommonObserver<Any> {
        return object : CommonObserver<Any>() {
            override fun onError(e: Throwable) {
                super.onError(e)
                onResponseError(requestCode)
            }

            override fun onNext(data: Any) {
                onResponseSuccess(requestCode, data)
            }
        }
    }

    /**
     * 请求成功的回调
     * @param requestCode 请求码
     * @param data        实体类
     */
    @Synchronized
    open fun onResponseSuccess(requestCode: String, data: Any?) {
        if (data is SimpleResponse) {
            ToastUtil.showShort(data.message)
        }
    }

    /**
     * 请求失败的回调
     */
    open fun onResponseError(requestCode: String) {}

    open val onClickEventCommand: Consumer<*> = clickCommand ?:
    Consumer<CommandEntity<Any>> { commandEntity ->
        onClickView(
            commandEntity.id,
            commandEntity.t as IBaseEntity?
        )
    }

    open fun onClickView(id: Int, iEntity: IBaseEntity?) {
        LogUtil.d("BaseViewModule", "onClickView", id)
    }

    protected fun startActivity(intent: Intent, requestCode: Int = -1) {
        postEvent(IView.START_ATY, requestCode to intent)
    }

    protected fun showDialog(@StringRes msg: Int, requestCode: Int) {
        if (msg != 0) {
            val title = ResourceUtil.getString(R.string.dialog_def_title)
            val message = ResourceUtil.getString(msg)
            showDialog(MsgDialogTurnEntity(title, message, requestCode))
        }
    }

    protected fun showDialog(entity: MsgDialogTurnEntity) {
        entity.listener = mDialogListener
        mView?.onPostEvent(IView.SHOW_DIALOG, entity)
    }

    /**
     * 显示加载中对话框
     * @param res
     */
    protected fun showLoadingDialog(@StringRes res: Int) {
        showLoadingDialog(ResourceUtil.getString(res))
    }

    protected fun showLoadingDialog(text: String) {
        showLoadingDialog(LoadingDialogTurnEntity(text))
    }

    protected fun showLoadingDialog(entity: LoadingDialogTurnEntity) {
        postEvent(IView.SHOW_LOADING, entity)
    }

    protected fun dismissLoadingDialog() {
        postEvent(IView.DISMISS_LOADING)
    }

    private val mDialogListener =
        BiConsumer<MsgDialogTurnEntity, Boolean> { entity, isClickEnter ->
            if (isClickEnter!!) {
                onClickDialogEnter(entity)
            } else {
                onClickDialogCancel(entity)
            }
        }

    /**
     * 点击对话框确定
     */
    protected open fun onClickDialogEnter(entity: MsgDialogTurnEntity) {}

    /**
     * 点击对话框取消
     */
    protected open fun onClickDialogCancel(entity: MsgDialogTurnEntity) {}

    /**
     * @param isForbidTouchScreen 是否禁用触摸屏幕（禁用一切事件）
     */
    fun setForbidTouchScreen(isForbidTouchScreen: Boolean) {
        postEvent(IView.FORBID_TOUCH_SCREEN, isForbidTouchScreen)
    }

}