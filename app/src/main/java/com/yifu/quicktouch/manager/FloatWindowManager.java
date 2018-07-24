package com.yifu.quicktouch.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.yifu.quicktouch.utils.PreferencesUtils;
import com.yifu.quicktouch.view.FloatWindowBigView;
import com.yifu.quicktouch.view.FloatWindowSmallView;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.Collection;

public class FloatWindowManager {

    /**
     * bigview第一层不存在的位置集合
     */
    private final static String FIRST_NO_EXIST = "firstNoExist";
    /**
     * bigview第二层不存在的位置集合
     */
    private final static String SECOND_NO_EXIST = "secondNoExist";
    /**
     * bigview第一层图表和标题（保存标记）
     */
    private final static String FIRST_TITLE = "first_title";
    private final static String FIRST_ICON = "first_icon";
    /**
     * bigview第二层图表和标题（保存标记）
     */
    private final static String SECOND_TITLE = "second_title";
    private final static String SECOND_ICON = "second_icon";
    /**
     * 小悬浮窗View的实例
     */
    private static FloatWindowSmallView smallWindow;
    /**
     * 大悬浮窗View的实例
     */
    private static FloatWindowBigView bigWindow;
    /**
     * 小悬浮窗View的参数
     */
    private static LayoutParams smallWindowParams;
    /**
     * 大悬浮窗View的参数
     */
    private static LayoutParams bigWindowParams;
    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;
    private static boolean NO_INSTALL = true;


    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createSmallWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (smallWindow == null) {
            smallWindow = new FloatWindowSmallView(context);
            if (smallWindowParams == null) {
                smallWindowParams = new LayoutParams();
                smallWindowParams.type = LayoutParams.TYPE_PHONE;
                smallWindowParams.format = PixelFormat.RGBA_8888;
                smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                smallWindowParams.width = FloatWindowSmallView.viewWidth;
                smallWindowParams.height = FloatWindowSmallView.viewHeight;
                smallWindowParams.x = screenWidth;
                smallWindowParams.y = screenHeight / 2;
            }
            smallWindow.setParams(smallWindowParams);
            windowManager.addView(smallWindow, smallWindowParams);
        }
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeSmallWindow(Context context) {
        if (smallWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(smallWindow);
            smallWindow = null;
        }
    }

    /**
     * 创建一个大悬浮窗。位置为屏幕正中间。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createBigWindow(final Context context) {
        WindowManager windowManager = getWindowManager(context);
        if (bigWindow == null) {
            String s1 = PreferencesUtils.getString(context, FIRST_NO_EXIST);
            String s2 = PreferencesUtils.getString(context, SECOND_NO_EXIST);
            ArrayList<Integer> list1 = stringToList(s1);
            ArrayList<Integer> list2 = stringToList(s2);
            if (list1 != null) {
                FloatWindowBigView.setFirstNoExist(list1);
            }
            if (list2 != null) {
                FloatWindowBigView.setSecondNoExist(list2);
            }
            if (NO_INSTALL) {
                int[] firstString = PreferencesUtils.getIntArray(context, FIRST_TITLE, 8);
                int[] secondString = PreferencesUtils.getIntArray(context, SECOND_TITLE, 8);
                int[] firstImage = PreferencesUtils.getIntArray(context, FIRST_ICON, 8);
                int[] secondImage = PreferencesUtils.getIntArray(context, SECOND_ICON, 8);
                if (firstString.length > 0 && firstString[0] != 0) {
                    FloatWindowBigView.setmFirstItemTitle(firstString);
                }
                if (secondString.length > 0 && secondString[0] != 0) {
                    FloatWindowBigView.setmSecondItemTitle(secondString);
                }
                if (firstImage.length > 0 && firstImage[0] != 0) {
                    FloatWindowBigView.setmFirstItemIcon(firstImage);
                }
                if (secondImage.length > 0 && secondImage[0] != 0) {
                    FloatWindowBigView.setmSecondItemIcon(secondImage);
                }
            } else {
                NO_INSTALL = true;
            }
            bigWindow = new FloatWindowBigView(context);
        }
        createBigWindowParams(context);
        if (bigWindow.getParent() == null) {
            windowManager.addView(bigWindow, bigWindowParams);
        }
        startAnimation(context);
    }

    private static void createBigWindowParams(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (bigWindowParams == null) {
            bigWindowParams = new LayoutParams();
            bigWindowParams.x = screenWidth / 2 - FloatWindowBigView.viewWidth / 2;
            bigWindowParams.y = screenHeight / 2 - FloatWindowBigView.viewHeight / 2;
            bigWindowParams.type = LayoutParams.TYPE_PHONE;
            bigWindowParams.format = PixelFormat.RGBA_8888;
            bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
            bigWindowParams.width = FloatWindowBigView.viewWidth;
            bigWindowParams.height = FloatWindowBigView.viewHeight;
        } else {
            bigWindowParams.x = screenWidth / 2 - FloatWindowBigView.viewWidth / 2;
            bigWindowParams.y = screenHeight / 2 - FloatWindowBigView.viewHeight / 2;
        }
    }

    /**
     * 刷新bigview的位置
     *
     * @param context
     */
    public static void refreshBigWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        createBigWindowParams(context);
        if (bigWindow != null && bigWindow.getParent() != null) {
            windowManager.updateViewLayout(bigWindow, bigWindowParams);
        }
    }

    /**
     * 将大悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeBigWindow(Context context) {
        if (bigWindow != null && bigWindow.getParent() != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(bigWindow);
            saveNotExist(context, FloatWindowBigView.getFirstNoExist(), FIRST_NO_EXIST);
            saveNotExist(context, FloatWindowBigView.getSecondNoExist(), SECOND_NO_EXIST);
            PreferencesUtils.putIntArray(context, FIRST_TITLE, FloatWindowBigView.getmFirstItemTitle());
            PreferencesUtils.putIntArray(context, SECOND_TITLE, FloatWindowBigView.getmSecondItemTitle());
            PreferencesUtils.putIntArray(context, FIRST_ICON, FloatWindowBigView.getmFirstItemIcon());
            PreferencesUtils.putIntArray(context, SECOND_ICON, FloatWindowBigView.getmSecondItemIcon());
        }
    }

    public static void setNoInstall(boolean s) {
        NO_INSTALL = s;
    }

    private static ArrayList<Integer> stringToList(String s) {
        if (s != null && !s.isEmpty()) {
            String[] ss = s.split(",");
            ArrayList<Integer> list = new ArrayList<>();
            for (String string : ss) {
                list.add(Integer.parseInt(string));
            }
            return list;
        }
        return null;
    }

    private static void saveNotExist(Context context, ArrayList<Integer> list, String key) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                s.append(list.get(i));
            } else {
                s.append(list.get(i)).append(",");
            }
        }
        PreferencesUtils.putString(context, key, s.toString());
    }

    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return smallWindow != null || bigWindow != null;
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context 必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 释放view
     */
    public static void clear() {
        bigWindow = null;
    }

    /**
     * view出场动画
     *
     * @param context
     */
    private static void startAnimation(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        float x = smallWindowParams.x - screenWidth / 2f;
        float y = smallWindowParams.y - screenHeight / 2f;
        Collection<Animator> c = new ArrayList<>();
        ObjectAnimator a1 = ObjectAnimator.ofFloat(bigWindow, "translationX", x, 0f);
        a1.setInterpolator(new AccelerateDecelerateInterpolator());
        c.add(a1);
        ObjectAnimator a2 = ObjectAnimator.ofFloat(bigWindow, "translationY", y, 0f);
        a2.setInterpolator(new AccelerateDecelerateInterpolator());
        c.add(a2);
        ObjectAnimator a3 = ObjectAnimator.ofFloat(bigWindow, "scaleX", 0f, 1f);
        c.add(a3);
        ObjectAnimator a4 = ObjectAnimator.ofFloat(bigWindow, "scaleY", 0f, 1f);
        c.add(a4);
        for (int i = 0; i < bigWindow.getChildCount(); i++) {
            View v = bigWindow.getChildAt(i);
            ObjectAnimator itemX = ObjectAnimator.ofFloat(v, "scaleX", 0f, 1f);
            itemX.setInterpolator(new AccelerateDecelerateInterpolator());
            c.add(itemX);
            ObjectAnimator itemY = ObjectAnimator.ofFloat(v, "scaleY", 0f, 1f);
            itemY.setInterpolator(new AccelerateDecelerateInterpolator());
            c.add(itemY);
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(c);
        set.setDuration(600);
        set.start();
    }
}
