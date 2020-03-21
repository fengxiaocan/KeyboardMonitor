package com.keyboard.monitor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import androidx.appcompat.widget.AppCompatEditText;

public class InputEditText extends AppCompatEditText implements OnKeyboardRetractListener {

    protected OnKeyboardRetractListener onKeyboardRetractListener;
    public InputEditText(Context context) {
        super(context);
    }

    public InputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new KeyboardInputConnection(this, (BaseInputConnection) super.onCreateInputConnection(outAttrs), this);
    }

    public void setOnKeyboardRetractListener(OnKeyboardRetractListener onKeyboardRetractListener) {
        this.onKeyboardRetractListener = onKeyboardRetractListener;
    }

    @Override
    public void onRetract() {
        //键盘点击收起了
        if (onKeyboardRetractListener != null){
            onKeyboardRetractListener.onRetract();
        }
    }
}
