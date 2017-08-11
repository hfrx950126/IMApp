package com.example.factory.data.message;


import com.example.factory.model.card.MessageCard;

/**
 * 消息中心，进行消息卡片的消费
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface MessageCenter {
    void dispatch(MessageCard... cards);
}
