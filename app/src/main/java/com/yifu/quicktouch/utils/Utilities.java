package com.yifu.quicktouch.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yifu on 16-10-18.
 */

public class Utilities {
    private static String sCommonFolderName = null;

    public static boolean copyApkFromAssets(Context context, String packageName, String asserPath) {
        boolean copyIsFinish = false;
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + getCommonFolderName(context));
        File file = new File(dir, packageName + ".apk");
        InputStream is = null;
        FileOutputStream os = null;
        if (!file.exists()) {
            try {
                is = context.getAssets().open(asserPath);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                if (file.createNewFile()) {
                    byte[] buffer = new byte[1024];
                    os = new FileOutputStream(file);
                    int count = -1;
                    while ((count = is.read(buffer)) != -1) {
                        os.write(buffer, 0, count);
                    }
                    copyIsFinish = true;
                }
            } catch (IOException e1) {
            } finally {
                try {
                    if (os != null) {
                        os.close();
                        os = null;
                    }
                    if (is != null) {
                        is.close();
                        is = null;
                    }
                } catch (IOException e1) {
                }
            }
        } else {
            copyIsFinish = true;
        }
        return copyIsFinish;
    }

    public static void installNewApp(Context context, String packageName) {
        String apkFilePath = new StringBuilder(Environment
                .getExternalStorageDirectory().getAbsolutePath())
                .append(File.separator).append(getCommonFolderName(context))
                .append(File.separator).append(packageName).append(".apk").toString();
        File file = new File(apkFilePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + apkFilePath), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static String getCommonFolderName(Context context) {
        if (sCommonFolderName == null) {
            sCommonFolderName = context.getPackageName();
        }
        return sCommonFolderName;
    }

    public static int isContain(int[] ints, int i) {
        for (int i1 = 0; i1 < ints.length; i1++) {
            if (ints[i1] == i) {
                return i1;
            }
        }
        return -1;
    }
}
