package com.yifu.quicktouch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yifu on 16-10-13.
 */

public class MenuListActivity extends AppCompatActivity implements View.OnClickListener {
    private static ItemClick mItemClick;
    private TextView mTvHome;
    private TextView mTvRecent;
    private TextView mTvBluetooth;
    private TextView mTvNetwork;
    private TextView mTvWifi;
    private TextView mTvRotation;
    private TextView mTvGps;
    private TextView mTvAirplane;
    private TextView mTvSetting;
    private TextView mTvSlient;
    private TextView mTvLock;
    private TextView mTvNotification;
    private TextView mTvDisplay;
    private TextView mTvAlarm;
    private TextView mTvCamera;
    private TextView mTvCall;
    private TextView mTvChat;
    private TextView mTvContact;
    private int title;
    private int resId;
    private Toolbar mToolbar;

    public static void setItemClick(ItemClick itemClick) {
        mItemClick = itemClick;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        initBanner();
        initUi();
    }

    private void initBanner() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ImageView back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if (mItemClick != null) {
                    mItemClick.onItemClick(-1, -1, MenuListActivity.this);
                }
            }
        });
    }

    private void initUi() {
        mTvHome = (TextView) findViewById(R.id.tv_home);
        mTvRecent = (TextView) findViewById(R.id.tv_recent);
        mTvBluetooth = (TextView) findViewById(R.id.tv_bluetooth);
        mTvNetwork = (TextView) findViewById(R.id.tv_network);
        mTvWifi = (TextView) findViewById(R.id.tv_wifi);
        mTvRotation = (TextView) findViewById(R.id.tv_rotation);
        mTvGps = (TextView) findViewById(R.id.tv_gps);
        mTvAirplane = (TextView) findViewById(R.id.tv_airplane);
        mTvSetting = (TextView) findViewById(R.id.tv_setting);
        mTvSlient = (TextView) findViewById(R.id.tv_slient);
        mTvLock = (TextView) findViewById(R.id.tv_lock);
        mTvNotification = (TextView) findViewById(R.id.tv_notification);
        mTvDisplay = (TextView) findViewById(R.id.tv_display);
//        mTvSync = (TextView) findViewById(R.id.tv_sync);
//        mTvScrshot = (TextView) findViewById(R.id.tv_scrshot);
        mTvAlarm = (TextView) findViewById(R.id.tv_alarm);
        mTvCamera = (TextView) findViewById(R.id.tv_camera);
        mTvCall = (TextView) findViewById(R.id.tv_call);
        mTvChat = (TextView) findViewById(R.id.tv_chat);
        mTvContact = (TextView) findViewById(R.id.tv_contact);
        mTvHome.setOnClickListener(this);
        mTvRecent.setOnClickListener(this);
        mTvBluetooth.setOnClickListener(this);
        mTvNetwork.setOnClickListener(this);
        mTvWifi.setOnClickListener(this);
        mTvRotation.setOnClickListener(this);
        mTvGps.setOnClickListener(this);
        mTvAirplane.setOnClickListener(this);
        mTvSetting.setOnClickListener(this);
        mTvSlient.setOnClickListener(this);
        mTvLock.setOnClickListener(this);
        mTvNotification.setOnClickListener(this);
        mTvDisplay.setOnClickListener(this);
//        mTvSync.setOnClickListener(this);
//        mTvScrshot.setOnClickListener(this);
        mTvAlarm.setOnClickListener(this);
        mTvCamera.setOnClickListener(this);
        mTvCall.setOnClickListener(this);
        mTvChat.setOnClickListener(this);
        mTvContact.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_home:
                title = R.string.home;
                resId = R.mipmap.home;
                break;
            case R.id.tv_recent:
                title = R.string.recent;
                resId = R.mipmap.recentapp;
                break;
            case R.id.tv_bluetooth:
                title = R.string.bluetooth;
                resId = R.mipmap.bluetooth;
                break;
            case R.id.tv_network:
                title = R.string.data;
                resId = R.mipmap.network;
                break;
            case R.id.tv_wifi:
                title = R.string.wifi;
                resId = R.mipmap.wifi;
                break;
            case R.id.tv_rotation:
                title = R.string.rotation;
                resId = R.mipmap.rotate;
                break;
            case R.id.tv_gps:
                title = R.string.gps;
                resId = R.mipmap.gps;
                break;
            case R.id.tv_airplane:
                title = R.string.flightmode;
                resId = R.mipmap.airplain;
                break;
            case R.id.tv_setting:
                title = R.string.setting;
                resId = R.mipmap.setting;
                break;
            case R.id.tv_slient:
                title = R.string.silent;
                resId = R.mipmap.quiet;
                break;
            case R.id.tv_lock:
                title = R.string.lock;
                resId = R.mipmap.lockscreen;
                break;
            case R.id.tv_notification:
                title = R.string.notification;
                resId = R.mipmap.notification;
                break;
            case R.id.tv_display:
                title = R.string.display;
                resId = R.mipmap.display1;
                break;
            /*case R.id.tv_sync:
                title = R.string.sync;
                resId = R.mipmap.synchronization;
            break;*/
            /*case R.id.tv_scrshot:
                title = R.string.scrshot;
                resId = R.mipmap.screenshot;
            break;*/
            case R.id.tv_alarm:
                title = R.string.alarm;
                resId = R.mipmap.clock;
                break;
            case R.id.tv_camera:
                title = R.string.camera;
                resId = R.mipmap.photo;
                break;
            case R.id.tv_call:
                title = R.string.call;
                resId = R.mipmap.phone;
                break;
            case R.id.tv_chat:
                title = R.string.chat;
                resId = R.mipmap.chat;
                break;
            case R.id.tv_contact:
                title = R.string.contact;
                resId = R.mipmap.maillist;
                break;

        }
        if (mItemClick != null) {
            mItemClick.onItemClick(resId, title, this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mItemClick != null) {
            mItemClick.onItemClick(-1, -1, MenuListActivity.this);
        }
    }

    public interface ItemClick {
        void onItemClick(int resId, int title, Activity activity);
    }
}
