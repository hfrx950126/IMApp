package net.qiujuer.italker.push.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.qiujuer.italker.common.factory.model.Author;
import net.qiujuer.italker.push.R;

public class MessageActivity extends AppCompatActivity {

    /**
     * 显示人的聊天界面
     * @param context  上下文
     * @param author 人的信息
     */
    public static void show(Context context, Author author){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }
}
