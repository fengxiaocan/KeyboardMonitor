package com.keyboard.test;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.keyboard.monitor.KeyboardHelper;
import com.keyboard.monitor.OnKeyboardListener;

public class MainActivity extends AppCompatActivity{
    private TextView tvPan;
    private EditText etInput;
    private Button btAdd;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        KeyboardHelper.registerKeyboardListener(getWindow(),new OnKeyboardListener(){
            @Override
            public void onKeyBoardEvent(boolean isShow,int height){
                if(isShow){
                    tvPan.setVisibility(View.GONE);
                } else{
                    if(height > 0){
                        tvPan.setText("Keyboard Height:" + height);
                        tvPan.getLayoutParams().height = height;
                        tvPan.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void initView(){
        tvPan = findViewById(R.id.tv_pan);
        etInput = findViewById(R.id.et_input);
        btAdd = findViewById(R.id.bt_add);
        layout = findViewById(R.id.layout);
        btAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new AlertDialog.Builder(MainActivity.this).setTitle("TITLE").setMessage("message").show();
            }
        });
    }
}
