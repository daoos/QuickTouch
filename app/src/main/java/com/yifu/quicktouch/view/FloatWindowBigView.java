package com.yifu.quicktouch.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yifu.quicktouch.MenuListActivity;
import com.yifu.quicktouch.R;
import com.yifu.quicktouch.WebActivity;
import com.yifu.quicktouch.manager.FloatWindowManager;
import com.yifu.quicktouch.manager.FunctionManager;
import com.yifu.quicktouch.utils.LogUtils;
import com.yifu.quicktouch.utils.PhoneStateUtils;
import com.yifu.quicktouch.utils.Utilities;

import java.util.ArrayList;

import static com.yifu.quicktouch.manager.FloatWindowManager.refreshBigWindow;


public class FloatWindowBigView extends LinearLayout implements CircleMenuLayout.OnMenuItemClickListener {

    private static final String TAG = "FloatWindowBigView";
    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;
    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;
    /**
     * 记录不存在的菜单
     */
    private static ArrayList<Integer> firstNoExist = new ArrayList<>();
    /**
     * 记录第二层不存在的菜单
     */
    private static ArrayList<Integer> secondNoExist = new ArrayList<>();
    /**
     * 公共的
     */
    private static ArrayList<Integer> commonNotExist = new ArrayList<>();
    /**
     * 第一层菜单名称和icon
     */
    private static int[] mFirstItemTitle;
    private static int[] mFirstItemIcon;
    /**
     * 第二层菜单名称和icon
     */
    private static int[] mSecondItemIcon;
    private static int[] mSecondItemTitle;
    private Context mContext;
    private PhoneStateUtils mPhoneStateUtils;
    private StateReceiver mStateReceiver;
    private MyObserver mObserver;
    /**
     * 记录状态（是否编辑）
     */
    private boolean isEdited = false;
    /**
     * 记录是否处于更多状态
     */
    private boolean isMore = false;
    private CircleMenuLayout mMenyLayout;
    private FunctionManager mFunction;

    public FloatWindowBigView(final Context context) {
        super(context);
        mContext = context;
        if (mPhoneStateUtils == null) {
            mPhoneStateUtils = PhoneStateUtils.getInstance(mContext);
        }
        if (mFunction == null) {
            mFunction = new FunctionManager(context);
        }

        if (mFirstItemTitle == null) {
            mFirstItemTitle = new int[]{R.string.rotation
                    , R.string.more
                    , R.string.home
                    , R.string.wifi
                    , R.string.call
                    , R.string.data
                    , R.string.lock
                    , R.string.camera};
        }
        if (mSecondItemTitle == null) {
            mSecondItemTitle = new int[]{R.string.gps
                    , R.string.alarm
                    , R.string.bluetooth
                    , R.string.chat
                    , R.string.silent
                    , R.string.contact
                    , R.string.display
                    , R.string.recent};
        }

        if (mFirstItemIcon == null) {
            mFirstItemIcon = new int[]{R.mipmap.rotate,
                    R.mipmap.more,
                    R.mipmap.home,
                    R.mipmap.wifi,
                    R.mipmap.phone,
                    R.mipmap.network,
                    R.mipmap.lockscreen,
                    R.mipmap.photo};
        }

        if (mSecondItemIcon == null) {
            mSecondItemIcon = new int[]{R.mipmap.gps,
                    R.mipmap.clock,
                    R.mipmap.bluetooth,
                    R.mipmap.chat,
                    R.mipmap.quiet,
                    R.mipmap.maillist,
                    R.mipmap.display1,
                    R.mipmap.recentapp};
        }
        LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
        View view = findViewById(R.id.big_window_layout);
        mMenyLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        mMenyLayout.setMenuItemIconsAndTexts(mFirstItemIcon, mFirstItemTitle);
        updateUi(firstNoExist);
        mMenyLayout.setOnMenuItemClickListener(this);
    }

    public static int[] getmFirstItemIcon() {
        return mFirstItemIcon;
    }

    public static void setmFirstItemIcon(int[] itemImgs) {
        mFirstItemIcon = itemImgs;
    }

    public static int[] getmFirstItemTitle() {
        return mFirstItemTitle;
    }

    public static void setmFirstItemTitle(int[] itemTexts) {
        mFirstItemTitle = itemTexts;
    }

    public static int[] getmSecondItemTitle() {
        return mSecondItemTitle;
    }

    public static void setmSecondItemTitle(int[] itemTexts1) {
        mSecondItemTitle = itemTexts1;
    }

    public static int[] getmSecondItemIcon() {
        return mSecondItemIcon;
    }

    public static void setmSecondItemIcon(int[] mSecondItemIcon) {
        FloatWindowBigView.mSecondItemIcon = mSecondItemIcon;
    }

    public static ArrayList<Integer> getFirstNoExist() {
        return firstNoExist;
    }

    public static void setFirstNoExist(ArrayList<Integer> list) {
        firstNoExist = list;
    }

    public static ArrayList<Integer> getSecondNoExist() {
        return secondNoExist;
    }

    public static void setSecondNoExist(ArrayList<Integer> list) {
        secondNoExist = list;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            try {
                FloatWindowManager.removeBigWindow(mContext);
                FloatWindowManager.createSmallWindow(mContext);
                isEdited = false;
                initExitUi();
                return true;
            } catch (Exception e) {

            }
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 刷新ui
     *
     * @param list 空白按钮的位置集合
     */
    private void updateUi(ArrayList list) {
        setNetwork();
        setWifi();
        setBlueTooth();
        setFlightMode();
        setRington();
        setLocation();
        setBright();
        setRatation();
        int j = mMenyLayout.getChildCount();
        for (int i = 0; i < j; i++) {
            View v1 = mMenyLayout.getChildAt(i);
            v1.setEnabled(true);
            TextView v2 = (TextView) v1.findViewById(R.id.id_circle_menu_item_text);
            ImageView v3 = (ImageView) v1.findViewById(R.id.id_circle_menu_item_image);
            ImageView v4 = (ImageView) v1.findViewById(R.id.id_circle_menu_item_delete);
            if (v2 != null && v3 != null && v4 != null) {
                v4.setVisibility(INVISIBLE);
                if (list.contains(i)) {
                    v2.setVisibility(INVISIBLE);
                    v3.setVisibility(INVISIBLE);
                } else {
                    v2.setVisibility(VISIBLE);
                    v3.setVisibility(VISIBLE);
                }
            }
        }
    }

    /**
     * 退出时格式化ui
     */
    private void initExitUi() {
        for (int i = 0; i < mMenyLayout.getChildCount(); i++) {
            View v1 = mMenyLayout.getChildAt(i);
            if (v1.getVisibility() == View.VISIBLE) {
                ImageView v2 = (ImageView) v1.findViewById(R.id.id_circle_menu_item_delete);
                if (isEdited) {
                    if (v1 != null) {
                        v1.setEnabled(false);
                    }
                } else {
                    if (v1 != null) {
                        v1.setEnabled(true);
                    }
                    if (v2 != null && v2.getVisibility() == VISIBLE) {
                        v2.setVisibility(INVISIBLE);
                    }
                    if (isMore) {
                        updateUi(secondNoExist);
                    } else {
                        updateUi(firstNoExist);
                        mMenyLayout.refreshCenterItem(R.drawable.game_center_selector);
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            /**
             * 确定悬浮菜单可点击范围
             */
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < 0
                        || event.getX() > viewWidth
                        || event.getY() < 0
                        || event.getY() > viewHeight) {
                    FloatWindowManager.removeBigWindow(getContext());
                    FloatWindowManager.createSmallWindow(getContext());
                    isEdited = false;
                    initExitUi();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 菜单点击回调
     *
     * @param view
     * @param pos  在菜单集合中的位置
     */
    @Override
    public void itemClick(final View view, final int pos) {
        if (isEdited) {
            FloatWindowManager.removeBigWindow(mContext);
            Intent intent = new Intent(view.getContext(), MenuListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
            MenuListActivity.setItemClick(new MenuListActivity.ItemClick() {
                @Override
                public void onItemClick(int resId, int titleId, Activity activity) {
                    /**
                     * 图片id或字符串id为-1,表示没有选择，不做响应
                     */
                    if (resId == -1 || titleId == -1) {
                        activity.finish();
                        FloatWindowManager.createBigWindow(mContext);
                        return;

                    }
                    if (isMore) {
                        for (int i : mSecondItemTitle) {
                            if (i == titleId) {
                                Toast.makeText(activity, activity.getString(R.string.exist), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    } else {
                        for (int i : mFirstItemTitle) {
                            if (i == titleId) {
                                Toast.makeText(activity, activity.getString(R.string.exist), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    activity.finish();
                    FloatWindowManager.createBigWindow(mContext);
                    mMenyLayout.refreshMenuItem(pos + 1, resId, titleId);
                    if (isMore) {
                        secondNoExist.remove((Object) (pos + 1));
                        mSecondItemTitle[pos] = titleId;
                        mSecondItemIcon[pos] = resId;
                    } else {
                        firstNoExist.remove((Object) (pos + 1));
                        mFirstItemTitle[pos] = titleId;
                        mFirstItemIcon[pos] = resId;
                    }
                    View view1 = mMenyLayout.getChildAt(pos + 1);
                    ImageView v2 = (ImageView) view1.findViewById(R.id.id_circle_menu_item_delete);
                    TextView v3 = (TextView) view1.findViewById(R.id.id_circle_menu_item_text);
                    v2.setVisibility(VISIBLE);
                    v3.setVisibility(VISIBLE);
                    view.setTag(titleId);
                }
            });
            LogUtils.d(TAG, "正在编辑，请选择");
        } else {
            if (!isMore && pos + 1 == 2) {
                /**
                 * 第一层更多菜单的点击事件
                 */
                isMore = true;
                for (int i = 0; i < mSecondItemTitle.length; i++) {
                    mMenyLayout.refreshMenuItem(i + 1, mSecondItemIcon[i], mSecondItemTitle[i]);
                }
                updateUi(secondNoExist);
                mMenyLayout.refreshCenterItem(R.mipmap.back);
                LogUtils.d(TAG, "更多");
            } else {
                LinearLayout v1 = (LinearLayout) mMenyLayout.getChildAt(pos + 1);
                TextView v2 = (TextView) v1.findViewById(R.id.id_circle_menu_item_text);
                if (!isMore) {
                    if (firstNoExist.contains(pos + 1)) {
                        return;
                    }
                } else {
                    if (secondNoExist.contains(pos + 1)) {
                        return;
                    }
                }
                mFunction.start(v1, v2);
                LogUtils.d(TAG, "正在跳转");
            }
        }
    }

    /**
     * 中心菜单按钮的点击事件
     *
     * @param view
     */
    @Override
    public void itemCenterClick(View view) {
        if (isMore) {
            if (!isEdited) {
                view.setBackgroundResource(R.drawable.game_center_selector);
            }
            commonNotExist = secondNoExist;
        } else {
            commonNotExist = firstNoExist;
            view.setBackgroundResource(R.drawable.game_center_selector);
        }
        if (isEdited) {
            isEdited = false;
            updateUi(commonNotExist);
        } else {
            if (isMore) {
                for (int i = 0; i < mFirstItemTitle.length; i++) {
                    mMenyLayout.refreshMenuItem(i + 1, mFirstItemIcon[i], mFirstItemTitle[i]);
                }
                isMore = false;
                updateUi(firstNoExist);
                LogUtils.d(TAG, "返回");
            } else {
                FloatWindowManager.removeBigWindow(mContext);
                FloatWindowManager.createSmallWindow(mContext);
                Intent intent = new Intent(view.getContext(), WebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        }
    }

    /**
     * 菜单按钮长按的响应事件
     *
     * @param view
     * @param pos
     */
    @Override
    public void itemLongClick(View view, int pos) {
        view.setPressed(false);
        //如果处于滑动状态不响应
        if (mMenyLayout.getIsFling()) {
            return;
        }
        if (isMore) {
            commonNotExist = secondNoExist;
        } else {
            mMenyLayout.refreshCenterItem(R.mipmap.back);
            commonNotExist = firstNoExist;
        }
        isEdited = true;
        int j = mMenyLayout.getChildCount();
        for (int i = 0; i < j; i++) {
            View v1 = mMenyLayout.getChildAt(i);
            ImageView v3 = (ImageView) v1.findViewById(R.id.id_circle_menu_item_image);
            if (!isMore && i == 2) {
                v1.setEnabled(false);
                continue;
            }
            ImageView v2 = (ImageView) v1.findViewById(R.id.id_circle_menu_item_delete);
            TextView v4 = (TextView) v1.findViewById(R.id.id_circle_menu_item_text);
            if (v2 != null && v3 != null) {
                if (commonNotExist.contains(i)) {
                    v3.setImageResource(R.mipmap.add);
                    v3.setVisibility(VISIBLE);
                    v4.setVisibility(INVISIBLE);
                } else {
                    v1.setEnabled(false);
                    v2.setEnabled(true);
                    v2.setVisibility(VISIBLE);
                }
            }
        }
    }

    /**
     * 处于编辑状态时，删除按钮的点击事件
     *
     * @param v
     * @param j
     */
    @Override
    public void itemDeleteClick(View v, int j) {
        View v1 = mMenyLayout.getChildAt(j + 1);
        ImageView v2 = (ImageView) v1.findViewById(R.id.id_circle_menu_item_delete);
        ImageView v3 = (ImageView) v1.findViewById(R.id.id_circle_menu_item_image);
        TextView v4 = (TextView) v1.findViewById(R.id.id_circle_menu_item_text);
        v2.setVisibility(INVISIBLE);
        v3.setImageResource(R.mipmap.add);
        v4.setVisibility(INVISIBLE);
        v1.setEnabled(true);
        if (isMore) {
            mSecondItemTitle[j] = 0;
            secondNoExist.add(j + 1);
        } else {
            mFirstItemTitle[j] = 0;
            firstNoExist.add(j + 1);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mStateReceiver == null) {
            mStateReceiver = new StateReceiver();
        }
        if (mObserver == null) {
            mObserver = new MyObserver(new Handler());
        }
        mObserver.startObserver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        filter.addAction("android.intent.action.AIRPLANE_MODE");
        filter.addAction("android.intent.action.ANY_DATA_STATE");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.media.RINGER_MODE_CHANGED");
        filter.addAction("android.location.PROVIDERS_CHANGED");
        filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        filter.addAction(Intent.ACTION_LOCALE_CHANGED);
        mContext.registerReceiver(mStateReceiver, filter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!FloatWindowManager.isWindowShowing()) {
            if (mStateReceiver != null) {
                mContext.unregisterReceiver(mStateReceiver);
            }
            if (mObserver != null) {
                mObserver.stopObserver();
            }
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        refreshBigWindow(mContext);
    }

    private void setItemUi(int resId, int textId, int typeId) {
        int i = Utilities.isContain(mFirstItemTitle, typeId);
        int j = Utilities.isContain(mSecondItemTitle, typeId);
        if (i != -1 && !isMore) {
            mMenyLayout.refreshMenuItem(i + 1, resId, textId);
        } else if (j != -1 && isMore) {
            mMenyLayout.refreshMenuItem(j + 1, resId, textId);
        }
    }

    /**
     * 设置数据样式
     */
    private void setNetwork() {
        if (mPhoneStateUtils.checkPhoneNet() && !mPhoneStateUtils.isAlplaneMode()) {
            if (mPhoneStateUtils.isMobileDataOpen()) {
                setItemUi(R.mipmap.network, 0, R.string.data);
            } else {
                setItemUi(R.mipmap.network2, 0, R.string.data);
            }
        } else {
            setItemUi(R.mipmap.network2, 0, R.string.data);
        }

    }

    /**
     * 设置wifi样式
     */
    private void setWifi() {
        if (mPhoneStateUtils.isWifiOpen()) {
            setItemUi(R.mipmap.wifi, 0, R.string.wifi);
        } else {
            setItemUi(R.mipmap.wifi2, 0, R.string.wifi);
        }
    }

    /**
     * 设置蓝牙样式
     */
    private void setBlueTooth() {
        if (mPhoneStateUtils.isBlueToochOpen()) {
            setItemUi(R.mipmap.bluetooth, 0, R.string.bluetooth);
        } else {
            setItemUi(R.mipmap.bluetooth2, 0, R.string.bluetooth);
        }
    }

    /**
     * 设置飞行模式样式
     */
    private void setFlightMode() {
        if (mPhoneStateUtils.isAlplaneMode()) {
            setItemUi(R.mipmap.airplain, 0, R.string.flightmode);
        } else {
            setItemUi(R.mipmap.airplain2, 0, R.string.flightmode);
        }
    }

    /**
     * 设置铃声样式
     */
    private void setRington() {
        int mode = mPhoneStateUtils.getRingerMode();
        if (AudioManager.RINGER_MODE_NORMAL == mode) {
            setItemUi(R.mipmap.bell, R.string.bell, R.string.silent);
        } else if (AudioManager.RINGER_MODE_VIBRATE == mode) {
            setItemUi(R.mipmap.vibrate, R.string.vibrate, R.string.silent);
        } else {
            setItemUi(R.mipmap.quiet, R.string.silent, R.string.silent);
        }
    }

    /**
     * 设置gps样式
     */
    private void setLocation() {
        if (mPhoneStateUtils.isLocationModeOpen()) {
            setItemUi(R.mipmap.gps, 0, R.string.gps);
        } else {
            setItemUi(R.mipmap.gps2, 0, R.string.gps);
        }
    }

    /**
     * 设置显示样式
     */
    private void setBright() {
        if (mPhoneStateUtils.isAutoBrightness()) {
            setItemUi(R.mipmap.display1, 0, R.string.display);
        } else {
            setItemUi(R.mipmap.display2, 0, R.string.display);
        }
    }

    /**
     * 设置旋转样式
     */
    private void setRatation() {
        if (mPhoneStateUtils.isAutoRatation()) {
            setItemUi(R.mipmap.rotate, 0, R.string.rotation);
        } else {
            setItemUi(R.mipmap.rotate2, 0, R.string.rotation);
        }
    }

    class StateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                setNetwork();
            } else if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                setWifi();
            } else if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                setBlueTooth();
            } else if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                setFlightMode();
                setNetwork();
            } else if ("android.intent.action.ANY_DATA_STATE".equals(action)) {
                setNetwork();
            } else if ("android.media.RINGER_MODE_CHANGED".equals(action)) {
                setRington();
            } else if ("android.location.PROVIDERS_CHANGED".equals(action)) {
                setLocation();
            } else if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
                setWifi();
            } else if (Intent.ACTION_LOCALE_CHANGED.equals(action)) {
                if (isMore) {
                    for (int i1 = 0; i1 < mSecondItemTitle.length; i1++) {
                        mMenyLayout.refreshMenuItem(i1 + 1, 0, mSecondItemTitle[i1]);
                    }
                } else {
                    for (int i1 = 0; i1 < mFirstItemTitle.length; i1++) {
                        mMenyLayout.refreshMenuItem(i1 + 1, 0, mFirstItemTitle[i1]);
                    }
                }
            }
        }
    }

    private class MyObserver extends ContentObserver {
        ContentResolver mResolver;

        public MyObserver(Handler handler) {
            super(handler);
            mResolver = mContext.getContentResolver();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            setBright();
            setRatation();
        }

        public void startObserver() {
            mResolver.registerContentObserver(Settings.System
                            .getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE), false,
                    this);
            mResolver.registerContentObserver(Settings.System
                            .getUriFor(Settings.System.ACCELEROMETER_ROTATION), false,
                    this);
        }

        public void stopObserver() {
            mResolver.unregisterContentObserver(this);
        }
    }
}
