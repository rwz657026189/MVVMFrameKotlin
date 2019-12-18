package com.rwz.lib_base.base

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.databinding.ViewDataBinding
import com.rwz.lib_base.R
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.abs.IViewModule
import com.rwz.lib_comm.base.BaseActivity
import com.rwz.lib_comm.config.PARCELABLE_ENTITY
import com.rwz.lib_comm.config.STRING
import com.rwz.lib_comm.entity.extension.TempEntity
import com.rwz.lib_comm.entity.params.WebEntity
import com.rwz.lib_comm.extension.TAG
import com.rwz.lib_comm.utils.show.LogUtil
import com.rwz.web.AbsWebViewProxy
import com.rwz.web.IPostEvent1
import com.rwz.web.InfWebView

/**
 * date： 2019/12/6 15:51
 * author： rwz
 * description：
 **/
abstract class BaseWebActivity<VB : ViewDataBinding, VM : IViewModule<IView>> : BaseActivity<VB, VM>() {

    protected var mType: Int = 0
    protected lateinit var mWebEntity: WebEntity
    protected lateinit var mProxy: AbsWebViewProxy
    protected lateinit var mTempEntity: TempEntity

    internal var mInfWebView: InfWebView = object : InfWebView {
        override fun onPageStarted(url: String) {}

        override fun onPageFinished(url: String) {
            setTempType(TempEntity.STATUS_DISMISS)
        }

        override fun onReceivedError(url: String) {
            setTempType(TempEntity.STATUS_ERROR)
        }

        override fun shouldOverrideUrlLoading(url: String): Boolean {
            return this@BaseWebActivity.shouldOverrideUrlLoading(url)
        }

        override fun onReceivedTitle(title: String) {
            mToolbarProxy.titleView?.text = title
        }

        override fun onFullScreenChanged(isFullScreen: Boolean, view: View) {
            val webView = mProxy.webView ?: return
            if (isFullScreen) { //全屏播放视频
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                webView.visibility = View.INVISIBLE
                val decor = window.decorView as FrameLayout
                decor.addView(view)
            } else {//正常播放视频
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                val decor = window.decorView as FrameLayout
                decor.removeView(view)
                webView.visibility = View.VISIBLE
            }
        }
    }

    private val onPageLoadProgress = IPostEvent1<Int> { progress ->
        onLoadProgress(progress!!)
        if (progress >= 100) {
            setTempType(TempEntity.STATUS_DISMISS)
        }
    }

    @NonNull
    protected abstract fun setWebViewProxy(): AbsWebViewProxy

    override fun init(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.init(savedInstanceState)
        setSwipeBackEnable(false)
        mWebEntity = intent.getParcelableExtra(PARCELABLE_ENTITY)
        val postParams = intent.getStringExtra(STRING)
        mType = mWebEntity.type
        mToolbarProxy.titleView?.text = mWebEntity.title
        mProxy = setWebViewProxy()
        mProxy.setInfWebView(mInfWebView)
        mProxy.setOnLoadProgress(onPageLoadProgress)
        mProxy.loadUrl(mWebEntity.url, postParams, true)
        mTempEntity = TempEntity()
        LogUtil.d(TAG, "init", mWebEntity)
//        mBinding?.setVariable(BR.entity, mTempEntity)
        val reload = findViewById(R.id.reload)
        reload.setOnClickListener(this)
    }

    protected fun shouldOverrideUrlLoading(url: String): Boolean {
        return false
    }

    protected open fun onLoadProgress(progress: Int) {

    }

    protected fun setTempType(type: Int) {
        mTempEntity.type.set(type)
    }

    override fun onLeftClick() {
        onBackPressed()
    }

    override fun onBackPressed() {
        if (!mProxy.goBack()) {
            super.onBackPressed()
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        if (v.id == R.id.reload) {
            setTempType(TempEntity.STATUS_DISMISS)
            mProxy.reload()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mProxy.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        mProxy.onDestroy()
        super.onDestroy()
    }


}