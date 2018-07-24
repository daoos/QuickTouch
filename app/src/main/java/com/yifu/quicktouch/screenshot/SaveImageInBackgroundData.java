package com.yifu.quicktouch.screenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by yifu on 16-10-19.
 */

public class SaveImageInBackgroundData {
    Context context;
    Bitmap image;
    Uri imageUri;
    Runnable finisher;
    int iconSize;
    int result;

    void clearImage() {
        image = null;
        imageUri = null;
        iconSize = 0;
    }

    void clearContext() {
        context = null;
    }
}