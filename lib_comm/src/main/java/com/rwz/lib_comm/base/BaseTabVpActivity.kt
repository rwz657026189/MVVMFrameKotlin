package com.rwz.lib_comm.base

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.ViewPager
import com.rwz.lib_comm.BR
import com.rwz.lib_comm.R
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.comm.CommonObserver
import com.rwz.lib_comm.entity.extension.TabEntity
import com.rwz.lib_comm.entity.extension.TempEntity
import com.rwz.lib_comm.entity.params.CommandEntity
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity
import com.rwz.lib_comm.ui.adapter.vp.SimpleVPAdapter
import com.rwz.lib_comm.ui.widget.CommTabLayout
import io.reactivex.functions.Consumer

/**
 * date： 2019/11/30 16:36
 * author： rwz
 * description：
 **/

abstract class BaseTabVpActivity<VB : ViewDataBinding>
    :BaseActivity<VB, BaseViewModule<IView>>(), ViewPager.OnPageChangeListener {

    lateinit var mVp:ViewPager
    var mTab: CommTabLayout? = null
    var mAdapter: SimpleVPAdapter<BaseFragment<*, *>>? = null
    var mCurrPos:Int = 0
    private var mViewStub: ViewStub? = null
    private var isInflate:Boolean = false//是否加载过空视图
    private var mTempEntity: TempEntity? = null
    var isAllowSwipeBack = true//是否允许滑动返回

    val currFragment:BaseFragment<*, *>?
        get() = mAdapter?.getItem(mCurrPos)

    private var onClickEventCommand: Consumer<CommandEntity<IBaseEntity>> =
        Consumer { commandEntity ->
            if (commandEntity != null) {
                val id = commandEntity.id
                val iEntity = commandEntity.t
                if (id == R.id.reload && mTempEntity != null) {
                    mTempEntity!!.type.set(TempEntity.STATUS_LOADING)
                    requestData()
                } else if (mViewModule != null) {
                    mViewModule?.onClickView(id, iEntity)
                }
            }
        }

    val observer: CommonObserver<List<TabEntity>>
        get() = object:CommonObserver<List<TabEntity>>() {
            override fun onNext(value:List<TabEntity>) {
                setupContentViewPager(value)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                onRequestFail()
            }
        }

    private val inflateListener: ViewStub.OnInflateListener
        get() = ViewStub.OnInflateListener { _, inflated ->
            val bind = DataBindingUtil.bind<ViewDataBinding>(inflated)
            mTempEntity = TempEntity()
            mTempEntity!!.type.set(TempEntity.STATUS_ERROR)
            bind?.setVariable(BR.entity, mTempEntity)
            bind?.setVariable(BR.viewModule, mViewModule)
        }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mVp = findViewById(R.id.vp) as ViewPager
        mTab = findViewById(R.id.tab) as CommTabLayout
        mViewStub = findViewById(R.id.view_stub) as ViewStub?
        mAdapter = SimpleVPAdapter(supportFragmentManager)
        mVp.addOnPageChangeListener(this)
        setSwipeBackEnable(isAllowSwipeBack)
    }

    override fun setViewModule():BaseViewModule<IView> {
        return BaseViewModule(onClickEventCommand)
    }

    override fun requestData() {
        requestTabData()//获取tab
    }

    protected abstract fun requestTabData()

    protected abstract fun initFragment(tab:TabEntity, position:Int):BaseFragment<*, *>

    /**
     * 初始化内容Viewpager
     */
    protected fun setupContentViewPager(tabs : List<TabEntity>?) {
        if (tabs == null){
            onRequestFail()
            return
        }
        mTempEntity?.type?.set(TempEntity.STATUS_DISMISS)
        mViewStub?.visibility = View.GONE
        for (i in tabs.indices){
            val tab = tabs[i]
            tab.position = i
            mAdapter!!.addFrag(initFragment(tab, i), tab.title)
        }
        mVp.adapter = mAdapter
        mTab?.let {
            it.setCanScroll(tabs.size > 5)
            it.setupWithViewPager(mVp)
        }
    }

    /**
     * 刷新数据
     */
    fun notifyDataSetChanged() {
        mTab?.notifyDataSetChanged()
    }

    fun onRequestFail() {
        inflateViewStub()
    }

    /**
     * 显示ViewStub--在需要的地方调用
     */
    private fun inflateViewStub() {
        mViewStub?.takeUnless { isInflate }?.let {
            isInflate = true
            it.setOnInflateListener(inflateListener)
            it.visibility = View.VISIBLE
        }
    }


    override fun onPageScrolled(position:Int, positionOffset:Float, positionOffsetPixels:Int) {
        //
    }

    override fun onPageSelected(position:Int) {
        mCurrPos = position
    }

    override fun onPageScrollStateChanged(state:Int) {
        //只允许在第一页滑动返回
        setSwipeBackEnable(isAllowSwipeBack && state == ViewPager.SCROLL_STATE_IDLE
                && mVp.currentItem == 0)
    }

    override fun scrollToTop() {
        currFragment?.scrollToTop()
    }

}
