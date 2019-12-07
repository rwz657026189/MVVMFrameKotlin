package com.rwz.lib_base.ui.activity

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.annotation.NonNull
import com.rwz.lib_base.R
import com.rwz.lib_base.base.BaseWebActivity
import com.rwz.lib_base.databinding.ActivityTencentWebBinding
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.abs.IViewModule
import com.rwz.web.AbsWebViewProxy
import com.rwz.web.sys.SysWebViewProxy

/**
 * date： 2019/12/6 15:50
 * author： rwz
 * description：
 **/
class CommWebActivity : BaseWebActivity<ActivityTencentWebBinding, IViewModule<IView>>() {

    private var mProgressBar: ProgressBar? = null

    @NonNull
    override fun setWebViewProxy(): AbsWebViewProxy {
        return SysWebViewProxy(this, findViewById(R.id.webView) as WebView)
    }

    override fun setViewModule(): IViewModule<IView>? {
        return null
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_tencent_web
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mProgressBar = mBinding?.progressBar
    }

    override fun onLoadProgress(progress: Int) {
        if (progress >= 100) {
            mProgressBar!!.visibility = View.GONE
        } else {
            mProgressBar!!.progress = progress
            mProgressBar!!.visibility = View.VISIBLE
        }
    }

}