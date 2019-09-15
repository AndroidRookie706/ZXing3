package com.example.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

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
        if (data == null) {
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

    public static void identify(Activity context, Intent data, CodeUtils.AnalyzeCallback callback) {
        if (data == null) {
            Log.i(TAG, "identify: data == null");
            return;
        }
        Uri uri = data.getData();
        if (uri == null) {
            return;
        }
        String filePath = uri.getPath();
        Log.i(TAG, "identify: " + filePath);
        CodeUtils.analyzeBitmap(filePath, callback);
    }

    public static Bitmap newQRCode(Context context, String text, int logo) {
        return CodeUtils.createImage(text, 400, 400, BitmapFactory.decodeResource(context.getResources(), logo));
    }

    public static Bitmap newQRCode(Context context, String text ){
        return CodeUtils.createImage(text, 400, 400, null);
    }

}
