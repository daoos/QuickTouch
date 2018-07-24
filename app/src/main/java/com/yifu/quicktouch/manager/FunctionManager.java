package com.yifu.quicktouch.manager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yifu.quicktouch.R;
import com.yifu.quicktouch.utils.FunctionUtils;

/**
 * Created by yifu on 16-10-14.
 */

public class FunctionManager {
    private Context mContext;
    private FunctionUtils mFunctionUtils;

    public FunctionManager(Context context) {
        mContext = context;
        mFunctionUtils = FunctionUtils.getInstance(mContext);
    }

    public void start(View v1, TextView v2) {
        int i = (int) (v1.getTag());
        if (i == R.string.home) {
            FloatWindowManager.removeBigWindow(mContext);
            FloatWindowManager.createSmallWindow(mContext);
            mFunctionUtils.backToHome();

        } else if (i == R.string.recent) {
            FloatWindowManager.removeBigWindow(mContext);
            FloatWindowManager.createSmallWindow(mContext);
            mFunctionUtils.showRecentlyApp();

        } else if (i == R.string.bluetooth) {
            mFunctionUtils.operateBluetooth();

        } else if (i == R.string.data) {
            FloatWindowManager.removeBigWindow(mContext);
            FloatWindowManager.createSmallWindow(mContext);
            mFunctionUtils.operateNetwork();

        } else if (i == R.string.wifi) {
            mFunctionUtils.operateWifi();

        } else if (i == R.string.rotation) {
            mFunctionUtils.operateRotation();

        } else if (i == R.string.gps) {
            FloatWindowManager.removeBigWindow(mContext);
            FloatWindowManager.createSmallWindow(mContext);
            mFunctionUtils.operateGps();

        } else if (i == R.string.flightmode) {
            FloatWindowManager.removeBigWindow(mContext);
            FloatWindowManager.createSmallWindow(mContext);
            mFunctionUtils.operateFlightMode();

        } else if (i == R.string.setting) {
            FloatWindowManager.removeBigWindow(mContext);
            FloatWindowManager.createSmallWindow(mContext);
            mFunctionUtils.openSetting();

        } else if (i == R.string.silent || i == R.string.bell || i == R.string.vibrate) {
            mFunctionUtils.setRingtonMode();

        } else if (i == R.string.lock) {
            FloatWindowManager.removeBigWindow(mContext);
            FloatWindowManager.createSmallWindow(mContext);
            mFunctionUtils.lockScreen();

        } else if (i == R.string.notification) {
            FloatWindowManager.removeBigWindow(mContext);
            FloatWindowManager.createSmallWindow(mContext);
            mFunctionUtils.OpenNotify();

        } else if (i == R.string.display) {
            mFunctionUtils.operateDisplay();

        } else if (i == R.string.sync) {// TODO: 16-10-18

        } else if (i == R.string.scrshot) {
            FloatWindowManager.removeBigWindow(mContext);
            FloatWindowManager.createSmallWindow(mContext);
            // TODO: 16-10-18

        } else if (i == R.string.alarm) {
            FloatWindowManager.removeBigWindow(mContext);
            FloatWindowManager.createSmallWindow(mContext);
            mFunctionUtils.openAlarmas();

        } else if (i == R.string.camera) {
            FloatWindowManager.removeBigWindow(mContext);
            FloatWindowManager.createSmallWindow(mContext);
            mFunctionUtils.openCamera();

        } else if (i == R.string.call) {
            FloatWindowManager.removeBigWindow(mContext);
            FloatWindowManager.createSmallWindow(mContext);
            mFunctionUtils.openCall();

        } else if (i == R.string.chat) {
            FloatWindowManager.removeBigWindow(mContext);
            FloatWindowManager.createSmallWindow(mContext);
            mFunctionUtils.openMessage();

        } else if (i == R.string.contact) {
            FloatWindowManager.removeBigWindow(mContext);
            FloatWindowManager.createSmallWindow(mContext);
            mFunctionUtils.openContact();

        }
    }
}
