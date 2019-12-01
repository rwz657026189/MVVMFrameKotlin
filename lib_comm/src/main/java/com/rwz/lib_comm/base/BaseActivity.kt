package com.rwz.lib_comm.base

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rwz.lib_comm.BR
import com.rwz.lib_comm.R
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.abs.IViewModule
import com.rwz.lib_comm.base.proxy.*
import com.rwz.lib_comm.entity.turn.MsgDialogTurnEntity
import com.rwz.lib_comm.utils.ImageLoader.ImageLoaderUtil
import com.rwz.lib_comm.utils.show.LogUtil
import com.rwz.lib_comm.utils.system.ScreenUtil
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.app.SwipeBackActivity

/**
 * date： 2019/11/18 16:07
 * author： rwz
 * description：activity基类
 **/
abstract class BaseActivity<VB : ViewDataBinding, VM : IViewModule<IView>>
    : SwipeBackActivity(), IView, View.OnClickListener {

    val TAG: String = javaClass.simpleName
    //activity是否存在
    var isAlive = false
        private set
    //activity是否可见
    var isRunning = false
        private set
    protected var binding: VB? = null
    protected var mViewModule: VM? = null
    lateinit var mDialogProxy: DialogProxy
    lateinit var mToolbarProxy: ToolbarProxy
    lateinit var mTransitionProxy: TransitionProxy
    lateinit var mStatusBarProxy: StatusBarProxy
    lateinit var mPostEventProxy: PostEventProxy

    open lateinit var rootView: View
        protected set
    //是否自动加载数据
    var isAutoLoadingData = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isAlive = true
        mTransitionProxy = TransitionProxy(this)
        mStatusBarProxy = StatusBarProxy(this)
        config()
        initSwipeBackLayout()
        initialization()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mDialogProxy = DialogProxy(supportFragmentManager, this::onClickDialogEnter, this::onClickDialogCancel)
        mToolbarProxy = ToolbarProxy(rootView, this)
        mPostEventProxy = PostEventProxy(this, mDialogProxy)
        if (mViewModule == null)
            mViewModule = setViewModule()
        mViewModule?.let {
            lifecycle.addObserver(it)
            it.bindView(this)
        }
        init(savedInstanceState)
        binding?.setVariable(BR.viewModule, mViewModule)
        binding?.executePendingBindings()
        if (isAutoLoadingData)
            requestData()
        mViewModule?.initCompleted()
    }

    private fun initSwipeBackLayout() {
        val layout = swipeBackLayout
        layout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)
        layout.setEdgeSize(ScreenUtil.getInstance().getScreenWidth(this) / 4)
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
        binding = DataBindingUtil.setContentView(this, setLayoutId())
        rootView = binding!!.root
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

    protected abstract fun setViewModule(): VM

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