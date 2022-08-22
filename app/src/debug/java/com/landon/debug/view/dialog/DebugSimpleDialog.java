package com.landon.debug.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

/**
 * @Author Ren Wenzhang
 * @Date 2021/6/25/025 9:50
 * @Description
 */
public class DebugSimpleDialog {
    public static AlertDialog show(Context context, String content, DialogInterface.OnClickListener sureListener) {
        return new AlertDialog
                .Builder(context)
                .setTitle("提示")
                .setMessage(content)
                .setPositiveButton("取消", (dialog1, which) -> dialog1.dismiss())
                .setPositiveButton("确定", sureListener)
                .create();
    }
}
