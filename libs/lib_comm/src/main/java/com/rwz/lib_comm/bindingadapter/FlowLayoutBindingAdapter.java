package com.rwz.lib_comm.bindingadapter;


import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.rwz.lib_comm.R;
import com.rwz.lib_comm.entity.params.CommandEntity;
import com.rwz.lib_comm.ui.widget.vg.FlowLayout;
import com.rwz.lib_comm.utils.app.ResourceUtil;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by rwz on 2017/12/20.
 */

public class FlowLayoutBindingAdapter {

    /**
     * 流式布局添加item
     */
    @BindingAdapter({"childLayout", "data", "clickCommand"})
    public static void addChildView(FlowLayout flowLayout, int childLayout, List data, final Consumer command) {
        if (flowLayout.getChildCount() == 0 && data != null) {
            flowLayout.setHorizontalSpacing(ResourceUtil.getDimen(R.dimen.h_10));
            flowLayout.setVerticalSpacing(ResourceUtil.getDimen(R.dimen.h_10));
            LayoutInflater inflater = LayoutInflater.from(flowLayout.getContext());
            for (int i = 0; i < data.size(); i++) {
                final Object entity = data.get(i);
                ViewDataBinding binding = DataBindingUtil.inflate(inflater, childLayout, null, false);
                binding.setVariable(com.rwz.lib_comm.BR.entity, entity);
                View view = binding.getRoot();
                if (command != null) {
                    final int id = view.getId();
                    view.setOnClickListener(v -> {
                        try {
                            command.accept(new CommandEntity(id, entity));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                flowLayout.addView(view);
            }
        }
    }

}
