package com.yifu.quicktouch.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;

/**
 * Created by yifu on 16-10-24.
 */

public class FunctionUtils {
    private static FunctionUtils mFunctionUtils;
    private PhoneStateUtils mPhoneStateUtils;
    private Context mContext;

    private FunctionUtils(Context context) {
        mContext = context;
        mPhoneStateUtils = PhoneStateUtils.getInstance(mContext);
    }

    public static FunctionUtils getInstance(Context context) {
        if (mFunctionUtils == null) {
            synchronized (FunctionUtils.class) {
                if (mFunctionUtils == null) {
                    mFunctionUtils = new FunctionUtils(context);
                }
            }
        }
        return mFunctionUtils;

    }

    /**
     * 返回到主桌面
     */
    public void backToHome() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        home.addCategory(Intent.CATEGORY_HOME);
        mContext.startActivity(home);
    }

    /**
     * 开启最近应用列表
     */
    public void showRecentlyApp() {
        Class serviceManagerClass;
        try {
            serviceManagerClass = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClass.getMethod("getService",
                    String.class);
            IBinder retbinder = (IBinder) getService.invoke(
                    serviceManagerClass, "statusbar");
            Class statusBarClass = Class.forName(retbinder
                    .getInterfaceDescriptor());
            Object statusBarObject = statusBarClass.getClasses()[0].getMethod(
                    "asInterface", IBinder.class).invoke(null,
                    retbinder);
            Method clearAll = statusBarClass.getMethod("toggleRecentApps");
            clearAll.setAccessible(true);
            clearAll.invoke(statusBarObject);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 操作蓝牙
     */
    public void operateBluetooth() {
        if (mPhoneStateUtils.isBlueToochOpen()) {
            mPhoneStateUtils.mBluetoothAdapter.disable();
        } else {
            mPhoneStateUtils.mBluetoothAdapter.enable();
        }
    }

    /**
     * 操作移动数据
     */
    public void operateNetwork() {
        if (mPhoneStateUtils.checkPhoneNet()) {
            if (mPhoneStateUtils.isMobileDataOpen()) {
                setMobileDataStatus(false);
            } else {
                setMobileDataStatus(true);
            }
        } else {
            Toast.makeText(mContext, "No SIM card!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setMobileDataStatus(boolean enabled) {
        Method setMobileDataEnabl;
        try {
            setMobileDataEnabl = mPhoneStateUtils.mConnectivityManager.getClass().getDeclaredMethod("setMobileDataEnabled", boolean.class);
            setMobileDataEnabl.invoke(mPhoneStateUtils.mConnectivityManager, enabled);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Intent dataIntent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                dataIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(dataIntent);
            } catch (Exception e2) {
                e2.printStackTrace();
                Toast.makeText(mContext, "Due to system limitations, cannot use!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 操作wifi
     */
    public void operateWifi() {
        if (mPhoneStateUtils.isWifiOpen()) {
            mPhoneStateUtils.mWifiManager.setWifiEnabled(false);
        } else {
            setWifiApEnabled(false);
            mPhoneStateUtils.mWifiManager.setWifiEnabled(true);
        }
    }

    private boolean setWifiApEnabled(boolean enabled) {
        if (enabled) {
            mPhoneStateUtils.mWifiManager.setWifiEnabled(false);
        }
        try {
            WifiConfiguration apConfig = new WifiConfiguration();
            apConfig.SSID = "wifi";
            apConfig.preSharedKey = "123456789";
            Method method = mPhoneStateUtils.mWifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            return (Boolean) method.invoke(mPhoneStateUtils.mWifiManager, apConfig, enabled);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 操作旋转
     */
    public void operateRotation() {
        if (mPhoneStateUtils.isAutoRatation()) {
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        } else {
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
        }
    }

    /**
     * 操作gps
     */
    public void operateGps() {
        try {
            Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            locationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            locationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(locationIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Due to system limitations, cannot use!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 操作飞行模式
     */
    public void operateFlightMode() {
        try {
            Intent airplaneIntent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            airplaneIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            airplaneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(airplaneIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Due to system limitations, cannot use!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开设置
     */
    public void openSetting() {
        try {
            Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
            settingsIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(settingsIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Due to system limitations, cannot use!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置情景模式
     */
    public void setRingtonMode() {
        int mode = mPhoneStateUtils.getRingerMode();
        if (AudioManager.RINGER_MODE_NORMAL == mode) {
            mPhoneStateUtils.mVibrator.vibrate(350);
            mPhoneStateUtils.mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            mPhoneStateUtils.mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
            mPhoneStateUtils.mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
        } else if (AudioManager.RINGER_MODE_VIBRATE == mode) {
            mPhoneStateUtils.mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            mPhoneStateUtils.mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
            mPhoneStateUtils.mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
        } else {
            mPhoneStateUtils.mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            mPhoneStateUtils.mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
            mPhoneStateUtils.mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
        }
    }

    /**
     * 打开通知栏
     */
    public void OpenNotify() {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        try {
            Object service = mContext.getSystemService("statusbar");
            Class<?> statusbarManager = Class
                    .forName("android.app.StatusBarManager");
            Method expand = null;
            if (service != null) {
                if (currentApiVersion <= 16) {
                    expand = statusbarManager.getMethod("expand");
                } else {
                    expand = statusbarManager
                            .getMethod("expandNotificationsPanel");
                }
                expand.setAccessible(true);
                expand.invoke(service);
            }

        } catch (Exception e) {
            Toast.makeText(mContext, "Due to system limitations, cannot use!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 锁屏
     */
    public void lockScreen() {
        Toast.makeText(mContext, "锁屏", Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置显示
     */
    public void operateDisplay() {
        if (!mPhoneStateUtils.isAutoBrightness()) {
            Settings.System.putInt(mContext.getContentResolver(),
                    SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        } else {
            Settings.System.putInt(mContext.getContentResolver(),
                    SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_MANUAL);
        }
    }

    /**
     * 打开闹钟
     */
    public void openAlarmas() {
        Intent alarmas = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarmas.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(alarmas);
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(camera);
    }

    /**
     * 打开拨号
     */
    public void openCall() {
        Intent call = new Intent(Intent.ACTION_DIAL);
        call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(call);
    }

    /**
     * 打开短信
     */
    public void openMessage() {
        Uri smsToUri = Uri.parse("smsto:");
        Intent chat = new Intent(Intent.ACTION_SENDTO, smsToUri);
        chat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(chat);
    }

    /**
     * 打开联系人
     */
    public void openContact() {
        Intent contact = new Intent();
        contact.setAction(Intent.ACTION_PICK);
        contact.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        contact.setData(ContactsContract.Contacts.CONTENT_URI);
        mContext.startActivity(contact);
    }
}
