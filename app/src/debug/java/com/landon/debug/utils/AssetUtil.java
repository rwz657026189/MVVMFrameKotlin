package com.landon.debug.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author Ren Wenzhang
 * @Date 2022/6/24/024 18:53
 * @Description
 */
public class AssetUtil {
    public static String readFile(String path) {
        try {
            InputStream is = ContextUtils.getContext().getAssets().open(path);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buff = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = bis.read(buff)) > 0) {
                sb.append(new String(buff, 0, len));
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
