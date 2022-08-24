package com.rwz.lib_comm.bindingadapter;


import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.databinding.BindingAdapter;

import com.rwz.lib_comm.entity.params.CommandEntity;

import io.reactivex.functions.Consumer;

/**
 * Created by rwz on 2017/3/17.
 */

public class RadioButtonBindingAdapter {

    @BindingAdapter({"rbCheckChanged"})
    public static void onRbCheckChanged(RadioButton rb, final Consumer command) {
        if(command == null)
            return;
        rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    final CommandEntity commandEntity = new CommandEntity(buttonView.getId(), isChecked);
                    command.accept(commandEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
