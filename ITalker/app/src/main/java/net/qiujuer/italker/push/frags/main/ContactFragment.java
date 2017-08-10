package net.qiujuer.italker.push.frags.main;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.factory.model.db.User;
import com.example.factory.presenter.contact.ContactContract;
import com.example.factory.presenter.contact.ContactPresenter;

import net.qiujuer.italker.common.app.PresenterFragment;
import net.qiujuer.italker.common.widget.EmptyView;
import net.qiujuer.italker.common.widget.PortraitView;
import net.qiujuer.italker.common.widget.recycler.RecyclerAdapter;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.activities.MessageActivity;
import net.qiujuer.italker.push.activities.PersonalActivity;

import butterknife.BindView;


public class ContactFragment extends PresenterFragment<ContactContract.Presenter>
implements ContactContract.View{

    @BindView(R.id.empty)
    EmptyView mEmptyView;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    // 适配器，User，可以直接从数据库查询数据
    private RecyclerAdapter<User> mAdapter;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        //初始化Recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<User>() {
            @Override
            protected int getItemViewType(int position, User user) {
                //返回cell的布局ID
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return new ContactFragment.ViewHolder(root);
            }
        });
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<User>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, User user) {
                //跳转到聊天界面
                MessageActivity.show(getContext(),user);
            }
        });

        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        //进行界面操作
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount()>0);
    }

    @Override
    protected ContactContract.Presenter initPresenter() {
        //初始化Presenter
        return new ContactPresenter(this);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        mPresenter.start();
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<User>{

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_desc)
        TextView mDesc;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        void onPortraitClick(){
            //发起关注
            PersonalActivity.show(getContext(),mData.getId());
        }
        @Override
        protected void onBind(User user) {
           mPortraitView.setup(Glide.with(ContactFragment.this),user);
            mName.setText(user.getName());
            mDesc.setText(user.getDesc());
        }
    }
}
