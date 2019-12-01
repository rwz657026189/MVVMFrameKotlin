package com.rwz.lib_comm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.rwz.lib_comm.BR
import com.rwz.lib_comm.R
import com.rwz.lib_comm.abs.IListView
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.abs.IViewModule
import com.rwz.lib_comm.base.proxy.DialogProxy
import com.rwz.lib_comm.base.proxy.PostEventProxy
import com.rwz.lib_comm.base.proxy.ToolbarProxy
import com.rwz.lib_comm.entity.turn.MsgDialogTurnEntity
import com.rwz.lib_comm.utils.ImageLoader.ImageLoaderUtil
import com.rwz.lib_comm.utils.show.LogUtil

/**
 * date： 2019/11/18 16:07
 * author： rwz
 * description：fragment基类
 **/
abstract class BaseFragment<VB : ViewDataBinding, VM : IViewModule<out IView>>
    : Fragment(), IView, View.OnClickListener {

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
    lateinit var mPostEventProxy: PostEventProxy

    open lateinit var rootView: View
        protected set
    //是否自动加载数据
    var isAutoLoadingData = true

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, setLayoutId(), container, false)
        rootView = binding!!.root
        rootView.let {  }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isAlive = true
        config()
        mDialogProxy = DialogProxy(fragmentManager!!, this::onClickDialogEnter, this::onClickDialogCancel)
        mToolbarProxy = ToolbarProxy(rootView, this)
        mPostEventProxy = PostEventProxy(activity!!, mDialogProxy)
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

    override fun onPostEvent(type: Int, params: Any?) {
        mPostEventProxy.onPostEvent(type, params)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        ImageLoaderUtil.getInstance().cleanGlide(context)
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
        //点击左侧按钮，, 非必须
    }

    open fun onRightClick() {
        //点击右侧按钮，, 非必须
    }

}