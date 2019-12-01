package com.rwz.lib_comm.ui.adapter.vp

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rwz.lib_comm.utils.show.LogUtil
import java.util.*

/**
 * date： 2019/11/15 16:49
 * author： rwz
 * description：
 **/
class SimpleVPAdapter<F : Fragment>(private val mFragmentManager: FragmentManager) :
    FragmentPagerAdapter(mFragmentManager) {
    private val mFragmentList = ArrayList<F>()
    private val mFragmentTitleList = ArrayList<String>()

    val fragmentList: List<F>
        get() = mFragmentList

    val fragmentTitleList: List<String>
        get() = mFragmentTitleList

    override fun getCount() = mFragmentList.size

    /**
     * 清空缓存的fragment，一般在activity.onCreate()中操作
     */
    fun clearCacheFragments() {
        val list = mFragmentManager.fragments
        LogUtil.d("SimpleVPAdapter, clearCacheFragments", list)
        if (list != null && list!!.size > 0) {
            try {
                val ft = mFragmentManager.beginTransaction()
                for (fragment in list) {
                    ft.remove(fragment)
                }
                ft.commitAllowingStateLoss()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position)
    }

    @JvmOverloads
    fun addFrag(fragment: F, title: String = "") {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    fun update(fragments: List<F>?) {
        if (fragments != null && fragments.isNotEmpty()) {
            mFragmentList.clear()
            mFragmentList.addAll(fragments)
            notifyDataSetChanged()
        }
    }

    fun getFragment(pos: Int): F? {
        return if (pos < mFragmentList.size) {
            mFragmentList[pos]
        } else null
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return if (mFragmentTitleList != null && mFragmentTitleList.size > position) {
            mFragmentTitleList[position]
        } else {
            null
        }
    }

    override fun getItem(position: Int): F {
        return mFragmentList[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        //        super.destroyItem(container, position, obj);
    }
}