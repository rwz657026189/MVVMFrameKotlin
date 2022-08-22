package com.landon.debug.view

import android.graphics.Bitmap
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * date： 2019/11/11 14:59
 * author： rwz
 * description：
 **/
class AbsSimpleViewHolder constructor(
        private val convertView: View
): RecyclerView.ViewHolder(convertView) {
    /**
     * 转载一个item里面所有控件的数组
     */
    private val mViews: SparseArray<View> = SparseArray()

    init {
        convertView.tag = this
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    fun <T : View> getView(viewId: Int): T? {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = convertView.findViewById(viewId)
            if (view != null) {
                mViews.put(viewId, view)
            }
        }
        return view as T?
    }

    fun setVisible(viewId: Int, visible: Boolean): AbsSimpleViewHolder {
        getView<View>(viewId)?.visibility = if(visible) View.VISIBLE else View.GONE
        return this
    }

    fun setListener(viewId: Int, listener: View.OnClickListener) {
        getView<View>(viewId)?.setOnClickListener(listener)
    }

    fun getText(viewId: Int): String? {
        return getView<TextView>(viewId)?.text?.toString()
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    fun setText(viewId: Int, text: CharSequence?): AbsSimpleViewHolder {
        val view = getView<TextView>(viewId)
        if (view != null) {
            view.text = text
        }
        return this
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    fun setImageResource(viewId: Int, drawableId: Int): AbsSimpleViewHolder {
        val view = getView<ImageView>(viewId)
        view?.setImageResource(drawableId)
        return this
    }

    /**
     * 为ImageView设置图片
     *
     */
    fun setImageBitmap(viewId: Int, bm: Bitmap): AbsSimpleViewHolder {
        val view = getView<ImageView>(viewId)
        view?.setImageBitmap(bm)
        return this
    }

    /**
     * 设置RadioButton
     *
     * @param viewId
     * @param checked
     * @return
     */
    fun setRadioButton(viewId: Int, checked: Boolean?): AbsSimpleViewHolder {
        val radioBt = getView<RadioButton>(viewId)
        if (radioBt != null) {
            radioBt.isChecked = checked!!
        }
        return this
    }

    /**
     * 是否显示radioBt
     *
     * @param viewId
     * @param visible
     * @return
     */
    fun setRadioBtVisiable(viewId: Int, visible: Boolean): AbsSimpleViewHolder {
        val radioBt = getView<RadioButton>(viewId)
        if (radioBt != null) {
            radioBt.visibility = if (visible) View.VISIBLE else View.GONE
        }
        return this
    }
}