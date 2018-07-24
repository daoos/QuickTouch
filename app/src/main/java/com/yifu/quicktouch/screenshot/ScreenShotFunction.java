package com.yifu.quicktouch.screenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

/**
 * Created by xf on 2014/9/14.
 */
public class ScreenShotFunction {

    private static String mImagePath = "";

    public static Bitmap screenshot(Context context) {
        if (mImagePath.equals("")) {
            mImagePath = Environment.getExternalStorageDirectory() + File.separator + "DCIM" + File.separator + "Screenshot.png";
        }

        Bitmap mScreenBitmap = null;
        WindowManager mWindowManager;
        DisplayMetrics mDisplayMetrics;
        Display mDisplay;

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
        mDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mDisplay.getRealMetrics(mDisplayMetrics);
        }

        float[] dims = {mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels};
        Class<?> demo = null;
        try {
            demo = Class.forName("android.view.SurfaceControl");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Method method = demo.getMethod("screenshot", int.class, int.class);
            mScreenBitmap = (Bitmap) method.invoke(null, (int) dims[0], (int) dims[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mScreenBitmap == null) {
            return null;
        }

        try {
            FileOutputStream out = new FileOutputStream(mImagePath);
            mScreenBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

        } catch (Exception e) {


            return null;
        }

        return mScreenBitmap;
    }
}
