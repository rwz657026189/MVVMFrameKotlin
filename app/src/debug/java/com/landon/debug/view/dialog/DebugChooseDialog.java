package com.landon.debug.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.landon.debug.inf.InfEvent;
import com.rwz.app.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author Ren Wenzhang
 * @Date 2021/6/25/025 9:50
 * @Description
 */
public class DebugChooseDialog {
    private Context context;
    private String title;
    private List<DebugItemEntity> mData = new ArrayList<>();
    private SimpleAdapter adapter;
    private AlertDialog mDialog;
    private InfEvent<DebugItemEntity> onItemClickListener;
    private String defaultName;

    public static DebugChooseDialog get() {
        return new DebugChooseDialog();
    }

    public DebugChooseDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public DebugChooseDialog addItem(String name) {
        DebugItemEntity entity = new DebugItemEntity();
        entity.name = name;
        mData.add(entity);
        return this;
    }

    public DebugChooseDialog addItem(DebugItemEntity entity) {
        mData.add(entity);
        return this;
    }

    public DebugChooseDialog addAllItem(List<DebugItemEntity> list) {
        if (list != null && !list.isEmpty()) {
            mData.addAll(list);
        }
        return this;
    }

    public DebugChooseDialog addAllItemName(Collection<String> list) {
        if (list != null && !list.isEmpty()) {
            for (String name : list) {
                addItem(name);
            }
        }
        return this;
    }

    public DebugChooseDialog setDefault(String name) {
        defaultName = name;
        return this;
    }

    public void show(Context context, InfEvent<DebugItemEntity> onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        View rootView = LayoutInflater.from(context).inflate(R.layout.debug_dialog_layout_choose, null);
        mDialog = new AlertDialog
                .Builder(context)
                .setView(rootView)
                .create();
        TextView titleView = rootView.findViewById(R.id.title);
        titleView.setText(title);
        ListView listView = rootView.findViewById(R.id.list);
        adapter = new SimpleAdapter();
        listView.setAdapter(adapter);
        mDialog.show();
    }

    private class SimpleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.debug_item_dialog, null);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            DebugItemEntity entity = mData.get(position);
            holder.name.setText(entity.name);
            boolean isChecked = TextUtils.equals(defaultName, entity.name);
            holder.name.setTextColor(Color.parseColor(isChecked ? "#5290FD" : "#666666"));
            convertView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.perform(entity);
                }
                mDialog.dismiss();
            });
            return convertView;
        }
    }

    private static class Holder {
        TextView name;

        public Holder(View rootView) {
            this.name = rootView.findViewById(R.id.name);
        }
    }
}
