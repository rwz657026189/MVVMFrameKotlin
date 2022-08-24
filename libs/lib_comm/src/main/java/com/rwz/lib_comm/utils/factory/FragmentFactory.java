package com.rwz.lib_comm.utils.factory;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;

import com.rwz.lib_comm.base.BaseDialog;
import com.rwz.lib_comm.base.BaseFragment;

import static com.rwz.lib_comm.config.BaseKeyKt.BOOLEAN;
import static com.rwz.lib_comm.config.BaseKeyKt.INT;
import static com.rwz.lib_comm.config.BaseKeyKt.PARCELABLE_ENTITY;
import static com.rwz.lib_comm.config.BaseKeyKt.STRING;

/**
 * Created by rwz on 2017/7/20.
 */

public class FragmentFactory {

    public static <T extends BaseFragment>T newInstance(Class<T> cl, Parcelable params) {
        T baseFragment = newInstance(cl);
        if(params instanceof Bundle)
            baseFragment.setArguments((Bundle) params);
        else
            putParcelable(baseFragment, params);
        return baseFragment;
    }

    public static <T extends BaseFragment>T newInstance(Class<T> cl, String title) {
        T baseFragment = newInstance(cl);
        putString(title, baseFragment);
        return baseFragment;
    }

    public static <T extends BaseFragment>T newInstance(Class<T> cl, int id) {
        T baseFragment = newInstance(cl);
        putInt(id, baseFragment);
        return baseFragment;
    }

    public static <T extends BaseFragment>T newInstance(Class<T> cl, boolean bool) {
        T baseFragment = newInstance(cl);
        putBoolean(bool, baseFragment);
        return baseFragment;
    }


    /** 获取dialog实例 **/
    public static <T extends BaseDialog>T newDialog(Class<T> cl, Parcelable params) {
        T dialog = newDialog(cl);
        putParcelable(dialog, params);
        return dialog;
    }



    private static void putParcelable(Fragment fragment, Parcelable params) {
        if (fragment != null && params != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(PARCELABLE_ENTITY,params);
            fragment.setArguments(bundle);
        }
    }

    private static void putInt(int id, BaseFragment baseFragment) {
        if (baseFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(INT, id);
            baseFragment.setArguments(bundle);
        }
    }

    private static void putString(String title, BaseFragment baseFragment) {
        if (baseFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(STRING, title);
            baseFragment.setArguments(bundle);
        }
    }

    private static void putBoolean(boolean value, BaseFragment baseFragment) {
        if (baseFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(BOOLEAN, value);
            baseFragment.setArguments(bundle);
        }
    }

    private static <T extends BaseFragment>T newInstance(Class<T> cl) {
        if (cl != null) {
            try {
                return cl.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static <T extends BaseDialog>T  newDialog(Class<T> cl) {
        if (cl != null) {
            try {
                return cl.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
