package net.qiujuer.italker.push.frags.message;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;


import com.bumptech.glide.Glide;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.message.ChatContract;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.EditText;
import net.qiujuer.genius.ui.widget.Loading;
import net.qiujuer.genius.ui.widget.TextView;
import net.qiujuer.italker.common.app.Fragment;
import net.qiujuer.italker.common.app.PresenterFragment;
import net.qiujuer.italker.common.factory.presenter.BaseContract;
import net.qiujuer.italker.common.widget.PortraitView;
import net.qiujuer.italker.common.widget.adapter.TextWatcherAdapter;
import net.qiujuer.italker.common.widget.recycler.RecyclerAdapter;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.activities.MessageActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/12.
 */

public abstract class ChatFragment<InitModel>
        extends PresenterFragment<ChatContract.Presenter>
implements AppBarLayout.OnOffsetChangedListener,
        ChatContract.View<InitModel>{

    protected String mReceiverId;
    protected Adapter mAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingLayout ;

    @BindView(R.id.edit_content)
    EditText mContent;

    @BindView(R.id.btn_submit)
    View mSubmit;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId= bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        initToolbar();
        initAppbar();
        initEditContent();

        //RecyclerView基本设置
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        //开始进行初始化
        mPresenter.start();
    }

    //初始化Toolbar
    protected void initToolbar(){
        Toolbar toolbar = mToolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }
    //给界面的Appbar设置一个监听，得到关闭与打开的时候的进度
    private void initAppbar() {
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    private void initEditContent(){
       mContent.addTextChangedListener(new TextWatcherAdapter() {
           @Override
           public void afterTextChanged(Editable s) {
            String content = s.toString().trim();
            boolean needSendMsg = TextUtils.isEmpty(content);
            mSubmit.setActivated(needSendMsg);
           }
       });
    }

    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    @OnClick(R.id.btn_face)
    void onFaceClick(){
        
    }
    @OnClick(R.id.btn_record)
    void onRecordClick(){
        
    }
    
    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        if(mSubmit.isActivated()){
            //发送
            String content = mContent.getText().toString().trim();
            mContent.setText("");
            mPresenter.pushText(content);
        }else{
            onMoreClick();
        }
    }

    private void onMoreClick() {

    }

    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        //界面没有占位布局，Recycler是一直显示的，所以不需要做事情
    }

    //Holder的基类
    private class Adapter extends RecyclerAdapter<Message>{

        @Override
        protected int getItemViewType(int position, Message message) {
            // 我发送的在右边，收到的在左边
            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUserId());

            switch (message.getType()) {
                // 文字内容
                case Message.TYPE_STR:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;

                // 语音内容
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;

                // 图片内容
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;

                // 其他内容：文件
                default:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
            }
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType) {
                // 左右都是同一个
                case R.layout.cell_chat_text_right:
                case R.layout.cell_chat_text_left:
                    return new TextHolder(root);

                case R.layout.cell_chat_audio_right:
                case R.layout.cell_chat_audio_left:
                    return new AudioHolder(root);

                case R.layout.cell_chat_pic_right:
                case R.layout.cell_chat_pic_left:
                    return new PicHolder(root);

                // 默认情况下，返回的就是Text类型的Holder进行处理
                // 文件的一些实现
                default:
                    return new TextHolder(root);
            }
        }
    }

    //文字的Holder
    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {
        @BindView(R.id.im_portrait)
        PortraitView mPortrait;

        //允许为空，左边没有，右边有
        @BindView(R.id.loading)
        Loading mLoading;

        public BaseHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            //进行数据加载
            sender.load();
            //头像加载
            mPortrait.setup(Glide.with(ChatFragment.this),sender);

            if (mLoading!=null){
                //当前布局应该是在右边
                int status = message.getStatus();
                if(status==Message.STATUS_DONE){
                    //正常状态
                    mLoading.stop();
                    mLoading.setVisibility(View.GONE);
                }else if(status==Message.STATUS_CREATED){
                    //正在发送中的状态
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(0);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(),R.color.colorAccent));
                    mLoading.start();
                }else if(status==Message.STATUS_FAILED){
                    //发送失败状态,允许重新发送
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(1);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(),R.color.alertImportant));
                    mLoading.stop();
                }

                //当状态是错误状态时才允许点击
                mPortrait.setEnabled(status == Message.STATUS_FAILED);
            }
        }

        @OnClick(R.id.im_portrait)
        void onRePushClick(){
            //重新发送
            if(mLoading!=null&&mPresenter.rePush(mData)){
                //必须是右边的才有可能需要重新发送
                //状态改变需要重新刷新界面当前的信息
                updateData(mData);
            }

        }

    }

    class TextHolder extends BaseHolder {
        @BindView(R.id.txt_content)
        TextView mContent;
        public TextHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);

            //把内容设置到布局上
            mContent.setText(message.getContent());
        }
    }

    // 语音的Holder
    class AudioHolder extends BaseHolder {

        public AudioHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            // TODO
        }
    }

    // 图片的Holder
    class PicHolder extends BaseHolder {

        public PicHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            // TODO
        }
    }

}
