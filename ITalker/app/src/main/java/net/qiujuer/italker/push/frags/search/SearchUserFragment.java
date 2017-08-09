package net.qiujuer.italker.push.frags.search;


import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.factory.model.card.UserCard;
import com.example.factory.presenter.contact.FollowContract;
import com.example.factory.presenter.contact.FollowPresenter;
import com.example.factory.presenter.search.SearchContract;
import com.example.factory.presenter.search.SearchUserPresenter;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;
import net.qiujuer.italker.common.app.PresenterFragment;
import net.qiujuer.italker.common.widget.EmptyView;
import net.qiujuer.italker.common.widget.PortraitView;
import net.qiujuer.italker.common.widget.recycler.RecyclerAdapter;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.activities.SearchActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 搜索人的界面实现
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment, SearchContract.UserView {
    @BindView(R.id.empty)
    EmptyView mEmptyView;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    public SearchUserFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        //初始化Recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(new RecyclerAdapter<UserCard>() {
            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                //返回cell的布局ID
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        });
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    public void search(String content) {
        //Activity->Fragment->Presenter->Net
        mPresenter.search(content);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        //数据成功的情况下返回数据
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        //初始化Presenter
        return new SearchUserPresenter(this);
    }

    @Override
    protected void initData() {
        super.initData();
        //发起首次搜索
        search("");
    }

    /**
     * 每一个cell的布局操作
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard>
            implements FollowContract.View {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;
        @BindView(R.id.txt_name)
        TextView mName;
        @BindView(R.id.im_follow)
        ImageView mFollow;

        private FollowContract.Presenter mPresenter;

        public ViewHolder(View itemView) {
            super(itemView);
            //当前view和Presenter绑定
            new FollowPresenter(this);
        }

        @Override
        protected void onBind(UserCard userCard) {
            mPortraitView.setup(Glide.with(SearchUserFragment.this),userCard);
            mName.setText(userCard.getName());
            mFollow.setEnabled(!userCard.isFollow());
        }
        @OnClick(R.id.im_follow)
        void onFollowClick(){
            //发起关注
            mPresenter.follow(mData.getId());
        }

        @Override
        public void showError(@StringRes int str) {
            //更改Drawable状态
            if(mFollow.getDrawable() instanceof LoadingDrawable){
                //失败则停止动画，并且显示一个圆圈
              LoadingDrawable drawable = (LoadingDrawable) mFollow.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
            }

        }

        @Override
        public void showLoading() {
            int minsize = (int) Ui.dipToPx(getResources(),22);
            int maxsize = (int) Ui.dipToPx(getResources(),30);
            //初始化一个圆形的动画的Drawable
            LoadingDrawable drawable = new LoadingCircleDrawable(minsize,maxsize);
            drawable.setBackgroundColor(0);
            int[] color = new int[]{UiCompat.getColor(getResources(),R.color.white_alpha_208)};
            drawable.setForegroundColor(color);
            mFollow.setImageDrawable(drawable);
            drawable.start();

        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void onFollowSucceed(UserCard userCard) {
            //更改Drawable状态
            if(mFollow.getDrawable() instanceof LoadingDrawable){
                ((LoadingDrawable) mFollow.getDrawable()).stop();
                //设置为默认的
                mFollow.setImageResource(R.drawable.sel_opt_done_add);
            }
            //发起更新
            updateData(userCard);
        }
    }
}
