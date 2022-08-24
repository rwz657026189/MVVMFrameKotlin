package com.rwz.lib_comm.utils.system;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.rwz.lib_comm.manager.ContextManager;
import com.rwz.lib_comm.utils.show.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件操作工具 可以创建和删除文件等
 */
public class FileUtil {
    private static final String KB = "KB";
    private static final String MB = "MB";
    private static final String GB = "GB";

    private FileUtil() {
    }

    private static final String TAG = "FileUtil";

    /**
     * 是否有读写权限
     * @return
     */
    private static boolean hasWritePermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || 
                ContextCompat.checkSelfPermission(ContextManager.context, 
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 获取文件夹大小
     */
    public static long getDirSize(String filePath) {
        if (!hasWritePermission())
            return 0L;

        long size = 0;
        File f = new File(filePath);
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (File file : files) {
                if(file == null)
                    continue;
                if (file.isDirectory()) {
                    size += getDirSize(file.getPath());
                    continue;
                }
                size += file.length();
            }
        } else {
            size += f.length();
        }
        return size;
    }

    /**
     * 存储bitmap
     */
    public static boolean saveBitmap(@NonNull String filePath, @NonNull Bitmap bitmap) {
        File file = createFile(filePath);
        FileOutputStream os = null;
        try {
            if(file == null)
                return false;
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 格式化文件大小
     *
     * @param size file.length() 获取文件大小
     * @return
     */
    public static String formatFileSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 获取文件后缀名
     */
    public static String getFileExtensionName(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        int endP = fileName.lastIndexOf(".");
        return endP > -1 ? fileName.substring(endP + 1, fileName.length()) : "";
    }

    /**
     * 根据路径获取文件名
     */
    public static String getFileName(String filePath){
        int start = filePath.lastIndexOf("/");
        int end = filePath.lastIndexOf(".");
        if(start != -1 && end != -1 && start < end){
            return filePath.substring(start + 1, end);
        }else{
            return "";
        }
    }

    /**
     * 校验文件MD5码
     *
     * @param md5
     * @param updateFile
     * @return
     */
    public static boolean checkMD5(String md5, File updateFile) {
        if (TextUtils.isEmpty(md5) || updateFile == null) {
            LogUtil.INSTANCE.e(TAG, "MD5 string empty or updateFile null");
            return false;
        }

        String calculatedDigest = getFileMD5(updateFile);
        if (calculatedDigest == null) {
            LogUtil.INSTANCE.e(TAG, "calculatedDigest null");
            return false;
        }
        return calculatedDigest.equalsIgnoreCase(md5);
    }

    /**
     * 获取文件MD5码
     *
     * @param updateFile
     * @return
     */
    public static String getFileMD5(File updateFile) {
        if (updateFile == null || !updateFile.exists()) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            LogUtil.INSTANCE.e(TAG, "Exception while getting digest", e);
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(updateFile);
        } catch (FileNotFoundException e) {
            LogUtil.INSTANCE.e(TAG, "Exception while getting FileInputStream", e);
            return null;
        }

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                LogUtil.INSTANCE.e(TAG, "Exception on closing MD5 input stream", e);
            }
        }
    }

    /**
     * 通过流创建文件
     */
    public static void createFileFormInputStream(InputStream is, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            byte[] buf = new byte[1024 * 4];
            while (is.read(buf) > 0) {
                fos.write(buf, 0, buf.length);
            }
            is.close();
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件 当文件不存在的时候就创建一个文件，否则直接返回文件
     *
     * @param path
     * @return
     */
    public static File createFile(String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            LogUtil.INSTANCE.d(TAG, "目标文件所在路径不存在，准备创建……");
            if (!createDir(file.getParent())) {
                LogUtil.INSTANCE.d(TAG, "创建目录文件所在的目录失败！文件路径【" + path + "】");
            }
        }
        // 创建目标文件
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    LogUtil.INSTANCE.d(TAG, "创建文件成功:" + file.getAbsolutePath());
                }
                return file;
            } else {
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建文件 当文件不存在的时候就创建一个文件
     * @param path
     * @param isDelIfExit 如果存在是否清除并重新创建文件
     * @return
     */
    public static File createFile(String path, boolean isDelIfExit) {
        File file = new File(path);
        File parentFile = file.getParentFile();
        if (parentFile == null) {
            LogUtil.INSTANCE.d(TAG, "parentFile is null , path = " + path);
            return null;
        }
        if (!parentFile.exists()) {
            LogUtil.INSTANCE.d(TAG, "目标文件所在路径不存在，准备创建……");
            if (!createDir(file.getParent())) {
                LogUtil.INSTANCE.d(TAG, "创建目录文件所在的目录失败！文件路径【" + path + "】");
            }
        }
        // 创建目标文件
        try {
            if (file.exists()) {
                if (isDelIfExit) {
                    file.delete();
                } else {
                    return file;
                }
            }
            if (!file.exists()) {
                if (file.createNewFile()) {
                    LogUtil.INSTANCE.d(TAG, "创建文件成功:" + file.getAbsolutePath());
                }
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 进创建父目录
     * @param filePath 文件目录
     * @param isDelIfExist 如果存在是否删除文件
     * @return
     */
    public static File createParentFile(String filePath, boolean isDelIfExist) {
        LogUtil.INSTANCE.d("createParentFile", "filePath = " + filePath, "isDelIfExist = " + isDelIfExist);
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if (parentFile == null) {
            return null;
        }
        if (!parentFile.exists()) {
            boolean result = parentFile.mkdirs();
            if (!result) {
                return null;
            }
        } else if (file.exists() && isDelIfExist) {
            file.delete();
            return file;
        }
        return file;
    }

    /**
     * 创建目录 当目录不存在的时候创建文件，否则返回false
     *
     * @param path
     * @return
     */
    public static boolean createDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                LogUtil.INSTANCE.d(TAG, "创建失败，请检查路径和是否配置文件权限！");
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 拷贝文件
     *
     * @param fromPath
     * @param toPath
     * @return
     */
    public static boolean copy(String fromPath, String toPath) {
        File file = new File(fromPath);
        if (!file.exists()) {
            return false;
        }
        createFile(toPath);
        return copyFile(fromPath, toPath);
    }

    /**
     * 拷贝文件
     *
     * @param fromFile
     * @param toFile
     * @return
     */
    private static boolean copyFile(String fromFile, String toFile) {
        InputStream fosfrom = null;
        OutputStream fosto = null;
        try {
            fosfrom = new FileInputStream(fromFile);
            fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (fosfrom != null) {
                    fosfrom.close();
                }
                if (fosto != null) {
                    fosto.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 删除文件 如果文件存在删除文件，否则返回false
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        boolean result = false;
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (file.exists()) {
            LogUtil.INSTANCE.d("删除文件 存在");
            result = file.delete();
        }
        LogUtil.INSTANCE.d("删除文件 path = " + path , " result = " + result);
        return result;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return 删除成功返回true，否则返回false,如果文件是空，那么永远返回true
     */
    public static boolean deleteDir(File dir) {
        if (dir == null) {
            return true;
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /** 获取目录下文件总数 **/
    public static int getFileCount(File dir) {
        if(dir == null)
            return 0;
        if(!dir.exists())
            return 0;
        int fileCount = 0;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            fileCount = children == null ? 0 : children.length;
        } else {
            fileCount = 1;
        }
        return fileCount;
    }

    /**
     * 递归返回文件或者目录的大小（单位:KB）
     * 不建议使用这个方法，有点坑
     * 可以使用下面的方法：http://blog.csdn.net/loongggdroid/article/details/12304695
     *
     * @param path
     * @param size
     * @return
     */
    private static float getSize(String path, Float size) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (String aChildren : children) {
                    float tmpSize = getSize(file.getPath() + File.separator + aChildren, size) / 1000;
                    size += tmpSize;
                }
            } else if (file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }

    /**
     * 获取apk文件的icon
     *
     * @param context
     * @param path    apk文件路径
     * @return
     */
    public static Drawable getApkIcon(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            //android有bug，需要下面这两句话来修复才能获取apk图片
            appInfo.sourceDir = path;
            appInfo.publicSourceDir = path;
//			    String packageName = appInfo.packageName;  //得到安装包名称
//	            String version=info.versionName;       //得到版本信息
            return pm.getApplicationIcon(appInfo);
        }
        return null;
    }

    /**
     * 根据uri获取当前路径
     * http://blog.csdn.net/mikogodzd/article/details/50979653
     */
    public static String getFilePathFromUri( Context context, Uri uri ) {
        if (context == null) return null;
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals(scheme) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] {MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    /**
     * 根据Uri获取文件的绝对路径，解决Android4.4以上版本Uri转换
     * @param fileUri
     */
    public static String getFileAbsolutePath(Context context, Uri fileUri) {
        if (context == null || fileUri == null)
            return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                DocumentsContract.isDocumentUri(context, fileUri)) {
            if (isExternalStorageDocument(fileUri)) {
                String docId = DocumentsContract.getDocumentId(fileUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(fileUri)) {
                String id = DocumentsContract.getDocumentId(fileUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(fileUri)) {
                String docId = DocumentsContract.getDocumentId(fileUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                switch (type) {
                    case "image":
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "video":
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "audio":
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        break;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(fileUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(fileUri))
                return fileUri.getLastPathSegment();
            return getDataColumn(context, fileUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(fileUri.getScheme())) {
            return fileUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Uri getUri(File file) {
        if (file == null) {
            return null;
        }
        Uri uri;
        if(Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            Context context = ContextManager.context;
            String packageName = AndroidUtils.getPackageName(context);
            uri = FileProvider.getUriForFile(context, packageName + ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        LogUtil.INSTANCE.d("getUri", "uri = " + uri);
        return uri;
    }


}
