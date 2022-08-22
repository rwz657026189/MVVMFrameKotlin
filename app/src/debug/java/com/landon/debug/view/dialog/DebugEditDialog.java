package com.landon.debug.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.rwz.app.R;

/**
 * @Author Ren Wenzhang
 * @Date 2021/6/25/025 19:50
 * @Description
 */
public class DebugEditDialog {

    private AlertDialog mDialog;
    private Context context;

    private String title;
    private String content;
    private String sureText;
    private String cancelText;
    private View.OnClickListener sureListener;
    private View.OnClickListener cancelListener;
    private TextView contentView;

    public DebugEditDialog setSure(String text, View.OnClickListener listener) {
        this.sureText = text;
        this.sureListener = listener;
        return this;
    }

    public DebugEditDialog setCancel(String text, View.OnClickListener listener) {
        this.cancelText = text;
        this.sureListener = listener;
        return this;
    }

    public DebugEditDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public DebugEditDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public void show(Context context) {
        this.context = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.debug_dialog_layout_edit, null);
        mDialog = new AlertDialog
                .Builder(context)
                .setView(rootView)
                .create();
        TextView titleView = rootView.findViewById(R.id.title);
        titleView.setText(title);
        contentView = rootView.findViewById(R.id.content);
        contentView.setText(content);
        TextView cancelView = rootView.findViewById(R.id.cancel);
        if (cancelListener != null) {
            cancelView.setOnClickListener(cancelListener);
        } else {
            cancelView.setOnClickListener(v -> mDialog.dismiss());
        }
        if (!TextUtils.isEmpty(cancelText)) {
            cancelView.setText(cancelText);
        } else {
            cancelView.setVisibility(View.GONE);
        }
        TextView sureView = rootView.findViewById(R.id.sure);
        if (!TextUtils.isEmpty(sureText)) {
            sureView.setText(sureText);
        } else {
            sureView.setVisibility(View.GONE);
        }
        sureView.setOnClickListener(sureListener);
        mDialog.show();
    }

    public String getContent() {
        return contentView.getText().toString();
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
