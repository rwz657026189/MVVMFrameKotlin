package com.rwz.mvvm_kotlin_demo.ui.activity

import android.os.Bundle
import com.rwz.lib_comm.base.BaseFragment
import com.rwz.lib_comm.base.BaseTabVpActivity
import com.rwz.lib_comm.config.EXIT_APP_DOUBLE_CLICK_TIME
import com.rwz.lib_comm.databinding.ActivityTabVpBinding
import com.rwz.lib_comm.entity.extension.TabEntity
import com.rwz.lib_comm.utils.app.CheckHelp
import com.rwz.lib_comm.utils.app.ResourceUtil
import com.rwz.lib_comm.utils.factory.FragmentFactory
import com.rwz.mvvm_kotlin_demo.R
import com.rwz.mvvm_kotlin_demo.ui.fragment.MainListFragment
import com.rwz.mvvm_kotlin_demo.ui.fragment.MineFragment
import kotlinx.coroutines.*
import java.util.*

class MainActivity : BaseTabVpActivity<ActivityTabVpBinding>() {

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
//        startActivity(Intent(this, MainDuplicatedActivity::class.java))
    }

    override fun requestTabData() {
        val list = ArrayList<TabEntity>()
        list.add(TabEntity(title = ResourceUtil.getString(R.string.main)))
        list.add(TabEntity(title = ResourceUtil.getString(R.string.find)))
        list.add(TabEntity(title = ResourceUtil.getString(R.string.mine)))
        setupContentViewPager(list)
    }

    override fun initFragment(tab: TabEntity, position: Int): BaseFragment<*, *> {
        return if (position == 2)
            MineFragment()
        else FragmentFactory.newInstance(
            MainListFragment::class.java,
            position
        )
    }

    override fun onBackPressed() {
        if (CheckHelp.onDoubleClickExit(EXIT_APP_DOUBLE_CLICK_TIME))
            super.onBackPressed()
    }

}

@ExperimentalCoroutinesApi
fun main() {
    println("${Thread.currentThread().name} step1")
    val job = GlobalScope.launch(Dispatchers.Unconfined) {
        println("${Thread.currentThread().name} step2")
        val data = requestData()
        println("${Thread.currentThread().name} step3 $data")
        delay(100)
        println("${Thread.currentThread().name} step4")
    }
//    job.cancel()
    println("${Thread.currentThread().name} step5")
    Thread.sleep(3000)

}

suspend fun requestData() : String{
   return withContext(Dispatchers.Default) {
       Thread.sleep(1000)
       "abc"
   }
}

suspend fun requestData2() : String{
    return GlobalScope.async {
        Thread.sleep(1000)
        "abc"
    }.await()
}