package com.landon.debug.view

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.landon.debug.manager.DebugLogManager
import com.landon.debug.net.interceptor.mock.LimitMock
import com.landon.debug.net.interceptor.mock.MockInterceptor
import com.landon.debug.view.dialog.DebugEditDialog
import com.landon.debug.view.dialog.DebugSimpleDialog
import com.rwz.app.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class DebugNetLogFragment: Fragment() {
    private lateinit var mList: RecyclerView
    private lateinit var mAdapter: MyAdapter
    private lateinit var holder: AbsSimpleViewHolder
    private val mData = mutableListOf<DevEntity>()
    private val threadPool = Executors.newSingleThreadExecutor()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.debug_fragment_net_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        holder = AbsSimpleViewHolder(view)
        mList = holder.getView(R.id.list)!!
        mList.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = MyAdapter()
        mList.adapter = mAdapter
        NetDevManager.addEntity.observe(viewLifecycleOwner) {
            if (!mData.contains(it)) {
                mData.add(it)
            }
        }
        mData.clear()
        mData.addAll(NetDevManager.data)
        mList.scrollToPosition(mData.size - 1)
        holder.getView<EditText>(R.id.search)?.doAfterTextChanged {
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
        holder.setListener(R.id.toBottom) {
            mList.scrollToPosition((mData.size - 1).coerceAtLeast(0))
        }
        holder.setListener(R.id.toBottom) {
            mList.scrollToPosition(0)
        }
        holder.setListener(R.id.toBottom) {
            DebugSimpleDialog.show(requireContext(), "确认清空日志?") { dialog, _ ->
                NetDevManager.data.clear()
                mData.clear()
                mAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }
    }

    private inner class MyAdapter : RecyclerView.Adapter<AbsSimpleViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsSimpleViewHolder {
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.debug_item_app_dev_net, parent, false)
            return AbsSimpleViewHolder(view)
        }

        override fun onBindViewHolder(holder: AbsSimpleViewHolder, position: Int) {
            val item = mData.getOrNull(position) ?: return
            val netEntity = item.netEntity
            val resp = if(netEntity == null) ""
            else "───────────── request ─────────────\n" +
                    "url: ${netEntity.url}\n" +
                    "method: ${netEntity.method}\n" +
                    "body: ${netEntity.postForm}\n" +
                    "请求时间: ${SimpleDateFormat("HH:mm:ss sss").format(netEntity.requestTime)}\n" +
                    "耗时: ${netEntity.dt / 1000f}秒\n" +
                    "───────────── response ─────────────\n" +
                    netEntity.respText
            val isGroup = item.group?.isNotEmpty() ?: false
            holder.setVisible(R.id.group, isGroup)
                    .setVisible(R.id.title, !isGroup)
                    .setVisible(R.id.detail, !isGroup && item.isExtend)
                    .setText(R.id.group, item.group)
                    .setText(R.id.resp, resp)
                    .setText(R.id.title, item.title)
            holder.getView<TextView>(R.id.title)?.setTextColor(Color.parseColor(if(item.isSucceed) "#333333" else "#FF8888"))
            holder.setListener(R.id.title) {
                item.isExtend = item.isExtend.not()
                mAdapter.notifyItemChanged(position)
            }
            // 下载到本地
            holder.setListener(R.id.download) {
                val entity = netEntity ?: return@setListener
                threadPool.submit {
                    val filePath = DebugLogManager.getInstance().download(
                        entity.requestTime,
                        entity.header,
                        entity.postForm,
                        entity.url,
                        entity.respText,
                        if(entity.url.isNullOrEmpty()) "--" else Uri.parse(entity.url).path
                    )
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "保存到$filePath", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            // 编辑请求
            holder.setListener(R.id.modify) {
                DebugEditDialog().apply {
                    content = netEntity?.respText
                    setTitle("修改响应")
                    setCancel("取消", null)
                    setSure("确定") {
                        val json: String
                        try {
                            json = JSONObject(content).toString()
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "非法json: $e", Toast.LENGTH_SHORT).show()
                            return@setSure
                        }
                        MockInterceptor.getInstance().register(LimitMock(
                                Uri.parse(netEntity?.url ?: "").path,
                                netEntity?.method,
                                json
                        ).setPerformCount(1))
                        Toast.makeText(requireContext(), "仅在下次请求生效一次", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }.show(requireContext())
            }
            holder.setListener(R.id.perform) {
                (requireActivity() as? DebugNetActivity)?.run {
                    (getFragment(1) as? DebugRequestFragment)?.setNetEntity(entity = netEntity)
                    showFragment(1)
                }
            }
        }

        override fun getItemCount(): Int {
            return mData.size
        }
    }
}

internal data class DevEntity(
        val requestTime: String?,
        val group: String?,
        val title: String?,
        var isExtend: Boolean = false,
        var isSucceed: Boolean = false,
        val netEntity: DevNetEntity?,
)

data class DevNetEntity(
        // header
        val requestTime: Long,
        val header: String?,
        val url: String?,
        val method: String?,
        val postForm: String?,
        // body
        val isSucceed: Boolean,
        val respText: String,
        val dt: Long
)

object NetDevManager {
    // 最多存储个数
    private const val maxCount = 100

    internal val data = mutableListOf<DevEntity>()

    internal val addEntity = MutableLiveData<DevEntity>()

    fun addGroup(group: String) {
        put(DevEntity(null, group, null, isExtend = false, isSucceed = false, netEntity = null))
    }

    fun addNetResponse(url: String, header: String?, body: String, method: String, dt: Long, resp: String) {
        val requestTime =
                SimpleDateFormat("HH:mm:ss sss").format(Date(System.currentTimeMillis() - dt))
        var isSucceed = false
        val message: String = try {
            when {
                resp.startsWith("{") -> {
                    val jsonObject = JSONObject(resp)
                    isSucceed = jsonObject.optJSONObject("head")?.optString("code") == "200"
                    jsonObject.toString(4)
                }
                resp.startsWith("[") -> {
                    val jsonArray = JSONArray(resp)
                    jsonArray.toString(4)
                }
                else -> {
                    resp
                }
            }
        } catch (e: JSONException) {
            resp
        }
        val netEntity = DevNetEntity(
                requestTime = System.currentTimeMillis() - dt,
                header = header,
                url = url,
                method = method,
                postForm = body,
                isSucceed = isSucceed,
                respText = message,
                dt = dt
        )
        put(DevEntity(
                requestTime,
                null,
                "${requestTime.split(" ").getOrNull(0)} ${Uri.parse(url).path}",
                isExtend = false,
                isSucceed = isSucceed,
                netEntity = netEntity
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