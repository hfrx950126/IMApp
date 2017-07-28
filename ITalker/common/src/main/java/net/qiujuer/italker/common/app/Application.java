package net.qiujuer.italker.common.app;

import android.os.SystemClock;

import java.io.File;

/**
 * Created by Administrator on 2017/7/28 0028.
 */

public class Application extends android.app.Application {
    private static android.app.Application instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 获取缓存文件夹地址
     * @return
     */
    public static File getCacheDirFile(){
        return instance.getCacheDir();
    }
    public static File getPortraitTmpFile() {
        //得到头像目录的缓存地址
        File dir = new File(getPortraitTmpFile(),"portrait");
        //创建所有的对应的文件夹
        dir.mkdirs();

        File path = new File(dir, SystemClock.uptimeMillis()+);
    }
}
