package com.landon.debug.view.page;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.landon.debug.DebugConfig;
import com.landon.debug.adapter.NetDevManager;
import com.landon.debug.utils.LogUtil;
import com.landon.debug.utils.StackUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author Ren Wenzhang
 * @Date 2022/6/24/024 19:06
 * @Description
 */
public class ActivityCallback implements Application.ActivityLifecycleCallbacks{
    private static final String TAG = "ActivityPage";

    private static final ActivityCallback instance = new ActivityCallback();

    public static ActivityCallback getInstance() {
        return instance;
    }

    private final Map<String, PageCallback> mCallbacks = new HashMap<>();

    public void register(String clsName, PageCallback callback) {
        if (DebugConfig.SUPPORT_ACTIVITY_INJECT) {
            mCallbacks.put(clsName, callback);
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        StackUtils.safeRun(() -> {
            String name = activity.getClass().getSimpleName();
            // activity参数
            if (DebugConfig.SUPPORT_ACTIVITY_ARGS) {
                Bundle extras = activity.getIntent().getExtras();
                LogUtil.debug(TAG, name + " args : " + LogUtil.toJsonString(extras));
            }
            // debug模式避免截图
            if (DebugConfig.SUPPORT_SCREENSHOT) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
            }
            if (DebugConfig.SUPPORT_SAVED_INSTANCE_STATE && savedInstanceState != null) {
                LogUtil.debug(TAG, name + " saved-state-args : " + LogUtil.toJsonString(savedInstanceState));
            }
        });
    }

    @NonNull
    private String parseBundle(Bundle extras) {
        StringBuilder sb = new StringBuilder();
        if (extras != null) {
            for (String key : extras.keySet()) {
                sb.append(",").append(key).append("=").append(extras.get(key));
            }
        }
        return sb.length() > 0 ? sb.substring(1) : "";
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        String name = activity.getClass().getCanonicalName();
        PageCallback callback = mCallbacks.get(name);
        if (callback != null) {
            StackUtils.safeRun(() -> callback.onStarted(activity));
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        NetDevManager.INSTANCE.addGroup(activity.getClass().getSimpleName());
        if (DebugConfig.SUPPORT_FRAGMENT) {
            StackUtils.safeRun(() -> {
                printFragment(activity);
            });
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    private void printFragment(Activity activity) {
        String name = activity.getClass().getSimpleName();
        if (activity instanceof AppCompatActivity) {
            FragmentManager manager = ((AppCompatActivity) activity).getSupportFragmentManager();
            List<Fragment> list = manager.getFragments();
            if (list.isEmpty()) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\n================== " + name + " ================== ");
            for (Fragment fragment : list) {
                boolean visible = !fragment.isHidden();
                Bundle arguments = fragment.getArguments();
                sb.append("\n")
                        .append(visible ? "☆" : "")
                        .append(fragment.getClass().getSimpleName())
                        .append(" : [")
                        .append(parseBundle(arguments))
                        .append("]");
            }
            String text = sb.length() > 0 ? sb.substring(1) : "";
            LogUtil.debug(TAG, name + " args : " + text);
        } else {
            android.app.FragmentManager manager = activity.getFragmentManager();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                List<android.app.Fragment> list = manager.getFragments();
                if (list.isEmpty()) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("\n================== " + name + " ================== ");
                for (android.app.Fragment fragment : list) {
                    Bundle arguments = fragment.getArguments();
                    sb.append("\n")
                            .append(fragment.getClass().getSimpleName())
                            .append(" : [")
                            .append(parseBundle(arguments))
                            .append("]");
                }
                String text = sb.length() > 0 ? sb.substring(1) : "";
                LogUtil.debug(TAG, name + " args : " + text);
            }
        }
    }
}
