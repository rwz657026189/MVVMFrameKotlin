package com.rwz.lib_comm.base

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rwz.lib_comm.BR
import com.rwz.lib_comm.R
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.abs.IViewModule
import com.rwz.lib_comm.base.proxy.*
import com.rwz.lib_comm.entity.turn.MsgDialogTurnEntity
import com.rwz.lib_comm.extension.TAG
import com.rwz.lib_comm.utils.ImageLoader.ImageLoaderUtil
import com.rwz.lib_comm.utils.show.LogUtil

/**
 * date： 2019/11/18 16:07
 * author： rwz
 * description：activity基类
 **/
abstract class BaseActivity<VB : ViewDataBinding, VM : IViewModule<out IView>>
    : AppCompatActivity(), IView, View.OnClickListener {

    //activity是否存在
    var isAlive = false
        private set
    //activity是否可见
    var isRunning = false
        private set
    //必须采用DataBinding布局才不为空
    protected var mBinding: VB? = null
    protected var mViewModule: VM? = null
    lateinit var mDialogProxy: DialogProxy
    lateinit var mToolbarProxy: ToolbarProxy
    lateinit var mTransitionProxy: TransitionProxy
    lateinit var mStatusBarProxy: StatusBarProxy
    lateinit var mPostEventProxy: PostEventProxy

    open lateinit var mRootView: View
        protected set
    //是否自动加载数据
    var isAutoLoadingData = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isAlive = true
        mTransitionProxy = TransitionProxy(this)
        mStatusBarProxy = StatusBarProxy(this)
        config()
        initialization()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mDialogProxy = DialogProxy(supportFragmentManager, this::onClickDialogEnter, this::onClickDialogCancel)
        mToolbarProxy = ToolbarProxy(mRootView, this)
        mPostEventProxy = PostEventProxy(this, mDialogProxy)
        if (mViewModule == null)
            mViewModule = setViewModule()
        mViewModule?.let {
            lifecycle.addObserver(it)
            it.bindView(this)
        }
        init(savedInstanceState)
        mBinding?.setVariable(BR.viewModule, mViewModule)
        mBinding?.executePendingBindings()
        if (isAutoLoadingData)
            requestData()
        mViewModule?.initCompleted()
    }

    override fun dispatchTouchEvent(ev: MotionEvent)
            = mPostEventProxy.isForbidTouchScreen || super.dispatchTouchEvent(ev)

    override fun onPostEvent(type: Int, params: Any?) {
        mPostEventProxy.onPostEvent(type, params)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        ImageLoaderUtil.getInstance().cleanGlide(this)
    }

    override fun finish() {
        super.finish()
        mTransitionProxy.startPlayTransition(false)
    }

    override fun startActivity(intent: Intent, options: Bundle?) {
        super.startActivity(intent, options)
        mTransitionProxy.startPlayTransition(true)
    }

    @SuppressLint("RestrictedApi")
    override fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.startActivityForResult(intent, requestCode, options)
            mTransitionProxy.startPlayTransition(false)
        }
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        mTransitionProxy.startPlayTransition(false)
    }

    private fun initialization() {
        mBinding = DataBindingUtil.setContentView(this, setLayoutId())
        mRootView = mBinding?.root ?: window.decorView.findViewById(android.R.id.content)
        LogUtil.d(TAG, mBinding)
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
    }

    override fun onPause() {
        super.onPause()
        isRunning = false
    }

    override fun onDestroy() {
        LogUtil.d(TAG, "onDestroy")
        isAlive = false
        isRunning = false
        mViewModule = null
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mViewModule?.onSaveInstanceState(outState)
    }

    open fun config() {
        //配置参数mTransitionProxy、mStatusBarProxy可用，非必须
    }

    open fun init(savedInstanceState: Bundle?) {
        //初始化，非必须
    }

    open fun requestData() {
        //网络请求，非必须
    }

    protected abstract fun setViewModule(): VM?

    /** 设置资源布局 **/
    protected abstract fun setLayoutId(): Int

    override fun onClick(v: View) {
        when {
            v.id == R.id.left -> onLeftClick()
            v.id == R.id.title -> onTitleClick()
            v.id == R.id.right -> onRightClick()
        }
    }

    /**
     * 点击对话框确定
     */
    open fun onClickDialogEnter(entity: MsgDialogTurnEntity) {
        //点击对话框确定, 非必须
    }

    /**
     * 点击对话框取消
     */
    open fun onClickDialogCancel(entity: MsgDialogTurnEntity) {
        //点击对话框取消, 非必须
    }

    /**
     * 滚动到顶部
     */
    open fun scrollToTop() {
        //滚动到底部，, 非必须
    }

    open fun onTitleClick() {
        scrollToTop()
    }

    open fun onLeftClick() {
        onBackPressed()
    }

    open fun onRightClick() {
        //点击右侧按钮，, 非必须
    }

}