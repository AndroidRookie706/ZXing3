package com.example.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.loader.content.CursorLoader;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;


public class ZXing3 {

    private static final String TAG = "ZXing3";

    public static void init(Context context) {
        ZXingLibrary.initDisplayOpinion(context);
    }

    public static void openCamera(Activity activity, int request_code) {
        Intent intent = new Intent(activity, CaptureActivity.class);
        activity.startActivityForResult(intent, request_code);
    }

    public static String dispose(Intent data) {
        //处理扫描结果（在界面上显示）
        if (null == data) {
            Log.i(TAG, "dispose: data == null");
            return null;
        }
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            Log.i(TAG, "dispose: bundle == null");
            return null;
        }
        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
            Log.i(TAG, "dispose: identify failed");
            return null;
        }
        return bundle.getString(CodeUtils.RESULT_STRING);

    }


    public static void openImage(Activity activity, int request_code) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(intent, request_code);
    }

    public static void identify(Activity context, Intent data, ZXing3Callback callback) {
        if (data == null) {
            Log.i(TAG, "identify: data == null");
            return;
        }
        Uri uri = data.getData();
        if (uri == null) {
            return;
        }
        String filePath = getRealPathFromUri(context,uri);
        Log.i(TAG, "identify: " + filePath);
        CodeUtils.analyzeBitmap(filePath, callback);
    }

    public static Bitmap newQRCode(Context context, String text, int logo) {
        return CodeUtils.createImage(text, 400, 400, BitmapFactory.decodeResource(context.getResources(), logo));
    }

    public static Bitmap newQRCode(Context context, String text) {
        return CodeUtils.createImage(text, 400, 400, null);
    }




    private static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 11) return getRealPathFromUri_BelowApi11(context, uri);
        if (sdkVersion < 19) return getRealPathFromUri_Api11To18(context, uri);
        else return getRealPathFromUri_AboveApi19(context, uri);
    }

    private static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI,
                new String[]{MediaStore.Images.ImageColumns.DATA},//
                null, null, null);
        if (cursor == null) result = contentURI.getPath();
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }



    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//
                projection, selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }


    private static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }


    private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }


}
