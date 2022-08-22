package com.landon.debug.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rwz.app.R

class DebugNetActivity: AppCompatActivity() {
    lateinit var holder: AbsSimpleViewHolder

    private val mFragments = listOf(
            DebugNetLogFragment(),
            DebugRequestFragment(),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView = LayoutInflater.from(this).inflate(R.layout.debug_activity_net, null)
        setContentView(contentView)
        holder = AbsSimpleViewHolder(contentView)
        holder.setListener(R.id.right) {
            holder.getView<View>(R.id.menu)?.visibility = View.VISIBLE
        }
        holder.setListener(R.id.net_log) {
            showFragment(0)
            holder.getView<View>(R.id.menu)?.visibility = View.GONE
        }
        holder.setListener(R.id.send_request) {
            showFragment(1)
            holder.getView<View>(R.id.menu)?.visibility = View.GONE
        }
        showFragment(0)
    }

    fun getFragment(position: Int): Fragment? = run {
        mFragments.getOrNull(position)
    }

    fun showFragment(position: Int) {
        supportFragmentManager.beginTransaction()
                .apply {
                    var fragment = supportFragmentManager.findFragmentByTag("debug-$position")
                    if (fragment != null) {
                        show(fragment)
                    } else {
                        fragment = mFragments.getOrNull(position) ?: return
                        add(R.id.container, fragment, "debug-$position")
                    }
                }
                .commitNow()
    }

}