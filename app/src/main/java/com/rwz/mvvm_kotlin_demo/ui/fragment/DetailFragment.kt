package com.rwz.mvvm_kotlin_demo.ui.fragment

import com.rwz.lib_comm.base.BaseListFragment
import com.rwz.lib_comm.config.PARCELABLE_ENTITY
import com.rwz.lib_comm.databinding.LayoutRecyclerviewBinding
import com.rwz.mvvm_kotlin_demo.entity.response.JokeEntity
import com.rwz.mvvm_kotlin_demo.entity.response.NewComment
import com.rwz.mvvm_kotlin_demo.viewmodule.DetailViewModule

/**
 * date： 2019/12/1 13:41
 * author： rwz
 * description：
 **/
class DetailFragment : BaseListFragment<LayoutRecyclerviewBinding, DetailViewModule>() {

    private lateinit var jokeEntity: JokeEntity

    override fun config() {
        jokeEntity = arguments!!.getParcelable(PARCELABLE_ENTITY)!!
    }

    override fun setViewModule(): DetailViewModule {
        return DetailViewModule(jokeEntity)
    }

    fun addComment(entity: String) {
        mViewModule?.addComment(entity)
    }

}