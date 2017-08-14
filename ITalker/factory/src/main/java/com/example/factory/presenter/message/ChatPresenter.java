package com.example.factory.presenter.message;

import android.support.v7.util.DiffUtil;

import com.example.factory.data.helper.MessageHelper;
import com.example.factory.data.message.MessageDataSource;
import com.example.factory.model.api.message.MsgCreateModel;
import com.example.factory.model.db.Message;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.BaseSourcePresenter;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 聊天Presenter的基础类
 */
public class ChatPresenter<View extends ChatContract.View>
        extends BaseSourcePresenter<Message,Message,MessageDataSource,View>
        implements ChatContract.Presenter{

    //接收者Id，可能是群，或者人的ID
    protected String mReceiverId;
    //区分是人还是群Id
    protected int mReceiverType;


    public ChatPresenter(MessageDataSource source, View view,
                         String mReceiverId, int mReceiverType) {
        super(source, view);
        this.mReceiverId = mReceiverId;
        this.mReceiverType = mReceiverType;
    }

    @Override
    public void pushText(String content) {
        //构建一个新的消息
        MsgCreateModel model = new MsgCreateModel.Builder()
                .receiver(mReceiverId,mReceiverType)
                .content(content,Message.TYPE_STR)
                .build();
        //进行网络发送
        MessageHelper.push(model);
    }

    @Override
    public void pushAudio(String path) {
        //TODO 发送语音
    }

    @Override
    public void pushImages(String[] paths) {
        //TODO 发送图片
    }

    @Override
    public boolean rePush(Message message) {
        if(Account.getUserId().equalsIgnoreCase(message.getSender().getId())
                && message.getStatus() == Message.STATUS_FAILED){

            //更改状态
            message.setStatus(Message.STATUS_CREATED);
            //构建发送Model
            MsgCreateModel model = MsgCreateModel.buildWithMessage(message);
            MessageHelper.push(model);
            return true;
        }
        return false;
    }

    @Override
    public void onDataLoaded(List<Message> messages) {
        ChatContract.View view = getView();
        if(view==null)
            return;

        //拿到老数据
        List<Message> old = view.getRecyclerAdapter().getItems();
        //差异计算
        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(old,messages);
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //进行界面刷新
        refreshData(result,messages);



    }
}
