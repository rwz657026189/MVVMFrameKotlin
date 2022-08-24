package com.rwz.lib_comm.ui.dialog

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.annotation.LayoutRes

import com.rwz.lib_comm.R
import com.rwz.lib_comm.base.BasePopupWindow
import com.rwz.lib_comm.databinding.PopTopMenuBinding
import com.rwz.lib_comm.entity.params.CommBottomEntity
import com.rwz.lib_comm.utils.app.ResourceUtil

import java.util.ArrayList

/**
 * Created by rwz on 2018/7/26.
 */

class CommMenuPop private constructor(context: Context, arguments: Bundle?) :
    BasePopupWindow<PopTopMenuBinding>(
        context,
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ), View.OnClickListener {

    private val mData: List<CommBottomEntity>?
    private val mArgs: Bundle?

    private var onClickItemListener: OnClickItemListener? = null

    init {
        val mItemContainer = mBinding!!.container
        mData = arguments!!.getParcelableArrayList(KEY_ITEMS_ARRAY)
        mArgs = arguments.getParcelable(KEY_ARGS)
        val cancelable = arguments.getBoolean(KEY_CANCELABLE)
        isOutsideTouchable = cancelable
        val color = arguments.getInt(KEY_COLOR, ResourceUtil.getColor(R.color.text_dark_color))
        val childLayout = arguments.getInt(KEY_CHILD_LAYOUT, R.layout.item_simple_menu)
        val inflater = LayoutInflater.from(context)
        for (i in mData!!.indices) {
            val (content, itemColor, isClickEnable) = mData[i]
            val item = inflater.inflate(childLayout, mItemContainer, false)
            val textView = item.findViewById<TextView>(R.id.text) ?: continue
            textView.text = content
            mItemContainer.addView(item)
            item.setTag(item.id, i)
            textView.setTextColor(if (itemColor == 0) color else itemColor)
            if (isClickEnable)
                item.setOnClickListener(this)
        }
    }

    override fun setLayoutId(): Int {
        return R.layout.pop_top_menu
    }

    override fun onClick(v: View) {
        if (onClickItemListener != null && mData != null) {
            val position = v.getTag(v.id) as Int
            onClickItemListener!!.onClickItem(v.context, position, mData, mArgs)
        }
        dismiss()
    }

    fun setOnClickItemListener(onClickItemListener: OnClickItemListener?) {
        this.onClickItemListener = onClickItemListener
    }

    interface OnClickItemListener {
        /**
         * @param context  某些dialog跳转dialog，再次跳转需要context
         * @param position 点击位置
         * @param data items
         * @param args  传递的参数
         */
        fun onClickItem(
            context: Context,
            position: Int,
            data: List<CommBottomEntity>,
            args: Bundle?
        )
    }

    /**
     * 建造者
     * 1.默认标题不可点击
     * 2.不可点击仅针对item
     */
    class Build {
        private val title = ""
        private var args: Bundle? = null//回调参数
        private var color = ResourceUtil.getColor(R.color.text_dark_color)//回调参数
        private var cancelable = true  //点击外部是否可以取消dialog
        private var isTransStatus = false //是否透明化状态栏
        private var data: ArrayList<CommBottomEntity>? = null
        private var onClickItemListener: OnClickItemListener? = null
        private var childLayout: Int = 0

        /** 是否可以取消  */
        fun setCancelable(cancelable: Boolean) {
            this.cancelable = cancelable
        }

        /** 传递参数  */
        fun setArgs(args: Bundle): Build {
            this.args = args
            return this
        }

        /**
         * 设置item布局， 必须包含id 为 text 的TextView控件
         */
        fun setChildLayout(@LayoutRes childLayout: Int): Build {
            this.childLayout = childLayout
            return this
        }

        /** 统一颜色值  */
        fun setItemColor(color: Int): Build {
            this.color = color
            return this
        }

        fun setOnClickItemListener(onClickItemListener: OnClickItemListener): Build {
            this.onClickItemListener = onClickItemListener
            return this
        }

        /** 设置是否透明状态栏 默认： false  */
        fun setTransStatus(transStatus: Boolean): Build {
            isTransStatus = transStatus
            return this
        }

        /** 添加item  */
        fun addItem(content: String): Build {
            if (!TextUtils.isEmpty(content))
                addItem(CommBottomEntity(content, 0, true))
            return this
        }

        fun addItem(entity: CommBottomEntity?): Build {
            if (data == null)
                data = ArrayList()
            if (entity != null)
                data!!.add(entity)
            return this
        }

        fun create(context: Context, data: List<String>?): CommMenuPop {
            val list = ArrayList<CommBottomEntity>()
            if (data != null && !data.isEmpty()) {
                for (i in data.indices) {
                    val entity = CommBottomEntity(data[i], 0, true)
                    list.add(entity)
                }
            }
            return create(context, list)
        }

        fun create(context: Context, data: Array<String>?): CommMenuPop {
            val list = ArrayList<CommBottomEntity>()
            if (data != null && data.isNotEmpty()) {
                for (aData in data) {
                    val entity = CommBottomEntity(aData, 0, true)
                    list.add(entity)
                }
            }
            return create(context, list)
        }

        @JvmOverloads
        fun create(context: Context, data: ArrayList<CommBottomEntity>? = this.data): CommMenuPop {
            val bundle = Bundle()
            if (data != null)
                bundle.putParcelableArrayList(KEY_ITEMS_ARRAY, data)
            bundle.putString(KEY_TITLE, title)
            if (args != null)
                bundle.putParcelable(KEY_ARGS, args)
            bundle.putInt(KEY_COLOR, color)
            bundle.putBoolean(KEY_CANCELABLE, cancelable)
            bundle.putBoolean(KEY_TRANS_STATUS, isTransStatus)
            if (childLayout != 0)
                bundle.putInt(KEY_CHILD_LAYOUT, childLayout)
            val dialog = CommMenuPop(context, bundle)
            dialog.setOnClickItemListener(onClickItemListener)
            return dialog
        }
    }

    companion object {
        private const val KEY_ITEMS_ARRAY = "KEY_ITEMS_ARRAY"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_ARGS = "KEY_ARGS"
        private const val KEY_COLOR = "KEY_COLOR"
        private const val KEY_CANCELABLE = "KEY_CANCELABLE"
        private const val KEY_TRANS_STATUS = "KEY_TRANS_STATUS"
        private const val KEY_CHILD_LAYOUT = "KEY_CHILD_LAYOUT"
    }


}