package com.example.factory.data.user;


import com.example.factory.model.card.UserCard;

/**
 * 用户中心的基本定义
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface UserCenter {
    // 分发处理一堆用户卡片的信息，并更新到数据库
    void dispatch(UserCard... cards);
}
