package com.keyboard.monitor;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

public class KeyboardHelper {

    private static final Rect windowRect = new Rect();
    private static Map<String, ViewTreeObserver.OnGlobalLayoutListener> onLayoutListeners = new HashMap<>();

    private static int sKeyboardMinHeight = -1;
    private static int sKeyboardHeight = -1;

    /**
     * 可以自己设置最小高度判断
     *
     * @param sMinHeight
     */
    public static void initMinHeight(int sMinHeight) {
        KeyboardHelper.sKeyboardMinHeight = sMinHeight;
    }

    /**
     * 注册键盘监听器
     *
     * @param window      当前所在的window,可以在dialog中使用
     * @param tag         标记
     * @param isLandscape 是否横屏状态
     * @param listener    回调
     */
    public static void registerKeyboardListener(Window window, String tag, boolean isLandscape, final OnKeyboardListener listener) {
        if (window == null || tag == null || listener == null) {
            return;
        }
        //先获取高度
        WindowManager wm = window.getWindowManager();
        wm.getDefaultDisplay().getRectSize(windowRect);
        //判断最小高度
        if (sKeyboardMinHeight <= 0) {
            sKeyboardMinHeight = dip2px(window.getContext(), 150);
        }
        final int SCREEN_HEIGHT;

        if (isLandscape) {
            SCREEN_HEIGHT = windowRect.width();
        } else {
            SCREEN_HEIGHT = windowRect.height();
        }

        final View decorView = window.getDecorView();
        final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                decorView.getWindowVisibleDisplayFrame(windowRect);
                final int dy = SCREEN_HEIGHT - windowRect.height();
                if (dy < sKeyboardMinHeight) {
                    //键盘没有弹出来
                    listener.onKeyBoardEvent(false, sKeyboardHeight);
                } else {
                    //键盘弹出来了
                    if (sKeyboardHeight <= 0) {
                        sKeyboardHeight = dy;
                    }
//                    Log.e("noah","键盘高度为:"+sKeyboardHeight);
                    listener.onKeyBoardEvent(true, sKeyboardHeight);
                }
            }
        };
        onLayoutListeners.put(tag, layoutListener);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
    }

    /**
     * 注册键盘监听器
     *
     * @param window      当前所在的window,可以在dialog中使用
     * @param isLandscape 是否横屏状态
     * @param listener    回调
     */
    public static void registerKeyboardListener(Window window, boolean isLandscape, final OnKeyboardListener listener) {
        if (window == null) {
            return;
        }
        registerKeyboardListener(window, window.toString(), isLandscape, listener);
    }

    /**
     * 注册键盘监听器
     *
     * @param window   当前所在的window,可以在dialog中使用
     * @param tag      标记
     * @param listener 回调
     */
    public static void registerKeyboardListener(Window window, String tag, final OnKeyboardListener listener) {
        registerKeyboardListener(window, tag, false, listener);
    }

    /**
     * 注册键盘监听器
     *
     * @param window   当前所在的window,可以在dialog中使用
     * @param listener 回调
     */
    public static void registerKeyboardListener(Window window, final OnKeyboardListener listener) {
        if (window == null) {
            return;
        }
        registerKeyboardListener(window, window.toString(), false, listener);
    }

    /**
     * 取消注册键盘监听器
     *
     * @param window 当前所在的window,可以在dialog中使用
     * @param tag    标记
     */
    public static void unregisterKeyboardListener(Window window, String tag) {
        if (window == null || tag == null) {
            return;
        }
        ViewTreeObserver.OnGlobalLayoutListener listener = onLayoutListeners.get(tag);
        if (listener != null) {
            window.getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    /**
     * 注册键盘监听器
     *
     * @param window 当前所在的window,可以在dialog中使用
     */
    public static void unregisterKeyboardListener(Window window) {
        if (window == null) {
            return;
        }
        unregisterKeyboardListener(window, window.toString());
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
