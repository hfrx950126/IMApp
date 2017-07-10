package com.example.common.app;

import android.app.*;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author  HfRX
 */

public abstract class Fragment extends android.support.v4.app.Fragment{
    protected View mRoot;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // 初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            int layId = getContentLayoutId();
            //初始化当前的跟布局，但是不在创建时就添加到container里面
            View root = inflater.inflate(layId,container,false);
            initWidget(root);
            mRoot = root;
        }else{
            if(mRoot.getParent()!=null){
                // 把当前Root从其父控件中移除
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;

    }

    /**
     * 初始化相关参数
     * @param bundle   参数Bundle
     * @return 如果参数正确，返回True，错误返回False
     */
    protected  void initArgs(Bundle bundle){

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //当View创建完后才能后初始化数据
        initData();
    }

    /**
     * 得到当前界面的资源文件ID
     * @return  资源文件ID
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(View root){

    }

    /**
     * 初始化数据
     */
    protected  void initData(){

    }

    /**
     * 返回按键触发时调用
     * @return 返回True代表我已处理返回逻辑，Activity不用自己Finish。
     * 返回False代表我没有处理逻辑，Activity自己走自己的逻辑
     */
    public boolean onBackPresed(){
        return false;
    }
}
