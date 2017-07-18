package net.qiujuer.italker.push;

import android.widget.EditText;
import android.widget.TextView;

import net.qiujuer.italker.common.app.Activity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity implements IView {
    @BindView(R.id.txt_result)
    TextView mResultText;

    @BindView(R.id.edit_query)
    EditText mInputText;

    private IPresenter mPresenter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter = new Presenter(this);
    }

    @OnClick(R.id.btn_submit)
    void onSubmit() {
        mPresenter.search();
    }

    @Override
    public String getInputString() {
        return mInputText.getText().toString();
    }

    @Override
    public void setResultString(String string) {
        mResultText.setText(string);
    }
}
