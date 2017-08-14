package com.example.factory.presenter.message;

import com.example.factory.model.db.Session;
import com.example.factory.model.db.User;
import com.example.factory.presenter.contact.ContactContract;

import net.qiujuer.italker.common.factory.presenter.BaseContract;

/**
 * Created by Administrator on 2017/8/13.
 */

public interface SessionContract {
    interface Presenter extends BaseContract.Presenter{

    }
    interface View extends BaseContract.RecyclerView<Presenter,Session>{

    }
}
