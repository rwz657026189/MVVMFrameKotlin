package com.landon.debug.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.landon.debug.utils.LogUtil
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
        val headers = parseHeader(holder.getText(R.id.header) ?: "")
        val body = parseBody(holder.getText(R.id.body) ?: "")
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
                activity?.runOnUiThread {
                    holder.setText(R.id.response, e.toString())
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val respText = response.body?.string() ?: ""
                val resp: String = try {
                    JSONObject(respText).toString(4)
                } catch (e: Exception) {
                    e.printStackTrace()
                    e.toString()
                }
                activity?.runOnUiThread {
                    holder.setText(R.id.response, resp)
                }
            }
        })
    }

    private fun parseBody(bodyText: String): RequestBody? {
        var mediaType: String? = null
        val realBodyText: String? = try {
            val json = JSONObject(bodyText).toString()
            mediaType = "application/json;charset=UTF-8"
            json
        } catch (e: Exception) {
            if (bodyText.contains("&")) {
                mediaType = "application/x-www-form-urlencoded"
                if (bodyText.endsWith("&")) bodyText.subSequence(0, bodyText.length - 1).toString() else bodyText
            } else {
                null
            }
        }
        LogUtil.debug("DebugRequestFragment", "performRequest: realBodyText = $realBodyText mediaType=$mediaType")
        return realBodyText?.toRequestBody(mediaType?.toMediaTypeOrNull())
    }

    private fun parseHeader(headers: String): Headers {
        val builder = Headers.Builder()
        if (headers.contains("\n")) {
            val lines = headers.split("\n")
            lines.forEach {
                val key = it.split(": ").getOrNull(0)
                val value = it.split(": ").getOrNull(1)
                if (key.isNullOrEmpty().not() && value != null) {
                    builder.add(key!!, value)
                }
            }
        } else {
            val params = headers.split("&")
            params.forEach {
                val key = it.split("=").getOrNull(0)
                val value = it.split("=").getOrNull(1)
                if (key.isNullOrEmpty().not() && value != null) {
                    builder.add(key!!, value)
                }
            }
        }
        return builder.build()
    }
}