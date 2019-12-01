package com.rwz.mvvm_kotlin_demo.viewmodule

import androidx.databinding.ObservableField
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.base.BaseViewModule
import com.rwz.lib_comm.comm.CommonObserver
import com.rwz.lib_comm.comm.SimpleObserver
import com.rwz.lib_comm.entity.turn.MsgDialogTurnEntity
import com.rwz.lib_comm.manager.ContextManager
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.utils.show.ToastUtil
import com.rwz.lib_comm.utils.system.AndroidUtils
import com.rwz.mvvm_kotlin_demo.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * date： 2019/11/30 18:27
 * author： rwz
 * description：
 **/
class MineViewModule : BaseViewModule<IView>() {

    val versionName: ObservableField<String> = ObservableField()

    init {
        versionName.set("v" + AndroidUtils.getVersionName(ContextManager.context))
    }

    override fun onClickView(id: Int, iEntity: IBaseEntity?) {
        when (id) {
            R.id.collect -> ToastUtil.showShortSingle(R.string.collect)
            R.id.follow -> ToastUtil.showShortSingle(R.string.follow)
            R.id.history_record -> ToastUtil.showShortSingle(R.string.history_record)
            R.id.version -> checkNewVersion()
            R.id.setting -> ToastUtil.showShortSingle(R.string.setting)
        }

    }

    private fun checkNewVersion() {
        showLoadingDialog("检查新版本...")
        Observable.just(1)
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CommonObserver<Int>() {
                override fun onNext(value: Int) {
                    dismissLoadingDialog()
                    val entity = MsgDialogTurnEntity("提示", "暂无新版本", 0)
                    entity.enterText = "知道了"
                    entity.cancelText = ""
                    showDialog(entity)
                }
            })
    }

}
