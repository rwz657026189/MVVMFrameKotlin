package com.landon.debug.view.dialog;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

/**
 * @Author Ren Wenzhang
 * @Date 2021/6/25/025 9:50
 * @Description
 */
public class ChooseDialog {
    public static class Entity {
        String title;
    }

    public static void show(Context context, Entity entity) {
        AlertDialog dialog = new AlertDialog
                .Builder(context)
                .setTitle(entity.title)
//                .setAdapter(new SimpleAdapter(context, null, ))
                .create();
    }


}
