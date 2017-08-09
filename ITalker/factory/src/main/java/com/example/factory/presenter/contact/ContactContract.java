package com.example.factory.presenter.contact;

import com.example.factory.model.db.User;

import net.qiujuer.italker.common.factory.presenter.BaseContract;

/**
 * Created by Administrator on 2017/8/9 0009.
 */

public interface ContactContract {
    //什么都不需要额外定义，开始就是调用start即可
    interface Presenter extends BaseContract.Presenter{

    }
    interface View extends BaseContract.RecyclerView<Presenter,User>{
        //界面端只能刷新整个数据集合，不能精确到每一条数据更新
        //void onDone(List<User> users);
        //拿到一个适配器，然后自己自主的进行刷新


    }
}
