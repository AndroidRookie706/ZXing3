package com.example.library;


import android.graphics.Bitmap;

import com.uuzuche.lib_zxing.activity.CodeUtils;

public interface ZXing3Callback extends CodeUtils.AnalyzeCallback {

    @Override
    void onAnalyzeSuccess(Bitmap mBitmap, String result);

    @Override
    void onAnalyzeFailed();
}
