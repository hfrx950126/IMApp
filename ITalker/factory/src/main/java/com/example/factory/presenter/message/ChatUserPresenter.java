package com.example.factory.presenter.message;

import com.example.factory.data.helper.UserHelper;
import com.example.factory.data.message.MessageDataSource;
import com.example.factory.data.message.MessageRepository;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;

/**
 * Created by Administrator on 2017/8/13.
 */

public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView>
implements ChatContract.Presenter{
    private User mReceiver;
    public ChatUserPresenter(ChatContract.UserView view, String mReceiverId) {
        //数据源、View、接收者、接受者的类型
        super(new MessageRepository(mReceiverId), view, mReceiverId, Message.RECEIVER_TYPE_NONE);
    }

    @Override
    public void start() {
        super.start();

        //从本地拿这个人的信息
        User receiver = UserHelper.findFromLocal(mReceiverId);
        getView().onInit(mReceiver);
    }
}
