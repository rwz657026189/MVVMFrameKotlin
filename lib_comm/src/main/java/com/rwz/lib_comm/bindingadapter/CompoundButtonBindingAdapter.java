package com.rwz.lib_comm.bindingadapter;

import android.widget.CompoundButton;

import androidx.databinding.BindingAdapter;

import com.rwz.lib_comm.entity.params.BiCommandEntity;
import com.rwz.lib_comm.entity.params.CommandEntity;
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity;

import io.reactivex.functions.Consumer;

/**
 * date： 2019/11/30 17:12
 * author： rwz
 * description：
 **/
public class CompoundButtonBindingAdapter {

    @BindingAdapter({"onCheckedChange"})
    public static void onRbCheckChanged(CompoundButton cb, final Consumer command) {
        if(command == null)
            return;
        final int id = cb.getId();
        if (cb.getTag(id) == null) {
            cb.setTag(id, id);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        command.accept(new CommandEntity(id, isChecked));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * android:checked : 必不可少， 省略时，如果checked先调用，如该控件已选中，会调用OnCheckedChanged(), 但此时setTag()还未更改过来
     */
    @BindingAdapter({"onCheckedChange","setEntity", "android:checked"})
    public static void onCheckedChange(final CompoundButton cb, final Consumer<BiCommandEntity<Boolean, IBaseEntity>> command,
                                       IBaseEntity entity, boolean checked) {
        if(command == null)
            return;
        final int id = cb.getId();
        Object tag = cb.getTag(id);
        if (tag == null) {
            cb.setTag(id, entity);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        command.accept(new BiCommandEntity(id, isChecked, cb.getTag(id)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            cb.setTag(id, entity);
        }
        //需要保证调用在setTag()之后
        cb.setChecked(checked);
    }

}
