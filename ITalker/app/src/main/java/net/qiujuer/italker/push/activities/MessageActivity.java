package net.qiujuer.italker.push.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Session;

import net.qiujuer.italker.common.app.Activity;
import net.qiujuer.italker.common.app.Fragment;
import net.qiujuer.italker.common.factory.model.Author;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.frags.message.ChatGroupFragment;
import net.qiujuer.italker.push.frags.message.ChatUserFragment;

public class MessageActivity extends Activity {
    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";
    public static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";

    private String mReceiverId;
    private boolean mIsGroup;

    /**
     * 通过Session发起聊天
     * @param context  上下文
     * @param session Session
     */
    public static void show(Context context, Session session){
        if(session==null||context==null||TextUtils.isEmpty(session.getId()))
            return;
        Intent intent = new Intent(context,MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID,session.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP,session.getReceiverType()== Message.RECEIVER_TYPE_GROUP);
        context.startActivity(intent);
    }

    /**
     * 显示人的聊天界面
     * @param context  上下文
     * @param author 人的信息
     */
    public static void show(Context context, Author author){
        if(author==null||context==null)
            return;
        Intent intent = new Intent(context,MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID,author.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP,false);
        context.startActivity(intent);
    }
    /**
     * 发起群聊天
     * @param context  上下文
     * @param group  群的Model
     */
    public static void show(Context context, Group group){
        if(group==null||context==null)
            return;
        Intent intent = new Intent(context,MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID,group.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP,true);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
        mIsGroup = bundle.getBoolean(KEY_RECEIVER_IS_GROUP);
        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        Fragment fragment;
        if(mIsGroup)
            fragment = new ChatGroupFragment();
        else
            fragment = new ChatUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID,mReceiverId);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container,fragment)
                .commit();

    }
}
