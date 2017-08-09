package com.example.factory.presenter.contact;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.util.DiffUtil;

import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.AppDatabase;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.persistence.Account;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.qiujuer.italker.common.factory.data.DataSource;
import net.qiujuer.italker.common.factory.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人的Presenter实现
 */
public class ContactPresenter extends BasePresenter<ContactContract.View>
        implements ContactContract.Presenter {

    public ContactPresenter(ContactContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();

        //加载本地数据库数据
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
                        getView().getRecyclerAdapter().replace(tResult);
                        getView().onAdapterDataChanged();
                    }
                })
                .execute();

        //加载网络数据
        UserHelper.refreshContacts(new DataSource.Callback<List<UserCard>>() {
            @Override
            public void onDataNotAvailable(@StringRes int strRes) {
                //网络失败，因为有本地数据，不管错误
            }

            @Override
            public void onDataLoaded(final List<UserCard> userCards) {
                //转换为User
                final List<User> users = new ArrayList<User>();
                for (UserCard userCard : userCards) {
                    users.add(userCard.build());
                }
                //丢到事务中保存数据库
                DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
                definition.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        FlowManager.getModelAdapter(User.class)
                                .saveAll(users);
                    }
                }).build().execute();
                //网络的数据往往是新的。我们需要直接刷新到界面
                getView().getRecyclerAdapter().replace(users);
                getView().onAdapterDataChanged();
            }
        });
        //TODO 问题：
        //关注后虽然存储数据库，但是没有刷新联系人
        //如果是花心数据库、或者从网络刷新，最终刷新的时候是全局刷新
        //本地刷新和网络刷新，在添加到界面的时候会有可能冲突
        //如何识别已经在数据库中有这样的数据了
    }
    private void diff(List<User> newList,List<User> oldList){
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return 0;
            }

            @Override
            public int getNewListSize() {
                return 0;
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return false;
            }
        });


        //
        result.dispatchUpdatesTo(getView().getRecyclerAdapter());
    }
}
