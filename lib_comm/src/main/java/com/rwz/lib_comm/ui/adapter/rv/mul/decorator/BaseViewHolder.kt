package com.rwz.lib_comm.ui.adapter.rv.mul.decorator

import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rwz.lib_comm.utils.ImageLoader.ImageLoader
import com.rwz.lib_comm.utils.ImageLoader.ImageLoaderUtil


/**
 * date： 2019/11/15 15:31
 * author： rwz
 * description：
 **/
class BaseViewHolder(
    convertView: View
): RecyclerView.ViewHolder(convertView) {

    val mConvertView = convertView
    private val mViews = SparseArray<View>()

    fun <T : View> getView(viewId: Int): T {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = mConvertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
    }

    fun setText(viewId: Int, text: String): BaseViewHolder {
        val tv = getView<TextView>(viewId)
        tv.text = text
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): BaseViewHolder {
        if(resId == 0)
            return this
        val view = getView<ImageView>(viewId)
        view.setImageResource(resId)
        return this
    }

    fun setImageUrl(viewId: Int, url: String): BaseViewHolder {
        val view = getView<ImageView>(viewId)
        ImageLoaderUtil.getInstance().loadImage(view.context,
            ImageLoader.Builder().imgView(view)
                .url(url)
                .build())
        return this
    }

    fun setVisible(viewId: Int, visible: Boolean): BaseViewHolder {
        val view = getView<View>(viewId)
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun setOnClickListener(listener: View.OnClickListener, vararg ids: Int): BaseViewHolder {
        for (id in ids) {
            getView<View>(id).setOnClickListener(listener)
        }
        return this
    }


}





