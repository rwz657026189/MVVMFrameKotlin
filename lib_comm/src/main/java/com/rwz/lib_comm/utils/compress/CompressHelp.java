package com.rwz.lib_comm.utils.compress;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.rwz.lib_comm.utils.show.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CompressHelp {

    public static boolean perform(CompressConfig config) {
        if(config == null)
            return false;
        SizeCompress sizeCompress = new SizeCompress();
        //尺寸压缩
        Bitmap bitmap = sizeCompress.compress(config);
        LogUtil.INSTANCE.d("CompressHelp", "perform: " + bitmap);
        if (bitmap == null) {
            return false;
        }
        QualityCompress qualityCompress = new QualityCompress();
        //质量压缩
        ByteArrayOutputStream bos = qualityCompress.compress(bitmap, config);
        boolean result = saveBitmap(bos, config.getSavePath());
        LogUtil.INSTANCE.d("CompressHelp", "perform: result = " + result);
        return result;
    }

    private static boolean saveBitmap(ByteArrayOutputStream bos, String savePath) {
        if (bos == null || TextUtils.isEmpty(savePath)) {
            return false;
        }
        File file = new File(savePath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            return false;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bos.toByteArray());
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
