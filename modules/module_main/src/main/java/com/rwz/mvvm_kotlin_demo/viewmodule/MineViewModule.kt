package com.rwz.mvvm_kotlin_demo.viewmodule

import android.view.View
import androidx.databinding.ObservableField
import com.rwz.lib_comm.base.BaseViewModule
import com.rwz.lib_comm.comm.CommonObserver
import com.rwz.lib_comm.entity.params.CommandEntity
import com.rwz.lib_comm.entity.turn.MsgDialogTurnEntity
import com.rwz.lib_comm.manager.ContextManager
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.utils.app.CommUtils
import com.rwz.lib_comm.utils.show.LogUtil
import com.rwz.lib_comm.utils.show.ToastUtil
import com.rwz.lib_comm.utils.system.AndroidUtils
import com.rwz.lib_comm.utils.system.FileUtil
import com.rwz.lib_comm.utils.system.ScreenUtil
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.ui.fragment.MineFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * date： 2019/11/30 18:27
 * author： rwz
 * description：
 **/
class MineViewModule : BaseViewModule<MineFragment>() {

    val versionName: ObservableField<String> = ObservableField()

    init {
        versionName.set("v" + AndroidUtils.getVersionName(ContextManager.context))
    }

    override fun onClickView(id: Int, iEntity: IBaseEntity?) {
        when (id) {
            R.id.version -> checkNewVersion()
            R.id.share -> share()
        }
    }

    private fun share(): Unit {
        val savePath = File((ContextManager.context.externalCacheDir?: ContextManager.context.cacheDir),
            "temp.jpg")
        val subscribe = Observable.just(mView!!.mRootView.findViewById(R.id.content) as View)
            .subscribeOn(Schedulers.io())
            .map { ScreenUtil.getInstance().snapView(it) }
            .map { FileUtil.saveBitmap(savePath.absolutePath, it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val packageName = "com.tencent.mm"
                val clsName = "com.tencent.mm.ui.tools.ShareToTimeLineUI"
                val result = CommUtils.shareImageToSystem(savePath, packageName, clsName)
                LogUtil.d("result = $result")
            }, {
                LogUtil.d("result = ${it.message}")
                print(it)
            })
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

    val onCheckedChangedEventCommand: Consumer<*> =
        Consumer<CommandEntity<Any>> { commandEntity ->
            if (commandEntity.id == R.id.sc) {
                ToastUtil.showShortSingle(if (commandEntity.t as Boolean) "已关闭推送" else "已开启推送")
            }
        }


}
