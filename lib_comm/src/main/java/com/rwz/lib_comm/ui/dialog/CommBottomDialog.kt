package com.rwz.lib_comm.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.rwz.lib_comm.R
import com.rwz.lib_comm.base.BaseDialog
import com.rwz.lib_comm.databinding.DialogBottomLinearBinding
import com.rwz.lib_comm.entity.params.CommBottomEntity
import com.rwz.lib_comm.utils.app.DensityUtils
import com.rwz.lib_comm.utils.app.ResourceUtil
import com.rwz.lib_comm.utils.show.LogUtil
import java.util.*
import kotlin.collections.ArrayList


/**
 * 通话的底部弹出dialog
 * Created by rwz on 2016/8/31.
 */
class CommBottomDialog : BaseDialog<DialogBottomLinearBinding>(), View.OnClickListener {

    private var mTemp: View? = null
    private var mData: List<CommBottomEntity>? = null
    private var mArgs: Bundle? = null
    private var mCancel: TextView? = null

    private var onClickItemListener: OnClickItemListener? = null

    override fun setLayoutId(): Int {
        return R.layout.dialog_bottom_linear
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        val arguments = arguments ?: return
        val mItemContainer = mBinding!!.mItemContainer
        mTemp = mBinding!!.temp
        val titleView = mBinding!!.title
        mData = arguments.getParcelableArrayList(KEY_ITEMS_ARRAY)
        val title = arguments.getString(KEY_TITLE)
        mArgs = arguments.getParcelable(KEY_ARGS)
        val cancelable = arguments.getBoolean(KEY_CANCELABLE)
        val isTransStatus = arguments.getBoolean(KEY_TRANS_STATUS)
        val color = arguments.getInt(KEY_COLOR, ResourceUtil.getColor(R.color.text_dark_color))
        if (mData == null) {
            LogUtil.d("tag", "———————————————【 mCancel 不能为空 】———————————————")
            return
        }
        val dialog = dialog
        val window = dialog?.window
        if (window != null) {
            window.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
            val p = window.attributes
            p.width = ViewGroup.LayoutParams.MATCH_PARENT
            if (isTransStatus)
                p.flags = p.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN //包含状态栏
            window.attributes = p
            window.setWindowAnimations(R.style.dialogAnim)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        if (!TextUtils.isEmpty(title)) {
            titleView.text = title
            titleView.visibility = View.VISIBLE
        }
        isCancelable = cancelable
        val inflater = LayoutInflater.from(context)
        val childLayout = arguments.getInt(KEY_CHILD_LAYOUT, R.layout.item_simple_menu)
        for (i in mData!!.indices) {
            val (content, itemColor, isClickEnable) = mData!![i]
            val item = inflater.inflate(childLayout, mItemContainer, false)
            val textView = item.findViewById<TextView>(R.id.text) ?: continue
            textView.text = content
            mItemContainer.addView(item)
            item.setTag(item.id, i)
            textView.setTextColor(if (itemColor == 0) color else itemColor)
            if (isClickEnable)
                item.setOnClickListener(this)
        }
        mCancel = inflater.inflate(R.layout.item_simple_menu, null, false) as TextView
        mCancel!!.text = ResourceUtil.getString(R.string.cancel)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.height = ResourceUtil.getDimen(R.dimen.tab_height)
        params.topMargin = DensityUtils.dp2px(CANCEL_TOP_MARGIN)
        mItemContainer.addView(mCancel, params)
        mCancel!!.setOnClickListener(this)
        mTemp!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v !== mCancel && v !== mTemp && onClickItemListener != null && mData != null) {
            val position = v.getTag(v.id) as Int
            onClickItemListener!!.onClickItem(v.context, position, mData!!, mArgs)
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
        private var title = ""
        private var args: Bundle? = null//回调参数
        private var color = ResourceUtil.getColor(R.color.text_dark_color)//回调参数
        private var cancelable = true  //点击外部是否可以取消dialog
        private var isTransStatus = false //是否透明化状态栏
        private var data: ArrayList<CommBottomEntity>? = null
        private var onClickItemListener: OnClickItemListener? = null
        private var childLayout: Int = 0    //子布局（最外层）

        fun setTitle(title: String): Build {
            this.title = if (TextUtils.isEmpty(title)) "" else title
            return this
        }

        /** 是否可以取消  */
        fun setCancelable(cancelable: Boolean) {
            this.cancelable = cancelable
        }

        /** 传递参数  */
        fun setArgs(args: Bundle): Build {
            this.args = args
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

        /**
         * 设置item布局， 必须包含id 为 text 的TextView控件
         */
        fun setChildLayout(@LayoutRes childLayout: Int): Build {
            this.childLayout = childLayout
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

        fun create(data: List<String>?): CommBottomDialog {
            val list = ArrayList<CommBottomEntity>()
            if (data != null && !data.isEmpty()) {
                for (i in data.indices) {
                    val entity = CommBottomEntity(data[i], 0, true)
                    list.add(entity)
                }
            }
            return create(list)
        }

        fun create(data: Array<String>?): CommBottomDialog {
            val list = ArrayList<CommBottomEntity>()
            if (data != null && data.isNotEmpty()) {
                for (str in data) {
                    val entity = CommBottomEntity(str, 0, true)
                    list.add(entity)
                }
            }
            return create(list)
        }

        @JvmOverloads
        fun create(data: ArrayList<CommBottomEntity>? = this.data): CommBottomDialog {
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
            val dialog = CommBottomDialog()
            dialog.arguments = bundle
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
        private const val CANCEL_TOP_MARGIN = 15//取消按钮顶部margin
    }


}
