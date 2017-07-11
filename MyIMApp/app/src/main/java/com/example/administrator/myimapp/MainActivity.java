package com.example.administrator.myimapp;


import android.media.audiofx.LoudnessEnhancer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.common.app.Activity;


public class MainActivity extends Activity implements IView,View.OnClickListener {

    TextView mResultText;
    EditText mInputText;
    Button mSubmit;
    private IPresenter mPresenter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        mResultText = (TextView) findViewById(R.id.txt_result);
        mInputText  = (EditText) findViewById(R.id.edit_query);
        mSubmit = (Button) findViewById(R.id.btn_submit);
        mSubmit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mPresenter = new Presenter(this);
    }




    @Override
    public String getInputString() {
        return mInputText.getText().toString();
    }

    @Override
    public void setResultString(String string) {
        mResultText.setText(string);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_submit:
                mPresenter.search();
                break;
        }

    }
}
