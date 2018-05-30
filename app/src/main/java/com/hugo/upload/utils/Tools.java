package com.hugo.upload.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import static com.reikyz.uploaddemo.utils.FileUtils.getDataColumn;

public class Tools {
    private final static int FZ_KB = 1024;
    private final static int FZ_MB = 1024 * FZ_KB;
    private final static int FZ_GB = 1024 * FZ_MB;
    private final static int FZ_PB = 1024 * FZ_GB;

    private final static int TS_SECOND = 1000;
    private final static int TS_MINUTE = 60 * TS_SECOND;
    private final static int TS_HOUR = 60 * TS_MINUTE;

    public static String formatSize(long fileLength) {
        StringBuilder sb = new StringBuilder();
        if (fileLength < FZ_KB) {
            sb.append(formatDouble(fileLength, 1)).append(" B");
        } else if (fileLength <= FZ_MB) {
            sb.append(formatDouble(fileLength, FZ_KB)).append(" KB");
        } else if (fileLength <= FZ_GB) {
            sb.append(formatDouble(fileLength, FZ_MB)).append(" MB");
        } else if (fileLength <= FZ_PB) {
            sb.append(formatDouble(fileLength, FZ_GB)).append(" GB");
        } else {
            sb.append(formatDouble(fileLength, FZ_PB)).append(" PB");
        }
        return sb.toString();
    }

    public static String formatMilliSeconds(long milliSeconds) {
        StringBuilder sb = new StringBuilder();
        long left = milliSeconds;
        if (left / TS_HOUR > 0) {
            sb.append(left / TS_HOUR).append("h ");
            left -= (left / TS_HOUR) * TS_HOUR;
        }
        if (left / TS_MINUTE > 0) {
            sb.append(left / TS_MINUTE).append("m ");
            left -= (left / TS_MINUTE) * TS_MINUTE;
        }
        if (left / TS_SECOND > 0) {
            sb.append(left / TS_SECOND).append("s ");
            left -= (left / TS_SECOND) * TS_SECOND;
        }
        sb.append(left).append("ms ");
        return sb.toString();
    }

    public static String formatDouble(long value, int divider) {
        double result = value * 1.0 / divider;
        return String.format(Locale.getDefault(), "%.2f", result);
    }

    public static String formatSpeed(double deltaSize, double deltaMillis) {
        double speed = deltaSize * 1000 / deltaMillis / FZ_KB;
        String result = String.format(Locale.getDefault(), "%.2f KB/s", speed);
        if ((int) speed > FZ_KB) {
            result = String.format(Locale.getDefault(), "%.2f MB/s", speed
                    / FZ_KB);
        }
        return result;
    }

    public static byte[] sha1(String val) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        byte[] data = val.getBytes("utf-8");
        MessageDigest mDigest = MessageDigest.getInstance("sha1");
        return mDigest.digest(data);
    }


    public static String getLineNumber(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null || trace.length == 0) return -1 + ""; //
        return trace[0].getLineNumber() + "==";
    }

    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);

                final String[] split = docId.split(":");

                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
}
