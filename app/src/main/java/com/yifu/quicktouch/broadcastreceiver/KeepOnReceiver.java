package com.yifu.quicktouch.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yifu.quicktouch.service.FloatWindowService;
import com.yifu.quicktouch.utils.LogUtils;

/**
 * Created by yifu on 16-10-18.
 */

public class KeepOnReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.e("startService", "服务重新启动");
        Intent intent1 = new Intent(context, FloatWindowService.class);
        context.startService(intent1);
    }
}
