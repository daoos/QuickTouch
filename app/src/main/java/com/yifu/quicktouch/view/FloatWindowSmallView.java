package com.yifu.quicktouch.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yifu.quicktouch.R;
import com.yifu.quicktouch.manager.FloatWindowManager;
import com.yifu.quicktouch.utils.LogUtils;

import java.lang.reflect.Field;

public class FloatWindowSmallView extends LinearLayout {

    /**
     * 记录小悬浮窗的宽度
     */
    public static int viewWidth;
    /**
     * 记录小悬浮窗的高度
     */
    public static int viewHeight;
    /**
     * 记录系统状态栏的高度
     */
    private static int statusBarHeight;
    private final int HIDE = 0Xeeee;
    private final long DELAY_TIME = 3000;
    /**
     * 用于更新小悬浮窗的位置
     */
    private WindowManager windowManager;
    /**
     * 小悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;
    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;
    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;
    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;
    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;
    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;
    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;
    //单击响应事件的阀值
    private float mTouchSlop = 6;
    //确定是否隐藏
    private boolean isHide = true;
    private ImageView mSmallView;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HIDE) {
                goToHide();
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public FloatWindowSmallView(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
        View view = findViewById(R.id.small_window_layout);
        mSmallView = (ImageView) findViewById(R.id.iv_small);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        ViewConfiguration viewCig = ViewConfiguration.get(context);
        mTouchSlop = viewCig.getScaledTouchSlop();
        LogUtils.d("阀值", mTouchSlop + "");
        sendHideMessage();
    }

    /**
     * 发送隐藏信息
     */
    private void sendHideMessage() {
        mHandler.removeMessages(HIDE);
        Message message = new Message();
        message.what = HIDE;
        mHandler.sendMessageDelayed(message, DELAY_TIME);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        FloatWindowManager.refreshBigWindow(getContext());
        LogUtils.d("切换屏幕:", newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? "横" : "竖");
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        mParams.y = screenHeight / 2;
        goToHide();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSmallView.setAlpha(1f);
                isHide = false;
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                // 手指移动的时候更新小悬浮窗的位置
                // 但在阈值范围内不响应微小移动
                if (!(xDownInScreen > xInScreen - mTouchSlop && xDownInScreen < xInScreen + mTouchSlop && yDownInScreen > yInScreen - mTouchSlop && yDownInScreen < yInScreen + mTouchSlop)) {
                    updateViewPosition();
                }
                break;
            case MotionEvent.ACTION_UP:
                isHide = true;
                // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                if (xDownInScreen > xInScreen - mTouchSlop && xDownInScreen < xInScreen + mTouchSlop && yDownInScreen > yInScreen - mTouchSlop && yDownInScreen < yInScreen + mTouchSlop) {
                    openBigWindow();
                } else {
                    sendHideMessage();
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 缩回到边上
     */
    private void goToHide() {
        if (isHide && getParent() != null) {
            int screenWidth = windowManager.getDefaultDisplay().getWidth();
            if (mParams.x > screenWidth / 2) {
                mParams.x = screenWidth;
            } else {
                mParams.x = 0;
            }
            windowManager.updateViewLayout(this, mParams);
            mSmallView.setAlpha(0.4f);
            //Toast.makeText(getContext(),"隐藏",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }

    /**
     * 打开大悬浮窗，同时关闭小悬浮窗。
     */
    private void openBigWindow() {
        FloatWindowManager.createBigWindow(getContext());
        FloatWindowManager.removeSmallWindow(getContext());
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}
