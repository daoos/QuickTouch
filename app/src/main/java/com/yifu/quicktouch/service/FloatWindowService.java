package com.yifu.quicktouch.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.yifu.quicktouch.manager.FloatWindowManager;
import com.yifu.quicktouch.utils.LogUtils;

/**
 * Created by yifu on 16-9-28.
 */

public class FloatWindowService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.e("startService", "oncreate");
        FloatWindowManager.clear();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }
        }
        if (!FloatWindowManager.isWindowShowing()) {
            FloatWindowManager.createSmallWindow(getApplicationContext());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.e("startService", "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FloatWindowManager.removeSmallWindow(getApplicationContext());
        FloatWindowManager.clear();
    }
}
