package net.qiujuer.italker.push.frags.account;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.qiujuer.italker.common.app.Fragment;
import net.qiujuer.italker.push.R;

/**
 * 登录Fragment
 */
public class LoginFragment extends Fragment {

    private AccountTrigger mAccountTrigger;

    public LoginFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        //默认切换为注册界面
        mAccountTrigger.triggerView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到我们的Activity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

}
