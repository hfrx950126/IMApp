package com.example.factory.data.message;

import com.example.factory.model.db.Message;

import net.qiujuer.italker.common.factory.data.DbDataSource;

/**
 * 消息的数据源定义，他的实现是：MessageRepository
 * 关注的对象是Message表
 */
public interface MessageDataSource extends DbDataSource<Message> {
}
