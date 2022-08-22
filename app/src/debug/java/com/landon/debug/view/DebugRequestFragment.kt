package com.landon.debug.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rwz.app.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.time.Duration

class DebugRequestFragment(
        var entity: DevNetEntity? = null
): Fragment() {
    private lateinit var holder: AbsSimpleViewHolder

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(60))
                .readTimeout(Duration.ofSeconds(60))
                .writeTimeout(Duration.ofSeconds(60))
                .build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.debug_fragment_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        holder = AbsSimpleViewHolder(view)
        setNetEntity(entity)
    }

    fun setNetEntity(entity: DevNetEntity?) {
        if (isAdded) {
            holder.setText(R.id.url, entity?.url)
                    .setText(R.id.method, entity?.method)
                    .setText(R.id.header, entity?.header)
                    .setText(R.id.body, entity?.postForm)
                    .setText(R.id.response, entity?.respText)
                    .setListener(R.id.perform, this::performRequest)
        } else {
            this.entity = entity
        }
    }

    private fun performRequest(v: View) {
        val url = holder.getText(R.id.url)
        if (url.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "url不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        val method = holder.getText(R.id.method)
        if (method.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "method", Toast.LENGTH_SHORT).show()
            return
        }
        val headers = Headers.Builder().apply {
            holder.getText(R.id.header)?.split("&")?.forEach { params ->
                val key = params.split("=").getOrNull(0)
                val value = params.split("=").getOrNull(1)
                if (key.isNullOrEmpty().not() && value != null) {
                    add(key!!, value)
                }
            }
        }.build()
        val body = holder.getText(R.id.body)?.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
        val timeout = holder.getText(R.id.timeout)?.toIntOrNull() ?: 60
        val okHttpClient = if (client.connectTimeoutMillis != timeout) {
            client.newBuilder()
                    .connectTimeout(Duration.ofSeconds(timeout.toLong()))
                    .readTimeout(Duration.ofSeconds(timeout.toLong()))
                    .writeTimeout(Duration.ofSeconds(timeout.toLong()))
                    .build()
        } else client
        val request = Request.Builder()
                .url(url)
                .method(method, body)
                .headers(headers)
                .build()
        okHttpClient.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                holder.setText(R.id.response, e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val respText = response.body?.string() ?: ""
                val resp: String = try {
                    JSONObject(respText).toString(4)
                } catch (e: Exception) {
                    e.printStackTrace()
                    e.toString()
                }
                holder.setText(R.id.response, resp)
            }
        })
    }
}