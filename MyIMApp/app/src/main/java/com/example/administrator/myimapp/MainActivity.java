package com.example.administrator.myimapp;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.common.app.Activity;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    @Override
    protected int getContentLayoutId() {
        return 0;
    }

    @Override
    protected void initWidget() {

    }

    @Override
    protected void initData() {

    }
}
