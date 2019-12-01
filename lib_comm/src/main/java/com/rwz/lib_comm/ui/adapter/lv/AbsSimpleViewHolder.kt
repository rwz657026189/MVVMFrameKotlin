package com.rwz.lib_comm.ui.adapter.lv

import android.content.Context
import android.graphics.Bitmap
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView

/**
 * date： 2019/11/11 14:59
 * author： rwz
 * description：
 **/
class AbsSimpleViewHolder private constructor(
    private val context: Context,
    parent: ViewGroup,
    layoutId: Int,
    val position: Int
) {
    /**
     * 转载一个item里面所有控件的数组
     */
    private val mViews: SparseArray<View> = SparseArray()
    /**
     * item所加载的view
     */
    val convertView: View = LayoutInflater.from(context).inflate(layoutId, parent, false)

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

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    fun setText(viewId: Int, text: CharSequence): AbsSimpleViewHolder {
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

    companion object {

        /**
         * 拿到一个ViewHolder对象
         *
         * @param context
         * @param convertView
         * @param parent
         * @param layoutId
         * @param position
         * @return
         */
        fun getViewHolder(
            context: Context,
            convertView: View?,
            parent: ViewGroup,
            layoutId: Int,
            position: Int
        ): AbsSimpleViewHolder {
            return if (convertView == null) {
                AbsSimpleViewHolder(context, parent, layoutId, position)
            } else convertView.tag as AbsSimpleViewHolder
        }
    }
}