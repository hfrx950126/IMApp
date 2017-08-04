package net.qiujuer.italker.common.app;

import android.app.*;
import android.content.Context;
import android.support.annotation.StringRes;

import net.qiujuer.italker.common.factory.presenter.BaseContract;

/**
 * Created by Administrator on 2017/8/4 0004.
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends Fragment
        implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    @Override
    public void showError(@StringRes int str) {
        Application.showToast(str);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //在界面onAttach之后就触发初始化Presenter
        initPresenter();
    }

    /**
     * 初始化presenter
     * @return Presenter
     */
    protected abstract Presenter initPresenter();

    @Override
    public void showLoading() {
        //TODO 显示一个Loading
    }

    @Override
    public void setPresenter(Presenter presenter) {
        //View中赋值Presenter
        mPresenter = presenter;
    }
}
