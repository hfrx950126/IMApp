package net.qiujuer.italker.push;

import com.example.factory.Factory;
import com.igexin.sdk.PushManager;

import net.qiujuer.italker.common.app.Application;

/**
 * Created by Administrator on 2017/7/29.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //调用Factory进行初始化
        Factory.setup();
        //推送进行初始化
        PushManager.getInstance().initialize(this);
    }
}
