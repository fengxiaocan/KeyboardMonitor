package com.keyboard.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;

import com.box.libs.DebugBox;
import com.keyboard.monitor.KeyboardHelper;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        DebugBox.init(getApplication());
        DebugBox.get().open();
        KeyboardHelper. bindActivity(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        startActivity(new Intent(this,TwoActivity.class));
    }
}
