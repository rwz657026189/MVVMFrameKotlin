package com.landon.debug.adapter

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rwz.app.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class DebugNetActivity: AppCompatActivity() {
    private lateinit var mList: RecyclerView
    private lateinit var mAdapter: MyAdapter
    private val mData = mutableListOf<DevEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.debug_activity_net)
        mList = findViewById(R.id.list)
        with(findViewById<Toolbar>(R.id.toolbar)) {
            setNavigationIcon(R.mipmap.dev_toobar_back_icon)
            setNavigationOnClickListener { onBackPressed() }
        }
        mList.layoutManager = LinearLayoutManager(this)
        mAdapter = MyAdapter()
        mList.adapter = mAdapter
        NetDevManager.addEntity.observe(this) {
            if (!mData.contains(it)) {
                mData.add(it)
            }
        }
        mData.clear()
        mData.addAll(NetDevManager.data)
        mList.scrollToPosition(mData.size - 1)
        findViewById<EditText>(R.id.search).doAfterTextChanged {
            val key = it.toString().toLowerCase()
            if (key.isEmpty()) {
                mData.clear()
                mData.addAll(NetDevManager.data)
            } else {
                mData.clear()
                mData.addAll(NetDevManager.data.filter { item -> item.title?.toLowerCase()?.contains(key) ?: false })
            }
            mAdapter.notifyDataSetChanged()
            mList.scrollToPosition((mData.size - 1).coerceAtLeast(0))
        }

        findViewById<View>(R.id.fab).setOnClickListener {
            mList.scrollToPosition((mData.size - 1).coerceAtLeast(0))
        }
    }

    private inner class MyAdapter : RecyclerView.Adapter<MyHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val view = LayoutInflater.from(this@DebugNetActivity).inflate(R.layout.debug_item_app_dev_net, parent, false)
            return MyHolder(view)
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            val item = mData.getOrNull(position)
            val isGroup = item?.group?.isNotEmpty() ?: false
            holder.group.visibility = if(isGroup) View.VISIBLE else View.GONE
            holder.title.visibility = if(!isGroup) View.VISIBLE else View.GONE
            holder.detail.visibility = if(!isGroup && item?.isExtend == true) View.VISIBLE else View.GONE
            holder.group.text = item?.group
            holder.title.text = item?.title
            holder.detail.text = item?.detail
            holder.title.setTextColor(Color.parseColor(if(item?.isSucceed == true) "#333333" else "#FF8888"))
            holder.title.setOnClickListener {
                item?.isExtend = item?.isExtend?.not() ?: false
                mAdapter.notifyItemChanged(position)
            }
        }

        override fun getItemCount(): Int {
            return mData.size
        }

    }

    private inner class MyHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {
        val group: TextView = itemView.findViewById(R.id.group)
        val title: TextView = itemView.findViewById(R.id.title)
        val detail: TextView = itemView.findViewById(R.id.detail)
    }
}

internal data class DevEntity(
    val requestTime: String?,
    val group: String?,
    val title: String?,
    val detail: String?,
    var isExtend: Boolean = false,
    var isSucceed: Boolean = false,
)

object NetDevManager {
    // 最多存储个数
    private const val maxCount = 100

    internal val data = mutableListOf<DevEntity>()

    internal val addEntity = MutableLiveData<DevEntity>()

    fun addGroup(group: String) {
        put(DevEntity(null, group, null, null))
    }

    fun addNetResponse(url: String, body: String, method: String, dt: Long, resp: String) {
        val requestTime =
            SimpleDateFormat("HH:mm:ss sss").format(Date(System.currentTimeMillis() - dt))
        var isSucceed = false
        val message: String = try {
            if (resp.startsWith("{")) {
                val jsonObject = JSONObject(resp)
                isSucceed = jsonObject.optString("code") == "0"
                jsonObject.toString(4)
            } else if (resp.startsWith("[")) {
                val jsonArray = JSONArray(resp)
                jsonArray.toString(4)
            } else {
                resp
            }
        } catch (e: JSONException) {
            resp
        }
        val detail = "────────────────────────────── request ──────────────────────────────\n" +
            "url: $url\n" +
                "method: $method\n" +
                "body: $body\n" +
                "请求时间: $requestTime\n" +
                "耗时: ${dt / 1000f}秒\n" +
                "────────────────────────────── response ──────────────────────────────\n" +
                message
        put(DevEntity(
            requestTime,
            null,
            "${requestTime.split(" ").getOrNull(0)} ${Uri.parse(url).path}",
            detail,
            isExtend = false,
            isSucceed = isSucceed
        ))
    }

    private fun put(entity: DevEntity) {
        synchronized(this) {
            if (data.size > maxCount) {
                data.removeLast()
            } else {
                data.add(entity)
            }
            addEntity.postValue(entity)
        }
    }
}