package com.keyboard.test;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.box.libs.DebugBox;
import com.keyboard.monitor.KeyboardHelper;

public class TwoActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        DebugBox.init(getApplication());
        DebugBox.get().open();
        KeyboardHelper. bindActivity(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

    }
}
