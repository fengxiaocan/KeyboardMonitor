package com.keyboard.monitor;

import android.app.Activity;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeyboardHelper{

    private static Map<String,Set<OnKeyboardListener>> listeners = new HashMap<>();

    public static void bindActivity(AppCompatActivity activity){
        final String window_key = activity.getWindow().toString();

        WindowFrameLayout layout = new WindowFrameLayout(activity,window_key);
        layout.attachActivity(activity);

        activity.getLifecycle().addObserver(new LifecycleEventObserver(){
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source,@NonNull Lifecycle.Event event){
                if(event == Lifecycle.Event.ON_DESTROY){
                    KeyboardHelper.listeners.remove(window_key);
                }
            }
        });
    }

    public static void bindActivity(Activity activity){
        final String window_key = activity.getWindow().toString();

        WindowFrameLayout layout = new WindowFrameLayout(activity,window_key);
        layout.attachActivity(activity);
    }


    public static void unbindActivity(Activity activity){
        final String window_key = activity.getWindow().toString();
        listeners.remove(window_key);
    }

    public static void addOnKeyboardListener(Window window,String tag,OnKeyboardListener listener){
        final String window_key = window.toString();
        Set<OnKeyboardListener> set = listeners.get(window_key);
        if(set == null){
            set = new HashSet<>();
        }
        set.add(listener);
    }

    public static void removeOnKeyboardListener(Window window,OnKeyboardListener listener){
        final String window_key = window.toString();
        Set<OnKeyboardListener> set = listeners.get(window_key);
        if(set == null){
            set = new HashSet<>();
        }
        set.add(listener);
    }
}
