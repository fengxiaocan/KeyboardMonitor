package com.keyboard.monitor;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;

public class KeyboardHelper{

    private static final Rect windowRect = new Rect();
    private static Map<String,ViewTreeObserver.OnGlobalLayoutListener> onLayoutListeners = new HashMap<>();

    private static int sScreenDisplayHeight = - 1;
    private static int sKeyboardMinHeight = - 1;
    private static int sKeyboardHeight = - 1;

    /**
     * 可以自己设置最小高度判断
     *
     * @param sMinHeight
     */
    public static void initMinHeight(int sMinHeight){
        KeyboardHelper.sKeyboardMinHeight = sMinHeight;
    }


    /**
     * 注册键盘监听器
     *
     * @param window   当前所在的window,可以在dialog中使用
     * @param listener 回调
     */
    public static void registerKeyboardListener(Window window,final OnKeyboardListener listener){
        if(window == null || listener == null){
            return;
        }
        registerKeyboard(window,window.toString(),listener);
    }


    /**
     * 注册键盘监听器
     *
     * @param window   当前所在的window,可以在dialog中使用
     * @param tag      标记
     * @param listener 回调
     */
    public static void registerKeyboardListener(final Window window,String tag,final OnKeyboardListener listener)
    {
        if(window == null || tag == null || listener == null){
            return;
        }
        registerKeyboard(window,tag,listener);
    }

    private static void registerKeyboard(Window window,String tag,final OnKeyboardListener listener){
        //判断最小高度
        if(sKeyboardMinHeight <= 0){
            sKeyboardMinHeight = dip2px(150);
        }
        getDisplayHeight(window);
        final View decorView = window.getDecorView();
        final View visibleLayout = ((ViewGroup)decorView).getChildAt(0);
        final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                decorView.getWindowVisibleDisplayFrame(windowRect);
                //获取可见view的高度
                final int dHeight = decorView.getMeasuredHeight();
                final int vHeight = visibleLayout.getMeasuredHeight();
                //boolean isHasNavigationBar = dHeight>vHeight; //有导航键
                final int dy;
                if(dHeight < sScreenDisplayHeight){
                    //说明当前处于全屏状态,挖孔屏下的状态栏异常,eg:小米9
                    dy = vHeight - windowRect.height();
                } else{
                    //非全屏状态下
                    dy = vHeight - windowRect.bottom;
                }

                if(dy < sKeyboardMinHeight){
                    //键盘没有弹出来
                    listener.onKeyBoardEvent(false,sKeyboardHeight);
                } else{
                    //键盘弹出来了
                    listener.onKeyBoardEvent(true,sKeyboardHeight = dy);
                }
            }
        };
        onLayoutListeners.put(tag,layoutListener);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
    }

    /**
     * 获取屏幕的设备绝对高度
     *
     * @param window
     */
    private static void getDisplayHeight(Window window){
        if(sScreenDisplayHeight <= 0){
            //全部高度
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                Point point = new Point();
                window.getWindowManager().getDefaultDisplay().getRealSize(point);
                sScreenDisplayHeight = point.y;
            } else{
                window.getWindowManager().getDefaultDisplay().getRectSize(windowRect);
                sScreenDisplayHeight = windowRect.height();
            }
        }
    }


    /**
     * 取消注册键盘监听器
     *
     * @param window 当前所在的window,可以在dialog中使用
     * @param tag    标记
     */
    public static void unregisterKeyboardListener(Window window,String tag){
        if(window == null || tag == null){
            return;
        }
        ViewTreeObserver.OnGlobalLayoutListener listener = onLayoutListeners.remove(tag);
        if(listener != null){
            window.getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    /**
     * 注册键盘监听器
     *
     * @param window 当前所在的window,可以在dialog中使用
     */
    public static void unregisterKeyboardListener(Window window){
        if(window == null){
            return;
        }
        unregisterKeyboardListener(window,window.toString());
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private static int dip2px(float dpValue){
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    /**
     * 获取虚拟导航键的高度
     *
     * @param manager
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getNavigationBarHeight(WindowManager manager){
        Point point = new Point();
        //不包含导航键高度
        manager.getDefaultDisplay().getSize(point);
        final int y = point.y;
        //全部高度
        manager.getDefaultDisplay().getRealSize(point);
        return point.y - y;
    }


    /**
     * 判断是否存在虚拟键盘
     *
     * @param window
     * @return
     */
    public static boolean isHasNavigationBar(Window window){
        ViewGroup vp = (ViewGroup)window.getDecorView();
        for(int i = 0;i < vp.getChildCount();i++){
            if(vp.getChildAt(i).getId() == android.R.id.navigationBarBackground){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否存在状态栏
     *
     * @param window
     * @return
     */
    public static boolean isHasStatusBar(Window window){
        ViewGroup vp = (ViewGroup)window.getDecorView();
        for(int i = 0;i < vp.getChildCount();i++){
            if(vp.getChildAt(i).getId() == android.R.id.statusBarBackground){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取状态栏高度
     *
     * @param window
     * @return
     */
    public static int getStatusBarheight(Window window){
        ViewGroup vp = (ViewGroup)window.getDecorView();
        for(int i = 0;i < vp.getChildCount();i++){
            if(vp.getChildAt(i).getId() == android.R.id.statusBarBackground){
                return vp.getMeasuredHeight();
            }
        }
        return 0;
    }

    /**
     * 判断是否存在虚拟键盘
     *
     * @param activity
     * @return
     */
    public static boolean isHasNavigationBar(Activity activity){
        return isHasNavigationBar(activity.getWindow());
    }

}
