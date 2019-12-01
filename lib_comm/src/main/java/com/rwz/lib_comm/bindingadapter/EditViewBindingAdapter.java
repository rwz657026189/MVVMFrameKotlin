package com.rwz.lib_comm.bindingadapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.rwz.lib_comm.entity.params.CommandEntity;

import io.reactivex.functions.Consumer;


/**
 * Created by rwz on 2017/3/24.
 */

public class EditViewBindingAdapter {

    @BindingAdapter("afterTextChanged")
    public static void afterTextChanged(final EditText view, final Consumer<CommandEntity> command) {
        if(command == null)
            return;
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                try {
                    command.accept(new CommandEntity<>(view.getId(), s));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @BindingAdapter("onEnterKeyDown")
    public static void onEnterKeyDown(final EditText view, final Consumer<CommandEntity> command) {
        view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if( event != null && EditorInfo.IME_ACTION_SEARCH == event.getAction()){
                    try {
                        command.accept(new CommandEntity(v.getId(), v.getText() + ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event != null && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    try {
                        command.accept(new CommandEntity(v.getId(), ((TextView)v).getText() + ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

}
