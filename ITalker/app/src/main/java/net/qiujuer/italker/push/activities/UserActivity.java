package net.qiujuer.italker.push.activities;


import android.content.Context;
import android.content.Intent;


import net.qiujuer.italker.common.app.Activity;
import net.qiujuer.italker.common.app.Fragment;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.frags.user.UpdateInfoFragment;

/**
 * 用户信息界面
 * 可以提供用户信息修改
 */
public class UserActivity extends Activity {
    private Fragment mCurFragment;

    /**
     * 显示界面的入口方法
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,UserActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }
    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurFragment.onActivityResult(requestCode,resultCode,data);
    }
}
