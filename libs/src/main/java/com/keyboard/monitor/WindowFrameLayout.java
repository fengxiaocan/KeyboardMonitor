package com.keyboard.monitor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class WindowFrameLayout extends FrameLayout{

    private final Rect windowRect = new Rect();
    private final int SCREEN_HEIGHT = getScreenHeight();
    private final String WINDOW_KEY ;

    public WindowFrameLayout(@NonNull Context context,String key){
        super(context);
        WINDOW_KEY = key;
    }


    public void attachActivity(Activity activity){
        View view = activity.findViewById(android.R.id.content);
        ViewParent parent = view.getParent().getParent();
        ViewGroup frameLayout = (ViewGroup)parent;

        ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
        setLayoutParams(layoutParams);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            setClipChildren(frameLayout.getClipChildren());
        }
        setWillNotDraw(frameLayout.willNotDraw());
        setBackground(frameLayout.getBackground());
        setAlpha(frameLayout.getAlpha());

        List<View> views = new ArrayList<>();
        for(int i = 0;i < frameLayout.getChildCount();i++){
            View childAt = frameLayout.getChildAt(i);
            views.add(childAt);
        }
        for(View view1: views){
            frameLayout.removeView(view1);
            addView(view1);
        }
        ViewGroup viewParent = ((ViewGroup)frameLayout.getParent());
        viewParent.removeView(frameLayout);
        viewParent.addView(this);
    }

    @Override
    protected void onLayout(boolean changed,int left,int top,int right,int bottom){
        super.onLayout(changed,left,top,right,bottom);
        getWindowVisibleDisplayFrame(windowRect);
        final int dy = SCREEN_HEIGHT - windowRect.bottom;
        if(dy <= 0){

            //键盘没有弹出来
            Log.e("noah","键盘没有弹出来");
        } else{
            //键盘弹出来了
            Log.e("noah","键盘高度为=" + dy);
        }
    }

    protected int getScreenHeight(){
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getRectSize(windowRect);
        return windowRect.height();
    }
}
