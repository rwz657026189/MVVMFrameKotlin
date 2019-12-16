package com.rwz.lib_comm.ui.adapter.rv.mul.decorator

import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.widget.TextView
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.ImageView
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.rwz.lib_comm.utils.ImageLoader.ImageLoader
import com.rwz.lib_comm.utils.ImageLoader.ImageLoaderUtil


/**
 * date： 2019/11/15 15:31
 * author： rwz
 * description：
 **/
class BaseViewHolder(
    private val mConvertView: View
): RecyclerView.ViewHolder(mConvertView) {

    private var mViews = SparseArray<View>()

    fun <T : View> getView(viewId: Int): T {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = mConvertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
    }

    fun getConvertView(): View {
        return mConvertView
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


}





