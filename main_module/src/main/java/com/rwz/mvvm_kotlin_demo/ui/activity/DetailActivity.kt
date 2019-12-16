package com.rwz.mvvm_kotlin_demo.ui.activity

import android.os.Bundle
import android.view.View
import com.rwz.lib_comm.abs.IView
import com.rwz.lib_comm.abs.IViewModule
import com.rwz.lib_comm.base.BaseActivity
import com.rwz.lib_comm.config.PARCELABLE_ENTITY
import com.rwz.lib_comm.ui.dialog.CommBottomDialog
import com.rwz.lib_comm.utils.app.CommUtils
import com.rwz.lib_comm.utils.app.DialogHelp
import com.rwz.lib_comm.utils.app.FragmentUtil
import com.rwz.lib_comm.utils.show.ToastUtil
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.databinding.ActivityDetailBinding
import com.rwz.mvvm_kotlin_demo.entity.response.JokeEntity
import com.rwz.mvvm_kotlin_demo.entity.response.NewComment
import com.rwz.mvvm_kotlin_demo.ui.fragment.DetailFragment
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.concurrent.TimeUnit

/**
 * date： 2019/12/1 13:32
 * author： rwz
 * description：
 **/
class DetailActivity : BaseActivity<ActivityDetailBinding, IViewModule<IView>>() {

    lateinit var mFragment: DetailFragment

    override fun setViewModule(): IViewModule<IView>? {
        return null
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_detail
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mFragment = FragmentUtil.newInstance(DetailFragment::class.java, intent.extras)
        supportFragmentManager.beginTransaction()
            .add(R.id.container, mFragment, "DetailFragment")
            .commit()
        mToolbarProxy.titleView?.text = "详情"
        mToolbarProxy.setRightDrawable(R.drawable.ic_more)
        enter.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        if (v.id == R.id.enter) {
            mFragment.addComment(edit.text.toString())
        }
    }

    override fun onRightClick() {
        val dialog = CommBottomDialog.Build()
            .addItem("举报")
            .addItem("分享")
            .setOnClickItemListener{
                    _, pos, _, _ ->
                run {
                    if (pos == 0) {
                        report()
                    } else if(pos == 1){
                        shareTo()
                    }
                }
            }
            .create()
        DialogHelp.show(supportFragmentManager, dialog, "CommBottomDialog")
    }

    private fun report() {
        Observable.just("举报成功")
            .delay(1, TimeUnit.SECONDS)
            .doOnNext { ToastUtil.showShortSingle(it) }
            .subscribe()
    }

    private fun shareTo() {
        val jokeEntity = intent.extras?.getParcelable<JokeEntity>(PARCELABLE_ENTITY)
        CommUtils.shareTextToSystem(jokeEntity?.title)
    }

}