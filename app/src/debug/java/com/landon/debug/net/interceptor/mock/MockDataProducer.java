package com.landon.debug.net.interceptor.mock;

import android.text.TextUtils;

import com.landon.debug.DebugManager;
import com.landon.debug.utils.FileUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author Ren Wenzhang
 * @Date 2021/7/1/001 10:43
 * @Description mock数据生产者
 */
public class MockDataProducer {
    private static final int MAX_RANDOM_TIME = 30 * 24* 3600_000;

    private String getUsername() {
        return FileUtil.getValueFromAssets("json/mock-data/username.json");
    }

    private String getTitle() {
        return FileUtil.getValueFromAssets("json/mock-data/title.json");
    }

    private String getContent() {
        return FileUtil.getValueFromAssets("json/mock-data/content.json");
    }

    private String getUrl() {
        List<String> randomUrl = DebugManager.mRandImageUrl;
        if (randomUrl == null || randomUrl.isEmpty()) {
            return FileUtil.getValueFromAssets("json/mock-data/urls.json");
        } else {
            return randomUrl.get((int) (Math.random() * randomUrl.size()));
        }
    }

    private long getTimestamp() {
        return  (long) (System.currentTimeMillis() - MAX_RANDOM_TIME + MAX_RANDOM_TIME * 2 * Math.random());
    }

    private String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return formatter.format(new Date(getTimestamp()));
    }

    public JSONObject parseObject(String className) {
        JSONObject jsonObject = new JSONObject();
        if (TextUtils.isEmpty(className)) {
            return jsonObject;
        }
        try {
            Class<?> cls = Class.forName(className);
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                jsonObject.put(fieldName, getValue(field));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private Object getValue(Field field) {
        Class<?> type = field.getType();
        String fieldName = field.getName().toLowerCase();
        if (type == String.class) {
            return getValueByFiledName(fieldName);
        } else if ((type.isArray() && type.getComponentType() == String.class)
        || (type == List.class && isUrlField(fieldName))) {
            JSONArray array = new JSONArray();
            int count = (int) (Math.random() * 9);
            for (int i = 0; i < count; i++) {
                final String value = getValueByFiledName(fieldName);
                array.put(value);
            }
            return array;
        }
        return null;
    }

    private String getValueByFiledName(String fieldName) {
        if (fieldName.contains("title")
                || fieldName.contains("subject")
        ) {
            return getTitle();
        } else if (fieldName.contains("content")) {
            return getContent();
        } else if (
                fieldName.contains("time")
                        || fieldName.contains("deadline")
        ) {
            return getTime();
        } else if (isUrlField(fieldName)) {
            return getUrl();
        } else if (fieldName.contains("name")) {
            return getUsername();
        }
        return null;
    }

    private boolean isUrlField(String fieldName) {
        return fieldName.contains("avatar")
                || fieldName.contains("url")
                || fieldName.contains("image")
                || fieldName.contains("img");
    }

}
