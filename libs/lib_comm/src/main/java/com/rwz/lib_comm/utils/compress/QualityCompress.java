package com.rwz.lib_comm.utils.compress;

import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;


import com.rwz.lib_comm.utils.show.LogUtil;

import java.io.ByteArrayOutputStream;

public class QualityCompress {

    public ByteArrayOutputStream compress(Bitmap bitmap, CompressConfig config) {
        int size = config.getSize() * 1000;
        int realSize = getSize(bitmap);
        if (size <= 0 || realSize <= size) {
            //若需要保存图片，仍然要获得字节流
            if (TextUtils.isEmpty(config.getSavePath())) {
                return null;
            }
            size = Integer.MAX_VALUE;
        }
        final int offset = 5;
        int quality = 100;
        ByteArrayOutputStream outputStream = null;
        while (quality > 0) {
            outputStream = new ByteArrayOutputStream();
            Bitmap.CompressFormat format = config.getFormat();
            bitmap.compress(format == null ? Bitmap.CompressFormat.JPEG : format, quality, outputStream);
            int osSize = outputStream.size();
            if (osSize < size) {
                LogUtil.INSTANCE.d("QualityCompress compress(): " + realSize / 1000 + "kb => " + osSize / 1000 + "kb, quality = " + quality);
                break;
            }
            quality -= offset;
        }
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return outputStream;
    }

    private int getSize(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        } else {
            return bitmap.getByteCount();
        }
    }

}
