package com.keyboard.monitor;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

public class KeyboardHelper{


    private static final String KEYBOARD_NAME="KeyboardHelper";
    private static final String KEYBOARD_HEIGHT_NAME="KeyboardHeight";

    private static int sScreenDisplayHeight=-1;
    private static int sKeyboardMinHeight=-1;
    private static int sKeyboardHeight=-1;
    private static int sDefultKeyboardHeight=dip2px(250);

    /**
     * 可以自己设置最小高度判断
     *
     * @param sMinHeight
     */
    public static void initMinHeight(int sMinHeight){
        KeyboardHelper.sKeyboardMinHeight=sMinHeight;
    }

    /**
     * 初始化最小的默认高度
     *
     * @param sDefultHeight
     */
    public static void initDefultHeight(int sDefultHeight){
        KeyboardHelper.sDefultKeyboardHeight=sDefultHeight;
    }


    /**
     * 注册键盘监听器
     *
     * @param activity
     * @param listener 回调
     */
    public static void registerKeyboardListener(Activity activity,final OnKeyboardListener listener){
        registerKeyboard(activity,activity.hashCode(),listener);
    }


    /**
     * 注册键盘监听器
     *
     * @param activity
     * @param tag      标记
     * @param listener 回调
     */
    public static void registerKeyboardListener(Activity activity,String tag,final OnKeyboardListener listener)
    {
        if(tag!=null){
            registerKeyboard(activity,tag.hashCode(),listener);
        }
    }

    public static void registerKeyboard(Activity activity,int key,final OnKeyboardListener listener){
        if(activity==null||listener==null){
            return;
        }
        if(key >>> 24<2){
            key=-key;
        }
        //判断最小高度
        if(sKeyboardMinHeight<=0){
            sKeyboardMinHeight=dip2px(150);
        }

        if(sKeyboardHeight<=0){
            sKeyboardHeight=activity.getSharedPreferences(KEYBOARD_NAME,Context.MODE_PRIVATE).getInt(
                    KEYBOARD_HEIGHT_NAME,
                    sDefultKeyboardHeight);
        }
        final Rect windowRect=new Rect();
        getDisplayHeight(activity.getWindowManager());

        final View decorView=activity.getWindow().getDecorView();
        final View visibleLayout=((ViewGroup)decorView).getChildAt(0);
        final OnGlobalLayoutListener layoutListener=new OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                decorView.getWindowVisibleDisplayFrame(windowRect);
                //获取可见view的高度
                final int dHeight=decorView.getMeasuredHeight();
                final int vHeight=visibleLayout.getMeasuredHeight();
                //boolean isHasNavigationBar = dHeight>vHeight; //有导航键
                final int dy;
                if(dHeight<sScreenDisplayHeight){
                    //说明当前处于全屏状态,挖孔屏下的状态栏异常,eg:小米9
                    dy=vHeight-windowRect.height();
                } else{
                    //非全屏状态下
                    dy=vHeight-windowRect.bottom;
                }

                if(dy<sKeyboardMinHeight){
                    //键盘没有弹出来
                    listener.onKeyBoardEvent(false,sKeyboardHeight);
                } else{
                    decorView.getContext().getSharedPreferences(KEYBOARD_NAME,Context.MODE_PRIVATE).edit().putInt(
                            KEYBOARD_HEIGHT_NAME,
                            dy).apply();
                    //键盘弹出来了
                    listener.onKeyBoardEvent(true,sKeyboardHeight=dy);
                }
            }
        };


        ViewTreeObserver viewTreeObserver=decorView.getViewTreeObserver();

        //去除重复添加
        Object tag=decorView.getTag(key);
        if(tag instanceof OnGlobalLayoutListener){
            viewTreeObserver.removeOnGlobalLayoutListener((OnGlobalLayoutListener)tag);
        }
        //添加到根布局中
        viewTreeObserver.addOnGlobalLayoutListener(layoutListener);
        decorView.setTag(key,layoutListener);
    }

    /**
     * 获取屏幕的设备绝对高度
     *
     * @param windowManager
     */
    private static void getDisplayHeight(WindowManager windowManager){
        if(sScreenDisplayHeight<=0){
            //全部高度
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                Point point=new Point();
                windowManager.getDefaultDisplay().getRealSize(point);
                sScreenDisplayHeight=point.y;
            } else{
                final Rect windowRect=new Rect();
                windowManager.getDefaultDisplay().getRectSize(windowRect);
                sScreenDisplayHeight=windowRect.height();
            }
        }
    }

    /**
     * 注册键盘监听器
     *
     * @param activity 当前所在的window
     */
    public static void unregisterKeyboardListener(Activity activity){
        if(activity!=null){
            unregisterKeyboardListener(activity,activity.hashCode());
        }
    }

    /**
     * 取消注册键盘监听器
     *
     * @param activity 当前所在的activity
     * @param tag      标记
     */
    public static void unregisterKeyboardListener(Activity activity,String tag){
        if(tag!=null){
            unregisterKeyboardListener(activity,tag.hashCode());
        }
    }

    /**
     * 取消注册键盘监听器
     *
     * @param activity 当前所在的activity
     * @param key      标记
     */
    public static void unregisterKeyboardListener(Activity activity,int key){
        if(key >>> 24<2){
            key=-key;
        }
        if(activity!=null){
            View decorView=activity.getWindow().getDecorView();
            ViewTreeObserver viewTreeObserver=decorView.getViewTreeObserver();

            Object tag=decorView.getTag(key);
            if(tag instanceof OnGlobalLayoutListener){
                viewTreeObserver.removeOnGlobalLayoutListener((OnGlobalLayoutListener)tag);
            }
        }
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private static int dip2px(float dpValue){
        final float scale=Resources.getSystem().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    /**
     * 获取虚拟导航键的高度
     *
     * @param manager
     * @return
     */
    @RequiresApi(api=Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getNavigationBarHeight(WindowManager manager){
        Point point=new Point();
        //不包含导航键高度
        manager.getDefaultDisplay().getSize(point);
        final int y=point.y;
        //全部高度
        manager.getDefaultDisplay().getRealSize(point);
        return point.y-y;
    }


    /**
     * 判断是否存在虚拟键盘
     *
     * @param activity
     * @return
     */
    public static boolean isHasNavigationBar(Activity activity){
        ViewGroup vp=(ViewGroup)activity.getWindow().getDecorView();
        for(int i=0;i<vp.getChildCount();i++){
            if(vp.getChildAt(i).getId()==android.R.id.navigationBarBackground){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否存在状态栏
     *
     * @param activity
     * @return
     */
    public static boolean isHasStatusBar(Activity activity){
        ViewGroup vp=(ViewGroup)activity.getWindow().getDecorView();
        for(int i=0;i<vp.getChildCount();i++){
            if(vp.getChildAt(i).getId()==android.R.id.statusBarBackground){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarheight(Activity activity){
        ViewGroup vp=(ViewGroup)activity.getWindow().getDecorView();
        for(int i=0;i<vp.getChildCount();i++){
            if(vp.getChildAt(i).getId()==android.R.id.statusBarBackground){
                return vp.getMeasuredHeight();
            }
        }
        return 0;
    }


}
