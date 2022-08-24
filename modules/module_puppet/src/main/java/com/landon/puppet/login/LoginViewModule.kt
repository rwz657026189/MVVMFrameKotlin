package com.landon.puppet.login

import android.text.SpannableString
import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.landon.puppet.R
import com.landon.puppet.net.ApiModule
import com.landon.puppet.utils.SpannableStringUtil
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.base.BaseViewModule
import com.rwz.lib_comm.entity.response.SimpleResponse
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.utils.app.ResourceUtil
import com.rwz.lib_comm.utils.show.ToastUtil

/**
 * date： 2019/12/6 14:33
 * author： rwz
 * description：
 **/
class LoginViewModule : BaseViewModule<IView>() {

    var account: ObservableField<String> = ObservableField()
    var password: ObservableField<String> = ObservableField()
    //是否展开
    var isExtend = ObservableBoolean(false)

    //是否可以登录
    var canLogin = ObservableBoolean()
    var mobileAreaCode: ObservableField<String> = ObservableField() //选择的国家区号
    var mobileCode: ObservableField<String> = ObservableField() //选择的国家区号

    private fun canLogin() {
        val account = this.account.get()
        val pw = password.get()
        val canLogin = !TextUtils.isEmpty(account) && !TextUtils.isEmpty(pw)
        this.canLogin.set(canLogin)
    }

    override fun onClickView(id: Int, iEntity: IBaseEntity?) {
        super.onClickView(id, iEntity)
        when (id) {
            R.id.login -> login()
//            R.id.tips//用户协议
//            -> TurnHelp.h5(mContext, R.string.user_agreement, H5.AGREEMENT)
        }
    }

    private fun login() {
        val account = this.account.get()
        val pw = this.password.get()
        ApiModule.login(account!!, pw!!).subscribe(getObserver("login"))
    }

    override fun onResponseSuccess(requestCode: String, data: Any?) {
        super.onResponseSuccess(requestCode, data)
        if ("login" === requestCode && data is SimpleResponse) {
            ToastUtil.showShortSingle(data.message)
        } else {

        }
    }

    /**
     * 用户协议
     * @return
     */
    fun getUserAgreement(): SpannableString {
        val tips = ResourceUtil.getString(R.string.reg_tips)
        val userAgreement =
            ResourceUtil.getString(R.string.user_agreement)
        val start = tips.length
        return SpannableStringUtil(tips + userAgreement)
            .addForegroundColorSpan(
                ResourceUtil.getColor(R.color.colorPrimary), start, start + userAgreement.length
            )
    }

}