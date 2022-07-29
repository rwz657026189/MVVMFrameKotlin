package com.landon.debug.utils;

import android.text.TextUtils;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    /**
     * 从asset读取文件
     *
     * @param pathName
     * @return
     */
    public static String getValueFromAssets(String pathName) {
        String list = AssetUtil.readFile(pathName);
        if (TextUtils.isEmpty(list)) {
            return "";
        }
        try {
            JSONArray jsonArray = new JSONArray(list);
            int length = jsonArray.length();
            int index = (int) (Math.random() * length);
            return jsonArray.optString(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通过流创建文件
     */
    public static void saveInputStream(InputStream is, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            byte[] buf = new byte[1024 * 4];
            while (is.read(buf) > 0) {
                fos.write(buf, 0, buf.length);
            }
            is.close();
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean writeText(String fileName, String content) {
        if(TextUtils.isEmpty(fileName) || content == null)
            return false;
        FileOutputStream fos = null;
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            if (!mkdirs) {
                return false;
            }
        }
        try {
            fos = new FileOutputStream(fileName);
            fos.write(content.getBytes());
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String readText(String fileName) {
        if(TextUtils.isEmpty(fileName))
            return null;
        File file = new File(fileName);
        if(!file.exists())
            return null;
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
