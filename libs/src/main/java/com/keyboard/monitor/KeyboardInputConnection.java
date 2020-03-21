package com.keyboard.monitor;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputContentInfo;

import androidx.annotation.RequiresApi;

/**
 * 重写{@link android.widget.EditText#onCreateInputConnection(EditorInfo)}方法,传入super.onCreateInputConnection
 */
public class KeyboardInputConnection extends BaseInputConnection {
    private BaseInputConnection baseInputConnection;
    private OnKeyboardRetractListener onKeyboardRetractListener;

    public void setOnKeyboardRetractListener(OnKeyboardRetractListener onKeyboardRetractListener) {
        this.onKeyboardRetractListener = onKeyboardRetractListener;
    }

    public KeyboardInputConnection(View targetView, BaseInputConnection connection) {
        super(targetView, true);
        baseInputConnection = connection;
    }

    public KeyboardInputConnection(View targetView, BaseInputConnection connection,OnKeyboardRetractListener listener) {
        super(targetView, true);
        baseInputConnection = connection;
        onKeyboardRetractListener = listener;
    }

    @Override
    public Editable getEditable() {
        return baseInputConnection.getEditable();
    }

    @Override
    public boolean beginBatchEdit() {
        return baseInputConnection.beginBatchEdit();
    }

    @Override
    public boolean endBatchEdit() {
        return baseInputConnection.endBatchEdit();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void closeConnection() {
        super.closeConnection();
        baseInputConnection.closeConnection();
    }

    @Override
    public boolean clearMetaKeyStates(int states) {
        return baseInputConnection.clearMetaKeyStates(states);
    }

    @Override
    public boolean commitCompletion(CompletionInfo text) {
        return baseInputConnection.commitCompletion(text);
    }

    @Override
    public boolean commitCorrection(CorrectionInfo correctionInfo) {
        return baseInputConnection.commitCorrection(correctionInfo);
    }

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        return baseInputConnection.commitText(text, newCursorPosition);
    }

    @Override
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        return baseInputConnection.deleteSurroundingText(beforeLength, afterLength);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean deleteSurroundingTextInCodePoints(int beforeLength, int afterLength) {
        return baseInputConnection.deleteSurroundingTextInCodePoints(beforeLength, afterLength);
    }

    @Override
    public boolean finishComposingText() {
        //这里就是点击收起键盘的回调
        if (onKeyboardRetractListener != null){
            onKeyboardRetractListener.onRetract();
        }
        return baseInputConnection.finishComposingText();
    }

    @Override
    public int getCursorCapsMode(int reqModes) {
        return baseInputConnection.getCursorCapsMode(reqModes);
    }

    @Override
    public ExtractedText getExtractedText(ExtractedTextRequest request, int flags) {
        return baseInputConnection.getExtractedText(request, flags);
    }

    @Override
    public CharSequence getTextBeforeCursor(int length, int flags) {
        return baseInputConnection.getTextBeforeCursor(length, flags);
    }

    @Override
    public CharSequence getSelectedText(int flags) {
        return baseInputConnection.getSelectedText(flags);
    }

    @Override
    public CharSequence getTextAfterCursor(int length, int flags) {
        return baseInputConnection.getTextAfterCursor(length, flags);
    }

    @Override
    public boolean performEditorAction(int actionCode) {
        return baseInputConnection.performEditorAction(actionCode);
    }

    @Override
    public boolean performContextMenuAction(int id) {
        return baseInputConnection.performContextMenuAction(id);
    }

    @Override
    public boolean performPrivateCommand(String action, Bundle data) {
        return baseInputConnection.performPrivateCommand(action, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean requestCursorUpdates(int cursorUpdateMode) {
        return baseInputConnection.requestCursorUpdates(cursorUpdateMode);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Handler getHandler() {
        return baseInputConnection.getHandler();
    }

    @Override
    public boolean setComposingText(CharSequence text, int newCursorPosition) {
        return baseInputConnection.setComposingText(text, newCursorPosition);
    }

    @Override
    public boolean setComposingRegion(int start, int end) {
        return baseInputConnection.setComposingRegion(start, end);
    }

    @Override
    public boolean setSelection(int start, int end) {
        return baseInputConnection.setSelection(start, end);
    }

    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        return baseInputConnection.sendKeyEvent(event);
    }

    @Override
    public boolean reportFullscreenMode(boolean enabled) {
        return baseInputConnection.reportFullscreenMode(enabled);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    public boolean commitContent(InputContentInfo inputContentInfo, int flags, Bundle opts) {
        return baseInputConnection.commitContent(inputContentInfo, flags, opts);
    }


}
